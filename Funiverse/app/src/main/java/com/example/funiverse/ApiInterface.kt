package com.example.funiverse

import com.example.funiverse.models.FuniverseData
import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {


    @GET("/v3/b/67374975e41b4d34e454d279?meta=false")
    fun getBollywoodMovies(): Call<FuniverseData>

    @GET("/v3/b/673db3a6e41b4d34e4576079?meta=false")
    fun getHollywoodMovies(): Call<FuniverseData>

}