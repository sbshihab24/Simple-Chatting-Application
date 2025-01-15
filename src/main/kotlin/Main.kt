import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import data.NavigationDirection
import data.Screens
import ui.pages.*
import java.util.Stack

@OptIn(ExperimentalAnimationApi::class)
@Composable
@Preview
fun App() {
    var backstack = remember { Stack<Pair<Screens, String>>() }
    var navigationDirection by remember { mutableStateOf(NavigationDirection.FORWARD) }
    var page by remember { mutableStateOf(Pair(Screens.SPLASH, "")) }

    val navigateTo: (Screens, String) -> Unit = { screen, name ->
        backstack.push(page)
        navigationDirection = NavigationDirection.FORWARD
        page = Pair(screen, name)
    }

    val navigateBack: () -> Unit = {
        if (backstack.isNotEmpty()) {
            navigationDirection = NavigationDirection.BACKWARD
            page = backstack.pop()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(page.second) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) {
        AnimatedContent(
            targetState = page,
            transitionSpec = {
                if (navigationDirection == NavigationDirection.FORWARD) {
                    slideInHorizontally(
                        initialOffsetX = { fullWidth -> fullWidth }, // Enter from the right
                        animationSpec = tween(600)
                    ) + fadeIn(animationSpec = tween(600)) with
                            slideOutHorizontally(
                                targetOffsetX = { fullWidth -> -fullWidth }, // Exit to the left
                                animationSpec = tween(600)
                            ) + fadeOut(animationSpec = tween(600))
                } else {
                    slideInHorizontally(
                        initialOffsetX = { fullWidth -> -fullWidth }, // Enter from the left
                        animationSpec = tween(600)
                    ) + fadeIn(animationSpec = tween(600)) with
                            slideOutHorizontally(
                                targetOffsetX = { fullWidth -> fullWidth }, // Exit to the right
                                animationSpec = tween(600)
                            ) + fadeOut(animationSpec = tween(600))
                }
            }
        ) { targetScreen ->
            when (targetScreen.first) {
                Screens.SPLASH -> SplashUI { navigateTo(it, "") }
                Screens.LOGIN -> LoginUI { navigateTo(it, "Login") }
                Screens.REGISTRATION -> RegistrationUI()
                Screens.FRIEND_LIST -> FriendListUI { screen, name ->
                    navigateTo(screen, name)
                }
                Screens.CHATTING_PAGE -> ChatUI()
            }
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}