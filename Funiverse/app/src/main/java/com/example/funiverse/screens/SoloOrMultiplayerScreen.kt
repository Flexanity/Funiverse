package com.example.funiverse.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.funiverse.GameData
import com.example.funiverse.GameModel
import com.example.funiverse.GameStatus
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlin.random.Random
import kotlin.random.nextInt


@Composable
fun SoloOrMultiplayerScreen(navController: NavController) {
    var isVisibleCreateMenu by remember { mutableStateOf(false) }
    var isVisibleJoinMenu by remember { mutableStateOf(false) }
    var context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize()
            .background(Color(0xFF1A1221)),
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF3C016E),
                contentColor = Color.White
            ),
            onClick = {
                createOfflineGame(navController)
            }
        ) {
            Text(text = "Offline", color = Color.White)
        }
        Button(
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF3C016E),
                contentColor = Color.White
            ),
            onClick = {
//                if (!isVisibleJoinMenu) {
//                    isVisibleCreateMenu = true
//                }
                Toast.makeText(context, "COMING SOON", Toast.LENGTH_SHORT).show()
            }
        ) {
            Text(text = "Create a Room", color = Color.White)
        }
        Button(
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF3C016E),
                contentColor = Color.White
            ),
            onClick = {
//                if (!isVisibleCreateMenu) {
//                    isVisibleJoinMenu = true
//                }
                Toast.makeText(context, "COMING SOON", Toast.LENGTH_SHORT).show()
            }
        ) {
            Text(
                text = "Join a Room",
                color = Color.White
            )
        }

        if (isVisibleCreateMenu) {
            ExtendedCreateRoomScreen(Random.nextInt(1000..9999).toString(), navController)
        }

        if (isVisibleJoinMenu) {
            ExtendedJoinRoomScreen(navController)
        }
    }

}

fun createOfflineGame(navController: NavController) {
    GameData.saveGameModel(
        GameModel(
            gameStatus = GameStatus.JOINED,
        )
    )
    navController.navigate("user_choice/${"-1"}")
}

fun createOnlineGame(navController: NavController, message: String) {
    GameData.myID = "1"
    GameData.saveGameModel(
        GameModel(
            gameStatus = GameStatus.CREATED,
            gameId = message
        )
    )
    navController.navigate("user_choice/$message")
}

fun joinOnlineGame(navController: NavController, gameId: String) {
    if (gameId.isEmpty()) {
        return
    }
    GameData.myID = "2"
    Firebase.firestore.collection("games")
        .document(gameId)
        .get()
        .addOnSuccessListener {
            val model = it?.toObject(GameModel::class.java)
            if (model == null) {
                Log.d("Join Test", "error")
            } else {
                model.gameStatus = GameStatus.JOINED
                GameData.saveGameModel(model)
                navController.navigate("gameplay/${model.theme}")
            }
        }
}


@Composable
fun ExtendedCreateRoomScreen(gameId: String, navController: NavController) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Game Id: $gameId", color = Color.White)
        Button(
            onClick = {
                createOnlineGame(navController, gameId)
            }
        ) {
            Text(text = "Next ->")
        }
    }
}

@Composable
fun ExtendedJoinRoomScreen(navController: NavController) {
    var input by rememberSaveable {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = input,
            onValueChange = { input = it }
        )
        Button(
            onClick = {
                joinOnlineGame(navController, input)
            }
        ) {
            Text(text = "Next ->")
        }
    }
}
