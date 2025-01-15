package data.model

data class ModelMessage(
    val to: String,
    val from: String,
    val time: Long,
    val message: String,
    val type: MessageType = MessageType.TEXT,
    val fileName: String? = null,
)

enum class MessageType(name: String) {
    TEXT("text"), FILE("file")
}
