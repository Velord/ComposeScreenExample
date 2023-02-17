package com.velord.composescreenexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.velord.composescreenexample.ui.theme.MainTheme
import com.velord.model.profile.UserProfile

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainTheme {
                MainScreen()
            }
        }
    }
}

@Composable
private fun MainScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        GreetingUser(userProfileExample)
    }
}

@Composable
fun GreetingUser(data: UserProfile) {
    Text(text = "Hello ${data.name}!")
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    MainScreen()
}

private val userProfileExample = UserProfile(1, "John Doe")