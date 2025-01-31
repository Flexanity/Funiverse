package com.example.funiverse

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.funiverse.models.FuniverseData
import com.example.funiverse.screens.GameplayScreen
import com.example.funiverse.screens.SoloOrMultiplayerScreen
import com.example.funiverse.screens.UserChoiceScreen
import com.example.funiverse.screens.WelcomeScreen
import com.example.funiverse.ui.theme.FuniverseTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.random.Random
import kotlin.random.nextInt

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FuniverseTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FuniverseApp()
                }
            }
        }
    }


    @Composable
    fun FuniverseApp() {
        val navController = rememberNavController()
        val systemUiController = rememberSystemUiController()

        var bollywoodMovies : List<String> = listOf()
        var hollywoodMovies : List<String> = listOf()

        val retrofitBuilder = Retrofit.Builder()
            .baseUrl("https://api.jsonbin.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)

        val retrofitBollywoodData = retrofitBuilder.getBollywoodMovies()
        retrofitBollywoodData.enqueue(object : Callback<FuniverseData?> {
            override fun onResponse(call: Call<FuniverseData?>, response: Response<FuniverseData?>) {
                bollywoodMovies = response.body()?.bollywood!!.movies
            }
            override fun onFailure(call: Call<FuniverseData?>, t: Throwable) {
                Log.d("Testing", "Unable to Fetch Bollywood")
            }
        })

        val retrofitHollywoodData = retrofitBuilder.getHollywoodMovies()
        retrofitHollywoodData.enqueue(object : Callback<FuniverseData?> {
            override fun onResponse(call: Call<FuniverseData?>, response: Response<FuniverseData?>) {
                hollywoodMovies = response.body()?.hollywood!!.movies
            }
            override fun onFailure(call: Call<FuniverseData?>, t: Throwable) {
                Log.d("Testing", "Unable to Fetch Hollywood")
            }
        })


        SideEffect {
            systemUiController.setStatusBarColor(color = Color(0xFF1A1221))
        }
        NavHost(navController = navController, startDestination = "welcome") {
            composable("welcome") { WelcomeScreen(navController) }
            composable("solo_or_multiplayer"){ SoloOrMultiplayerScreen(navController) }
            composable(
                route = "user_choice/{message}",
                arguments = listOf(navArgument("message"){type = NavType.StringType})) {backStackEntry->
                val message = backStackEntry.arguments?.getString("message")
                UserChoiceScreen(navController, message!!)
            }

            composable(
                route ="gameplay/{theme}",
            ) { backStackEntry ->
                val theme = backStackEntry.arguments?.getString("theme")
                if(theme=="Bollywood"){
                    GameplayScreen(bollywoodMovies, onNavigateBack = {
                        navController.popBackStack() // This will pop the current screen and navigate back
                    })
                }else{
                    GameplayScreen(hollywoodMovies, onNavigateBack = {
                        navController.popBackStack() // This will pop the current screen and navigate back
                    })
                }
            }
        }
    }

}