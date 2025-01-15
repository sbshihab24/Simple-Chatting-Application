package data.repo

import data.Config
import java.net.Socket

class RepoSocket {

    fun connect() : Socket {
        val socket = Socket(Config.IP, Config.PORT)
        return socket
    }
}