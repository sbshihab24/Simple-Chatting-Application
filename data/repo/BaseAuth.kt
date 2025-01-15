package data.repo

import data.model.ModelAuth
import data.model.ModelMessage

interface BaseAuth {

    suspend fun readMessages(notification: Notification)

    suspend fun sendMessage(modelMessage: ModelMessage)

    suspend fun login(modelAuth: ModelAuth): Long?
}

interface Notification {
    fun getMessage(modelMessage: ModelMessage)
}