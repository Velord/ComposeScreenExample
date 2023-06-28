# ComposeScreenExample
## Architecture:
Single Activity

Multi-module

Multiple Backstack

MVVM

Clean Architecture
## Shape Demo
[ShapeDemo.webm](https://github.com/Velord/ComposeScreenExample/assets/33905170/c5cbd5ca-8cb3-4efb-8601-10994eb011a5)

### Custom crafted paths:

#### 1. TicketPath represents that kind of shape
![Ticket](https://github.com/Velord/ComposeScreenExample/assets/33905170/b7bf28f2-5e1e-4eb9-ae53-fe3e8ecffd16)

#### 2. ArcAtBottomCenterPath is the arc placed in the bottom center of the layout dependent on the progress
![Shape path in percent](https://github.com/Velord/ComposeScreenExample/assets/33905170/fd752fb4-8e3c-49e8-b1bb-f860138600e8)

### Custom crafted shapes based on mentioned paths:
#### 1. TicketShape 2. ArcAtBottomCenterShape

### Custom crafted 'Compose' components:

#### 1. PervasiveArcFromBottomLayout 
That component brings animation that devastating layout based on ArcAtBottomCenterShape and his path ArcAtBottomCenterPath:


## Modifier Demo
[Modifier Demo 2023.6.28.webm](https://github.com/Velord/ComposeScreenExample/assets/33905170/6cab883e-895a-4f35-93ee-c56fafd17980)


### You can find custom crafted Modifiers:
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


