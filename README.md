# ComposeScreenExample
## Architecture:
Single Activity — Multi Module — Multiple Backstack — MVVM — Clean Architecture

## UI Features:
* [Shape Demo](#shape-demo)
    * [TicketPath](#ticketpath)
    * [ArcAtBottomCenterPath](#arcatbottomcenterpath)
* [Modifier Demo](#modifier-demo)
    * [Shimmering](#modifier.shimmering())
    * [BlinkingShadow](#modifier.blinkingShadow())
* [Glance Widget Demo](glance-widget-demo)

## Shape Demo
[ShapeDemo.webm](https://github.com/Velord/ComposeScreenExample/assets/33905170/c5cbd5ca-8cb3-4efb-8601-10994eb011a5)

### Shapes and layouts used:
#### 1. TicketShape 2. ArcAtBottomCenterShape 3. PervasiveArcFromBottomLayout

### Paths used:
* #### TicketPath
![246669480-b7bf28f2-5e1e-4eb9-ae53-fe3e8ecffd16](https://github.com/Velord/ComposeScreenExample/assets/33905170/64e99cdf-4c2d-4e6a-bdc4-7ab81ac4734d)

* #### ArcAtBottomCenterPath 
It is the arc placed in the bottom center of the layout dependent on the progress
![246670194-fd752fb4-8e3c-49e8-b1bb-f860138600e8](https://github.com/Velord/ComposeScreenExample/assets/33905170/75dc1cdc-b9f8-4bff-8b98-4ff675ef2617) 


## Modifier Demo
[Modifier Demo 2023.6.28.webm](https://github.com/Velord/ComposeScreenExample/assets/33905170/6cab883e-895a-4f35-93ee-c56fafd17980)


### Custom Modifiers used:
#### 1. Modifier.shimmering()
#### 2. Modifier.blinkingShadow()
#### 3. Modifier.hanging()
#### 4. Modifier.swoling()

### Modifier.shimmering()
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

### Modifier.blinkingShadow()
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

### Modifier.hanging()
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

### Modifier.hanging()
1. Default
```
.swoling()
```
![ezgif com-crop](https://github.com/Velord/ComposeScreenExample/assets/33905170/4cc02032-f7f9-442c-9c25-de3bf128242f)

2. OneAndAHalf
```
.swoling(targetScale = 1.5f)
```
![ezgif com-crop](https://github.com/Velord/ComposeScreenExample/assets/33905170/037f85aa-deef-433e-abe3-6e0b0dd8c99c)

3. HalfToOnePointTwo
```
.swoling(
    initialScale = 0.5f,
    targetScale = 1.2f,
    duration = 2000
)
```
![ezgif com-crop](https://github.com/Velord/ComposeScreenExample/assets/33905170/232cd7cb-93cd-4264-b1bd-6001ac53b2f2)

4. Bounce
```
.swoling(
    initialScale = 0.7f,
    targetScale = 1.1f,
    duration = 3000,
    easing = EaseInBounce
)
```
![ezgif com-crop](https://github.com/Velord/ComposeScreenExample/assets/33905170/adb556b2-2c0c-46de-ac09-55efa9145ee8)
