package ui.pages

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.Screens
import data.model.ModelFriend
import di.App
import di.App.sendingID
import ui.views.FriendListItem

@Composable
fun FriendListUI(onNavigate: (Screens, String) -> Unit) {
    val friends = listOf(
        ModelFriend("212002063", "Tahsin", "https://lh3.googleusercontent.com/ogw/ANLem4ZdcifEBmoNNmaRUd8iUof7NBMUejo3PnB6tGa-kw"),
        ModelFriend("212002110", "Shihab", "https://lh3.googleusercontent.com/a-/ALV-UjVYK87WlIcTVryMgVEcn40dUJ7F-dxTnB2jMCDeAwgPFZTP=s48-p"),

    )

    val newList = ArrayList<ModelFriend>()

    friends.filter {
        it.id != App.id.toString()
    }.also {
        newList.addAll(it)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(8.dp)
    ) {
        items(newList) { friend ->
            sendingID = friend.id.toLong()
            FriendListItem(friend) {
                onNavigate.invoke(Screens.CHATTING_PAGE, it)
            }
        }
    }
}