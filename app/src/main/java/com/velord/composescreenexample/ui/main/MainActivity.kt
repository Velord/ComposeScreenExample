package com.velord.composescreenexample.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.velord.composescreenexample.ui.compose.preview.PreviewCombined
import com.velord.composescreenexample.ui.compose.theme.MainTheme
import com.velord.model.profile.UserProfile
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainTheme {
                MainScreen(viewModel)
            }
        }
    }
}

@Composable
private fun MainScreen(viewModel: MainViewModel) {
    val userState = viewModel.userFlow.collectAsStateWithLifecycle()

    userState.value?.let {
        Content(it)
    }
}

@Composable
private fun Content(user: UserProfile) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        GreetingUser(user)
    }
}

@Composable
private fun GreetingUser(data: UserProfile) {
    Text(text = "Hello ${data.name}!")
}

@PreviewCombined
@Composable
private fun MainPreview() {
    Content(userProfileExample)
}

private val userProfileExample = UserProfile(1, "John Doe")