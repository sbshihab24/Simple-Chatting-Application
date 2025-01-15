package data

object Config {

    const val PORT = 80
    const val IP = "192.168.0.105"

    private const val user1 = "{ email:\"tahsin\", pass:\"pass\", id:\"212002063\" }"
    private const val user2 = "{ email:\"shihab\", pass:\"pass\", id:\"212002110\" }"


    val users = listOf(user1, user2)
}