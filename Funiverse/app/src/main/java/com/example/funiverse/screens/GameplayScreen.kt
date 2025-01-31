package com.example.funiverse.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay


fun getRandomLetter(): Char {
    val alphabet = ('A'..'Z').toList()
    return alphabet.random()
}

@Composable
fun GameplayScreen(initialMovies: List<String>, onNavigateBack: () -> Unit) {
    val movies = remember { mutableStateListOf(*initialMovies.toTypedArray()) }

    var givenLetter by remember { mutableStateOf(getRandomLetter()) }
    var movieGuess by remember { mutableStateOf(TextFieldValue("")) }
    var feedback by remember { mutableStateOf("") }
    var player1Score by remember { mutableIntStateOf(0) }
    var player2Score by remember { mutableIntStateOf(0) }
    var currentPlayer by remember { mutableStateOf("Player 1") }
    var showPopup by remember { mutableStateOf(false) }
    var gameEnded by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    // Show popup for a short duration when currentPlayer changes
    if (showPopup && !gameEnded) {
        LaunchedEffect(currentPlayer) {
            showPopup = true
            // Hide the popup after 1 second
            delay(1000)
            showPopup = false
        }
    }

    // Show the winner in the popup when the game ends
    if (gameEnded) {
        LaunchedEffect(gameEnded) {
            showPopup = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1221))
    ) {
        // The main content (score, input fields, etc.)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Player score and turn information
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "Player 1: $player1Score", color = Color.White, fontSize = 18.sp)
                    Text(text = "Player 2: $player2Score", color = Color.White, fontSize = 18.sp)
                }
                    Text(text = "Turn: $currentPlayer", color = Color.White, fontSize = 18.sp)
            }

            Text(
                text = "Guess the Movie Name!",
                fontSize = 24.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Movie name should start with:",
                fontSize = 18.sp,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Text(
                text = "$givenLetter",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(20.dp))

            BasicTextField(
                value = movieGuess,
                onValueChange = { movieGuess = it },
                singleLine = true,
                textStyle = TextStyle(
                    fontSize = 20.sp,
                    color = Color.White
                ),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .border(1.dp, Color.White, shape = MaterialTheme.shapes.small)
                            .padding(8.dp)
                    ) {
                        if (movieGuess.text.isEmpty()) {
                            Text("Type your guess here...", color = Color.Gray)
                        }
                        innerTextField()
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3C016E),
                    contentColor = Color.White
                ),
                onClick = {
                    val guessedMovie = movieGuess.text.trim()

                    if (guessedMovie.startsWith(givenLetter, ignoreCase = true) &&
                        movies.any { it.equals(guessedMovie, ignoreCase = true) }
                    ) {
                        // Remove the guessed movie from the list
                        movies.removeIf { it.equals(guessedMovie, ignoreCase = true) }
                        // Update the letter to the last letter of the guessed movie
                        givenLetter = guessedMovie.last().uppercaseChar()
                        feedback = "Correct!"
                        if (currentPlayer == "Player 1") {
                            player1Score++
                            currentPlayer = "Player 2"
                        } else {
                            player2Score++
                            currentPlayer = "Player 1"
                        }
                        showPopup = true // Show the popup after player changes
                    } else {
                        feedback =
                            "Incorrect! or Already Guessed,\nTry a different movie starting with $givenLetter."
                        currentPlayer = if (currentPlayer == "Player 1") {
                            "Player 2"
                        } else {
                            "Player 1"
                        }
                        showPopup = true // Show the popup after player changes
                    }

                    movieGuess = TextFieldValue("")
                    focusManager.clearFocus()
                }
            ) {
                Text(text = "Submit Guess")
            }

            Spacer(modifier = Modifier.height(6.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3C016E),
                    contentColor = Color.White
                ),
                onClick = {
                    givenLetter = getRandomLetter()
                    if (currentPlayer == "Player 1") {
                        player1Score--
                    } else {
                        player2Score--
                    }
                    focusManager.clearFocus()
                }
            ) {
                Text(text = "Skip Letter")
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Add End Game Button
            Button(
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3C016E),
                    contentColor = Color.White
                ),
                onClick = {
                    gameEnded = true // End the game
                    showPopup = true // Show the winner in the popup
                    focusManager.clearFocus()
                }
            ) {
                Text(text = "End Game")
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = feedback,
                fontSize = 16.sp,
                color = if (feedback.startsWith("Correct")) Color.Green else Color.Red,
                textAlign = TextAlign.Center
            )
        }

        // Fullscreen Popup that appears on top of the content
        AnimatedVisibility(
            modifier = Modifier.fillMaxSize(),
            visible = showPopup,
            enter = scaleIn() + fadeIn(),
            exit = scaleOut() + fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.7f), shape = MaterialTheme.shapes.medium)
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                if (gameEnded) {
                    // Show winner when the game ends
                    val winner = when {
                        player1Score > player2Score -> "Player 1 wins!"
                        player2Score > player1Score -> "Player 2 wins!"
                        else -> "It's a Tie"
                    }
                    Text(
                        text = "Game Over!\n\n$winner",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                } else {
                    Text(
                        text = "$currentPlayer's turn!",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }

    BackHandler {
        onNavigateBack()
    }
}

