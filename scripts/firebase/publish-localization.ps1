[CmdletBinding()]
param(
    [string]$ProjectId = "true-artwork-239920",
    [string]$LocalizationPath = "core/core-resource/src/commonMain/composeResources/files/localization.json",
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
        [regex]::Matches($Value, '%\d+\$[A-Za-z]') |
            ForEach-Object { $_.Value } |
            Sort-Object
    )
}

function Assert-LocalizationDocument {
    param([Parameter(Mandatory = $true)]$Document)

    if ($null -eq $Document.schemaVersion) {
        throw "localization.json is missing schemaVersion."
    }

    if ($null -eq $Document.languages -or $null -eq $Document.languages.en -or $null -eq $Document.languages.es) {
        throw "localization.json must contain languages.en and languages.es."
    }

    $englishKeys = @($Document.languages.en.PSObject.Properties.Name | Sort-Object)
    $spanishKeys = @($Document.languages.es.PSObject.Properties.Name | Sort-Object)

    $keyDifference = @(Compare-Object -ReferenceObject $englishKeys -DifferenceObject $spanishKeys)
    if ($keyDifference.Count -ne 0) {
        $details = ($keyDifference | ForEach-Object { "{0} ({1})" -f $_.InputObject, $_.SideIndicator }) -join ", "
        throw "English and Spanish localization keys do not match: $details"
    }

    foreach ($key in $englishKeys) {
        $englishValue = [string]$Document.languages.en.$key
        $spanishValue = [string]$Document.languages.es.$key

        $englishPlaceholders = @(Get-Placeholders $englishValue)
        $spanishPlaceholders = @(Get-Placeholders $spanishValue)
        $placeholderDifference = @(Compare-Object -ReferenceObject $englishPlaceholders -DifferenceObject $spanishPlaceholders)

        if ($placeholderDifference.Count -ne 0) {
            throw "Placeholder mismatch for localization key '$key'. EN=[$($englishPlaceholders -join ', ')] ES=[$($spanishPlaceholders -join ', ')]"
        }
    }

    Write-Host "Localization validation passed: $($englishKeys.Count) keys in EN and ES."
}

$firebase = Get-Command firebase -ErrorAction SilentlyContinue
if ($null -eq $firebase) {
    throw "Firebase CLI was not found. Install firebase-tools and run 'firebase login' once before using this script."
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

# Compact the localization document before placing it inside the Remote Config parameter value.
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

    # Preserve any existing conditional values and other parameter metadata. Only the default
    # localization document, JSON type and missing description are changed here.
    $localizationParameter | Add-Member -NotePropertyName defaultValue -NotePropertyValue ([pscustomobject]@{
        value = $compactLocalization
    }) -Force
    $localizationParameter | Add-Member -NotePropertyName valueType -NotePropertyValue "JSON" -Force

    if ($null -eq $localizationParameter.description -or [string]::IsNullOrWhiteSpace([string]$localizationParameter.description)) {
        $localizationParameter | Add-Member -NotePropertyName description -NotePropertyValue "ComposeScreenExample localization document (English + Spanish)" -Force
    }

    # Server-generated metadata is not part of the template we deploy.
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
        Write-Host ".\scripts\firebase\publish-localization.ps1 -Publish"
        return
    }

    Write-Host "Publishing only the patched Remote Config template..."
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
