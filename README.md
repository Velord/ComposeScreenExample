# ComposeScreenExample
## Architecture:
Single Activity — Multi Module — Multiple Backstack — MVVM(UiState + UiAction) — Clean Architecture

Project uses navigation libs:
* [Jetpack Navigation](https://developer.android.com/guide/navigation) **80%** _screens covered._ **DEPRECATED**
* [Voyager](https://github.com/adrielcafe/voyager) **100%** _screens covered_
* [Compose Navigation](https://developer.android.com/develop/ui/compose/navigation) **100%** _screens covered_
* [Compose Destinations](https://github.com/raamcosta/compose-destinations) **100%** _screens covered_

To use different Navigation library need to change in ```build.gradle.kts``` at ```app``` module.

```
buildConfigField(    
    "com.velord.navigation.NavigationLib",
    "NAVIGATION_LIB",
    "com.velord.navigation.NavigationLib.{Voyager}" or "Jetpack" or "Destinations" or "Compose"
)
```


## UI Features:
* [Movie Demo](#movie-demo)
* [Theme Demo](#theme-demo)
   * [Before 8.1 Oreo](#before-81-oreo)
   * [After 8.1 and Before 11 Red Velvet Cake](#before-11-red-velvet-cake)
   * [After 11 Red Velvet Cake](#after-11-red-velvet-cake)
* [Phone Number Hint Demo](#phone-number-hint-demo)
* [Compose Shapes](#shape-demo)
   * [TicketPath](#ticketpath)
   * [ArcAtBottomCenterPath](#arcatbottomcenterpath)
   * [WaveShape](#arcatbottomcenterpath)
* [Compose Modifiers](#modifier-demo)
   * [Shimmering](#shimmering)
   * [BlinkingShadow](#blinking-shadow)
   * [Hanging](#hanging)
   * [Swelling](#swelling)
* [Glance Widget Demo](#glance-widget-demo)
    * [Synced Theme](#synced-theme-with-app)
    * [Counter](#counter)
    * [Refreshable Image](#refreshable-image)

## Movie Demo
[MovieDemo.webm](https://github.com/user-attachments/assets/32bfe923-977a-4803-a297-014d17cc9af9)

## Theme Demo
* ### Before 8.1 Oreo
![themedemobefore8.1](https://github.com/user-attachments/assets/4bf872d7-ab15-45d8-91af-06c9caaf81ae)

* ### Before 11 Red Velvet Cake
https://github.com/user-attachments/assets/aeca3717-9f1d-49cd-9821-a12fa466923e

* ### After 11 Red Velvet Cake
https://github.com/user-attachments/assets/da8fd995-e6f6-4583-9514-6c985062de39


## Shape Demo
[ShapeDemo.webm](https://github.com/user-attachments/assets/e7ed352d-f9b7-46c4-9745-9ff2498b22c8)

### Shapes and layouts used:
#### 1. TicketShape 2. ArcAtBottomCenterShape 3. PervasiveArcFromBottomLayout

### Paths used:
* #### TicketPath
![246669480-b7bf28f2-5e1e-4eb9-ae53-fe3e8ecffd16](https://github.com/Velord/ComposeScreenExample/assets/33905170/64e99cdf-4c2d-4e6a-bdc4-7ab81ac4734d)

* #### ArcAtBottomCenterPath 
It is the arc placed in the bottom center of the layout that depends on the progress
![246670194-fd752fb4-8e3c-49e8-b1bb-f860138600e8](https://github.com/Velord/ComposeScreenExample/assets/33905170/75dc1cdc-b9f8-4bff-8b98-4ff675ef2617) 

* #### WaveShape
![Screenshot_20241125_174739](https://github.com/user-attachments/assets/664f9e5b-5ca1-4fe4-8410-d1792cf1ce47)


## Modifier Demo
[Modifier Demo 2023.6.28.webm](https://github.com/Velord/ComposeScreenExample/assets/33905170/6cab883e-895a-4f35-93ee-c56fafd17980)


### Custom Modifiers used:
#### 1. Modifier.shimmering()
#### 2. Modifier.blinkingShadow()
#### 3. Modifier.hanging()
#### 4. Modifier.swelling()

### Shimmering
1. Rainbow
```
.shimmering(
    gradientColorAndPosition = { animatedValue ->
        listOf(
            Color.Red to 0f,
            Color.Green to animatedValue * 0.1f,
            Color.Blue to animatedValue * 0.2f,
            Color.Yellow to animatedValue * 0.3f,
            Color.Magenta to animatedValue * 0.7f,
            Color.Cyan to animatedValue * 0.8f,
            Color.Gray to animatedValue * 0.9f,
            Color.Black to animatedValue,
            Color.White to 1f,
        )
    }
)
```

![ezgif com-crop](https://github.com/Velord/ComposeScreenExample/assets/33905170/b6d9a1fe-e1a7-43c5-87c6-adf13f8f11a5)


2. Default
```
.shimmering()
```
![ezgif com-crop](https://github.com/Velord/ComposeScreenExample/assets/33905170/47b356de-4c9f-48f3-9b1c-0a9197c4a8c2)

3. Magenta
```
.shimmering(
    duration = 2000,
    gradientColorAndPosition = { animatedValue ->
        listOf(
            Color.Magenta to 0f,
            Color.Cyan to animatedValue * 0.3f,
            Color.Gray to animatedValue,
            Color.Magenta to 1f,
        )
    }  
)
```
![ezgif com-crop](https://github.com/Velord/ComposeScreenExample/assets/33905170/9b1798ff-dc77-49f3-a26c-20cae02b957b)

4. SurfaceTint
```
.shimmering(
    duration = 3000,
    gradientColorAndPosition = { animatedValue ->
        listOf(
            MaterialTheme.colorScheme.surfaceTint to 0f,
            MaterialTheme.colorScheme.tertiary to animatedValue,
            MaterialTheme.colorScheme.surfaceTint to 1f,
        )
    }
)
```
![ezgif com-crop](https://github.com/Velord/ComposeScreenExample/assets/33905170/c34a2f97-1de2-4405-8f0d-9b0654d88528)

5. Reverse
```
.shimmering(
    duration = 3000,
    gradientColorAndPosition = { animatedValue ->
        listOf(
            MaterialTheme.colorScheme.tertiaryContainer to 0f,
            MaterialTheme.colorScheme.onTertiaryContainer to animatedValue,
            MaterialTheme.colorScheme.tertiaryContainer to 1f,
        )
    },
    reverse = true
)
```
![ezgif com-crop](https://github.com/Velord/ComposeScreenExample/assets/33905170/4b7edba5-e46a-4130-bd2d-8e87d06225f8)

6. ReverseRainbow
```
.shimmering(
    duration = 3000,
    gradientColorAndPosition = { animatedValue ->
        listOf(
            Color.Green to 0f,
            Color.Yellow to animatedValue * 0.1f,
            Color.DarkGray to animatedValue * 0.2f,
            Color.Magenta to animatedValue * 0.3f,
            Color.Cyan to animatedValue * 0.35f,
            Color.Transparent to animatedValue * 0.7f,
            Color.Gray to animatedValue * 0.8f,
            Color.Black to animatedValue * 0.85f,
            Color.White to animatedValue * 0.9f,
            Color.LightGray to animatedValue * 0.95f,
            Color.Red to animatedValue,
            Color.Blue to 1f
        )
    },
    reverse = true
)
```
![ezgif com-gif-maker](https://github.com/Velord/ComposeScreenExample/assets/33905170/d975f4c6-7027-4c02-8a41-08154263654e)

### Blinking Shadow
1. GreenReverse
```
.blinkingShadow(
    elevationMax = 64.dp,
    shape = CardDefaults.shape,
    duration = 3000,
    spotColor = Color.Green
)
```
![ezgif com-crop (1)](https://github.com/Velord/ComposeScreenExample/assets/33905170/15eba3f3-a0d0-482f-92a3-7887aa13cccb)

2. RedReverse
```
.blinkingShadow(
    elevationMax = 16.dp,
    shape = CardDefaults.shape,
    duration = 500,
    spotColor = Color.Red
)
```
![ezgif com-crop](https://github.com/Velord/ComposeScreenExample/assets/33905170/c5b1afd5-1c4f-44f1-8da8-997a8fe2ba99)

3. CyanRestart
```
.blinkingShadow(
    elevationMax = 32.dp,
    shape = shape,
    spotColor = Color.Cyan,
    repeatMode = RepeatMode.Restart
)
```
![ezgif com-crop](https://github.com/Velord/ComposeScreenExample/assets/33905170/01927ade-1920-4558-9afe-62a2adae8c97)

### Hanging
1. Default
```
.hanging()
```
![ezgif com-crop](https://github.com/Velord/ComposeScreenExample/assets/33905170/b0a593e4-12d1-4226-b541-a195df850a25)

2. SmallLeft
```
.hanging(
    shift = HangingDefaults.shift(
        startRotationAngle = 5,
        endRotationAngle = -20
    ),
    animation = HangingDefaults.animation(duration = 2000)
)
```
![ezgif com-crop](https://github.com/Velord/ComposeScreenExample/assets/33905170/706099c8-d3eb-4137-9551-b5d2c06861c3)

3. FullRotation
```
.hanging(
    shift = HangingDefaults.shift(startRotationAngle = 180),
    animation = HangingDefaults.animation(3000)
)
```
![ezgif com-crop](https://github.com/Velord/ComposeScreenExample/assets/33905170/869dd32d-c4f5-4229-8667-ba61b9eae371)

4. HangingOnRightSide
```
.hanging(pivotPoint = HangingPivotPoint.Right)
```
![ezgif com-crop](https://github.com/Velord/ComposeScreenExample/assets/33905170/1ffe734c-6f34-4527-bb7c-2114253bd4c8)

5. QuickHangingOnLeftSide
```
.hanging(
    shift = HangingDefaults.shift(10),
    animation = HangingDefaults.animation(300),
    pivotPoint = HangingPivotPoint.Left
)
```
![ezgif com-crop](https://github.com/Velord/ComposeScreenExample/assets/33905170/157c095e-e59b-4a52-9afa-9319855ab90e)

6. EpilepsyAtBottom
```
.hanging(
    shift = HangingDefaults.shift(
        startRotationAngle = 5,
        endRotationAngle = -10
    ),
    animation = HangingDefaults.animation(duration = 80),
    pivotPoint = HangingPivotPoint.Bottom
)
```
![ezgif com-crop](https://github.com/Velord/ComposeScreenExample/assets/33905170/dc8a22b2-6def-43dc-8e6d-53f26951d1ac)

7. HangingOnCenter
```
.hanging(
    animation = HangingDefaults.animation(duration = 4300),
    pivotPoint = HangingDefaults.pivot(
        pivotFractionX = 0.5f,
        pivotFractionY = 0.5f
    )
)
```
![ezgif com-crop](https://github.com/Velord/ComposeScreenExample/assets/33905170/42720e4d-724d-440b-9c5d-c4f5a259aa54)

### Swelling
1. Default
```
.swelling()
```
![ezgif com-crop](https://github.com/Velord/ComposeScreenExample/assets/33905170/4cc02032-f7f9-442c-9c25-de3bf128242f)

2. OneAndAHalf
```
.swelling(targetScale = 1.5f)
```
![ezgif com-crop](https://github.com/Velord/ComposeScreenExample/assets/33905170/037f85aa-deef-433e-abe3-6e0b0dd8c99c)

3. HalfToOnePointTwo
```
.swelling(
    initialScale = 0.5f,
    targetScale = 1.2f,
    duration = 2000
)
```
![ezgif com-crop](https://github.com/Velord/ComposeScreenExample/assets/33905170/232cd7cb-93cd-4264-b1bd-6001ac53b2f2)

4. Bounce
```
.swelling(
    initialScale = 0.7f,
    targetScale = 1.1f,
    duration = 3000,
    easing = EaseInBounce
)
```
![ezgif com-crop](https://github.com/Velord/ComposeScreenExample/assets/33905170/adb556b2-2c0c-46de-ac09-55efa9145ee8)



## Glance Widget Demo
[Glance Widgets](https://github.com/Velord/ComposeScreenExample/assets/33905170/0f5f81a7-f81f-4d1f-89a6-40f7cd0d0eba)

* ### Synced Theme with App
[Synced Theme](https://github.com/Velord/ComposeScreenExample/assets/33905170/d8aa6172-3a6a-427d-a03d-be7e6d4d69c4)

* ### Counter

[Counter](https://github.com/Velord/ComposeScreenExample/assets/33905170/72aa7732-4763-41eb-b778-01ba09958fd7)


* ### Refreshable Image

[Refreshable Image](https://github.com/Velord/ComposeScreenExample/assets/33905170/52762849-2d09-4445-85f1-4975d22ff52e)


## Phone Number Hint Demo
[Phone Number Hint Demo](https://github.com/user-attachments/assets/041d7f78-7083-4640-a68e-04662335d250)
