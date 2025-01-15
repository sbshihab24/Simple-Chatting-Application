package ui.pages

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import data.Screens
import data.model.ModelAuth
import data.repo.RepoAuth
import di.App
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun LoginUI(onNavigate: (Screens) -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    Icon(Icons.Default.Send, contentDescription = "Toggle Password Visibility")
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            CoroutineScope(Dispatchers.IO).launch {//kotlin io thread
                RepoAuth().login(ModelAuth(0, username, password))?.let {
                    App.id = it
                    onNavigate.invoke(Screens.FRIEND_LIST)
                } ?: run {
//                    withRunningRecomposer {
//                        Snackbar {
//                            Text("Wrong Credentials")
//                        }
//                    }
                }
            }
        }) {
            Text("Login")
        }
        Button(onClick = {
            onNavigate.invoke(Screens.REGISTRATION)
        }) {
            Text("Create new account")
        }
    }
}