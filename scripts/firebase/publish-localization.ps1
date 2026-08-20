[CmdletBinding()]
param(
    [string]$ProjectId = $env:FIREBASE_PROJECT_ID,
    [string]$LocalizationPath = "core/core-resource/src/commonMain/composeResources/files/localization.json",
    [switch]$ValidateOnly,
    [switch]$Publish
)

$ErrorActionPreference = "Stop"

function Write-Utf8WithoutBom {
    param(
        [Parameter(Mandatory = $true)]
        [string]$Path,
        [Parameter(Mandatory = $true)]
        [string]$Content
    )

    $encoding = New-Object System.Text.UTF8Encoding($false)
    [System.IO.File]::WriteAllText($Path, $Content, $encoding)
}

function Get-Placeholders {
    param([string]$Value)

    if ($null -eq $Value) {
        return @()
    }

    return @(
        [regex]::Matches($Value, '%\d+\$[sd]') |
            ForEach-Object { $_.Value } |
            Sort-Object
    )
}

function Assert-LocalizationDocument {
    param([Parameter(Mandatory = $true)]$Document)

    if ($Document.schemaVersion -ne 1) {
        throw "localization.json must use schemaVersion 1."
    }

    if ($null -eq $Document.languageRoster) {
        throw "localization.json must contain languageRoster."
    }

    $languageProperties = @($Document.languageRoster.PSObject.Properties)
    if ($languageProperties.Count -eq 0) {
        throw "localization.json must contain at least one language."
    }

    $defaultLanguage = [string]$Document.defaultLanguage
    if ([string]::IsNullOrWhiteSpace($defaultLanguage)) {
        throw "localization.json must contain defaultLanguage."
    }

    $defaultLanguageProperty = $Document.languageRoster.PSObject.Properties[$defaultLanguage]
    if ($null -eq $defaultLanguageProperty) {
        throw "localization.json must contain default language '$defaultLanguage'."
    }

    $defaultStrings = $defaultLanguageProperty.Value
    $defaultKeys = @($defaultStrings.PSObject.Properties.Name | Sort-Object)
    if ($defaultKeys.Count -eq 0) {
        throw "localization.json must contain strings."
    }

    foreach ($languageProperty in $languageProperties) {
        $language = $languageProperty.Name
        $strings = $languageProperty.Value
        $languageKeys = @($strings.PSObject.Properties.Name | Sort-Object)
        $keyDifference = @(Compare-Object -ReferenceObject $defaultKeys -DifferenceObject $languageKeys)

        if ($keyDifference.Count -ne 0) {
            $details = ($keyDifference | ForEach-Object { "{0} ({1})" -f $_.InputObject, $_.SideIndicator }) -join ", "
            throw "Localization keys for '$language' do not match '$defaultLanguage': $details"
        }

        foreach ($key in $defaultKeys) {
            $defaultValue = [string]$defaultStrings.$key
            $translatedValue = [string]$strings.$key

            if ([string]::IsNullOrEmpty($defaultValue) -or [string]::IsNullOrEmpty($translatedValue)) {
                throw "Localization value must not be empty for '$language/$key'."
            }

            $defaultPlaceholders = @(Get-Placeholders $defaultValue)
            $translatedPlaceholders = @(Get-Placeholders $translatedValue)
            $placeholderDifference = @(
                Compare-Object -ReferenceObject $defaultPlaceholders -DifferenceObject $translatedPlaceholders
            )

            if ($placeholderDifference.Count -ne 0) {
                throw "Placeholder mismatch for '$language/$key'. Default=[$($defaultPlaceholders -join ', ')] Translation=[$($translatedPlaceholders -join ', ')]"
            }
        }
    }

    $languageRoster = @($languageProperties.Name | Sort-Object)
    Write-Host "Localization validation passed: $($defaultKeys.Count) keys in $($languageRoster.Count) languages [$($languageRoster -join ', ')], default '$defaultLanguage'."
}

$repoRoot = (Resolve-Path (Join-Path $PSScriptRoot "..\..")).Path
$resolvedLocalizationPath = if ([System.IO.Path]::IsPathRooted($LocalizationPath)) {
    $LocalizationPath
} else {
    Join-Path $repoRoot $LocalizationPath
}

if (-not (Test-Path $resolvedLocalizationPath)) {
    throw "Localization file not found: $resolvedLocalizationPath"
}

$localizationRaw = Get-Content -Raw -Path $resolvedLocalizationPath
try {
    $localization = $localizationRaw | ConvertFrom-Json
} catch {
    throw "localization.json is not valid JSON: $($_.Exception.Message)"
}

Assert-LocalizationDocument $localization

if ($ValidateOnly) {
    Write-Host "Validation-only run complete. Firebase was not contacted."
    return
}

if ([string]::IsNullOrWhiteSpace($ProjectId)) {
    throw "Firebase project ID is missing. Pass -ProjectId or set FIREBASE_PROJECT_ID."
}

$firebase = Get-Command firebase -ErrorAction SilentlyContinue
if ($null -eq $firebase) {
    throw "Firebase CLI was not found. Install firebase-tools and run 'firebase login' once before using this script."
}

$compactLocalization = $localization | ConvertTo-Json -Depth 100 -Compress

$tempDir = Join-Path ([System.IO.Path]::GetTempPath()) ("compose-screen-example-remote-config-" + [Guid]::NewGuid().ToString("N"))
New-Item -ItemType Directory -Path $tempDir | Out-Null

$currentTemplatePath = Join-Path $tempDir "current.remoteconfig.template.json"
$patchedTemplatePath = Join-Path $tempDir "remoteconfig.template.json"
$firebaseConfigPath = Join-Path $tempDir "firebase.json"

$keepTemp = -not $Publish

try {
    Write-Host "Fetching current Remote Config template from Firebase project '$ProjectId'..."
    & firebase remoteconfig:get --project $ProjectId -o $currentTemplatePath
    if ($LASTEXITCODE -ne 0) {
        throw "Firebase CLI failed to download the current Remote Config template."
    }

    $templateRaw = Get-Content -Raw -Path $currentTemplatePath
    $template = $templateRaw | ConvertFrom-Json

    if ($null -eq $template.parameters) {
        $template | Add-Member -NotePropertyName parameters -NotePropertyValue ([pscustomobject]@{})
    }

    $localizationParameter = $template.parameters.localization
    if ($null -eq $localizationParameter) {
        $localizationParameter = [pscustomobject]@{}
        $template.parameters | Add-Member -NotePropertyName localization -NotePropertyValue $localizationParameter
    }

    $localizationParameter | Add-Member -NotePropertyName defaultValue -NotePropertyValue ([pscustomobject]@{
        value = $compactLocalization
    }) -Force
    $localizationParameter | Add-Member -NotePropertyName valueType -NotePropertyValue "JSON" -Force

    if ($null -eq $localizationParameter.description -or [string]::IsNullOrWhiteSpace([string]$localizationParameter.description)) {
        $localizationParameter | Add-Member -NotePropertyName description -NotePropertyValue "ComposeScreenExample localization document" -Force
    }

    if ($null -ne $template.PSObject.Properties["etag"]) {
        $template.PSObject.Properties.Remove("etag")
    }
    if ($null -ne $template.PSObject.Properties["version"]) {
        $template.PSObject.Properties.Remove("version")
    }

    $patchedTemplateJson = $template | ConvertTo-Json -Depth 100
    Write-Utf8WithoutBom -Path $patchedTemplatePath -Content $patchedTemplateJson

    $firebaseConfig = [ordered]@{
        remoteconfig = [ordered]@{
            template = "remoteconfig.template.json"
        }
    } | ConvertTo-Json -Depth 10
    Write-Utf8WithoutBom -Path $firebaseConfigPath -Content $firebaseConfig

    if (-not $Publish) {
        Write-Host "Dry run complete. Nothing was published."
        Write-Host "Generated template: $patchedTemplatePath"
        Write-Host "Run again with -Publish when ready:"
        Write-Host ".\scripts\firebase\publish-localization.ps1 -Publish -ProjectId <firebase-project-id>"
        return
    }

    Write-Host "Publishing patched Remote Config template..."
    Push-Location $tempDir
    try {
        & firebase deploy --only remoteconfig --project $ProjectId --config firebase.json --non-interactive
        if ($LASTEXITCODE -ne 0) {
            throw "Firebase CLI failed to publish Remote Config."
        }
    } finally {
        Pop-Location
    }

    Write-Host "Published Remote Config parameter 'localization' from: $resolvedLocalizationPath"
} finally {
    if (-not $keepTemp -and (Test-Path $tempDir)) {
        Remove-Item -Recurse -Force $tempDir
    }
}
