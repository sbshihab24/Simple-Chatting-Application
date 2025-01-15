package ui.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.useResource
import androidx.compose.ui.unit.dp
import data.Screens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ui.views.loadImageFromNetwork
import org.jetbrains.skia.Image

@Composable
fun SplashUI(onNavigate: (Screens) -> Unit) {


    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.align(Alignment.Center)) {
            useResource("img.png") { stream ->
                Image.makeFromEncoded(stream.readBytes()).asImageBitmap()
            }.let {
                Image(
                    bitmap = it,
                    contentDescription = "Loaded Image",
                    modifier = Modifier.size(350.dp, 350.dp)
                        .padding(bottom = 200.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }

        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 30.dp), // Padding for bottom
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }
    }

    CoroutineScope(Dispatchers.Default).launch {
        delay(3000)
        onNavigate.invoke(Screens.LOGIN)
    }
}