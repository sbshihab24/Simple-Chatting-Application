package data.repo

import data.Config
import data.model.MessageType
import data.model.ModelAuth
import data.model.ModelMessage
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class RepoAuth  : BaseAuth {

    companion object {
        private var socket: Socket? = null
        private var writer: PrintWriter? = null
        private var reader: BufferedReader? = null
    }

    init {
        if (socket == null) {
            socket = RepoSocket().connect()
            socket?.keepAlive
            writer = PrintWriter(socket!!.getOutputStream(), true)
            reader = BufferedReader(InputStreamReader(socket!!.getInputStream()))
        }
    }

    override suspend fun readMessages(notification: Notification) {
        var response: String?
        while (reader!!.readLine().also { response = it } != null) {
            val json = JSONObject(response)
            println(json)
            notification.getMessage(
                ModelMessage(
                    to = json.getString("to"),
                    from = json.getString("from"),
                    time = json.getLong("time").toLong(),
                    message = json.getString("message"),
                    type = MessageType.valueOf(json.getString("type")),
                    fileName = if (json.has("fileName")) json.getString("fileName") else null
                )
            )
//            println(json.toString())
        }
    }

    override suspend fun sendMessage(modelMessage: ModelMessage) {
        val json = JSONObject()
        json.put("to", modelMessage.to)
        json.put("from", modelMessage.from)
        json.put("time", modelMessage.time)
        json.put("message", modelMessage.message)
        json.put("type", modelMessage.type.name)
        json.put("fileName", modelMessage.fileName)
        writer?.println(json.toString())
    }

    override suspend fun login(modelAuth: ModelAuth): Long? {
        var id: Long? = null
        Config.users.forEach {
            val jo = JSONObject(it)
            println(jo.getString("pass") + ", " + modelAuth.password)
            println(jo.getString("pass") == modelAuth.password)
            if (jo.getString("email") == modelAuth.email
                && jo.getString("pass") == modelAuth.password) {
                println(jo.getString("email"))
                id = jo.getString("id").toLong()
                return jo.getString("id").toLong()
            }
        }
        id?.let { writer!!.println("{ from:\"${it}\" }") }
        return null
    }
}