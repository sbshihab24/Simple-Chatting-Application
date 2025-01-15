package ui.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import data.model.MessageType
import data.model.ModelMessage
import data.repo.Notification
import data.repo.RepoAuth
import di.App
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ui.views.ChatBubbles
import java.awt.FileDialog
import java.nio.file.Files
import java.awt.Frame
import java.io.File
import java.util.Base64

@Composable
fun ChatUI() {

    fun pickFileAndConvertToBase64() : Pair<String, String>? {
        val fileDialog = FileDialog(Frame(), "Select File to Open", FileDialog.LOAD)
        fileDialog.isVisible = true
        if (fileDialog.file != null) {
            val file = File(fileDialog.directory, fileDialog.file)
            val fileContent = Files.readAllBytes(file.toPath())
            return Pair(fileDialog.files.first().name, Base64.getEncoder().encodeToString(fileContent))
        }
        return null
    }

    MaterialTheme {
        var me = true
        val listState = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()
        var messages by remember { mutableStateOf(listOf<ModelMessage>()) }
        var textState by remember { mutableStateOf(TextFieldValue("")) }

        CoroutineScope(Dispatchers.IO).launch {
            RepoAuth().sendMessage(
                ModelMessage(
                    from = App.id.toString(),
                    to = "-",
                    time = 0,
                    message = "",
                )
            )
        }

        Scaffold(
            bottomBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        pickFileAndConvertToBase64()?.let {
                            val model = ModelMessage(
                                from = App.id.toString(), //if (me) "abcd" else "efgh",
                                to = App.sendingID.toString(),
                                time = System.currentTimeMillis(),
                                message = it.second,
                                type = MessageType.FILE,
                                fileName = it.first
                            )
                            messages = messages + model
                            CoroutineScope(Dispatchers.IO).launch {
                                RepoAuth().sendMessage(model)
                            }
                        }
                    }) {
                        Icon(imageVector = Icons.Filled.Add, contentDescription = "Add File")
                    }
                    TextField(
                        value = textState,
                        onValueChange = { textState = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        placeholder = { Text("Enter text here") },
                        trailingIcon = {
                            IconButton(
                                onClick = {
//                                    me = !me
                                    val model = ModelMessage(
                                        from = App.id.toString(), //if (me) "abcd" else "efgh",
                                        to = App.sendingID.toString(),
                                        time = System.currentTimeMillis(),
                                        message = textState.text,
                                    )
                                    messages = messages + model
                                    CoroutineScope(Dispatchers.IO).launch {
                                        RepoAuth().sendMessage(model)
                                    }
                                    textState = TextFieldValue("")
                                }
                            ) {
                                Icon(Icons.Rounded.Send, contentDescription = "Send")
                            }
                        }
                    )
                }
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = innerPadding,
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    items(messages) { item ->
                        ChatBubbles(item)
                    }
                }

                CoroutineScope(Dispatchers.IO).launch {
                    RepoAuth().readMessages(object : Notification {
                        override fun getMessage(modelMessage: ModelMessage) {
                            messages = messages + modelMessage
                            coroutineScope.launch {
                                delay(500)
                                listState.animateScrollToItem(messages.size - 1)
                            }
//                            println( "Notification: " + modelMessage.toString())
                        }
                    })
                }
            }
        }
    }
}