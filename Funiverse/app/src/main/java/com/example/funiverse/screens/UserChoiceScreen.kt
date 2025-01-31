package com.example.funiverse.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.funiverse.GameData
import com.example.funiverse.GameModel
import com.example.funiverse.GameStatus
import com.example.funiverse.R
import com.example.funiverse.ui.theme.FuniverseTheme
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun UserChoiceScreen(navController: NavHostController, message : String?) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF1A1221))
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Choose Your Theme!",
            color = Color.White,
            fontSize = 24.sp,
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 12.dp)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        ThemeCard(
            imageResource = R.drawable.hollywood,
            themeName = "Hollywood",
            onClick = {
                Firebase.firestore.collection("games")
                    .document(message!!)
                    .get()
                    .addOnSuccessListener {
                        val model = it?.toObject(GameModel::class.java)
                        if (model == null) {
                            Log.d("Join Test", "error")
                        } else {
                            model.theme = "Hollywood"
                            GameData.saveGameModel(model)
                        }
                    }
                navController.navigate("gameplay/Hollywood")
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        ThemeCard(
            imageResource = R.drawable.bollywood,
            themeName = "Bollywood",
            onClick = {
                Firebase.firestore.collection("games")
                    .document(message!!)
                    .get()
                    .addOnSuccessListener {
                        val model = it?.toObject(GameModel::class.java)
                        if (model == null) {
                            Log.d("Join Test", "error")
                        } else {
                            model.theme = "Bollywood"
                            GameData.saveGameModel(model)
                        }
                    }
                navController.navigate("gameplay/Bollywood")
            }
        )
    }
}

@Composable
fun ThemeCard(imageResource: Int, themeName: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
        ) {
            Image(
                painter = painterResource(id = imageResource),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(201.dp),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = themeName,
            color = Color.White,
            textAlign = TextAlign.Left,
            fontSize = 20.sp,
            fontFamily = FontFamily.Default,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
