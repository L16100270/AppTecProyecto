package com.example.pruebacamerax

import retrofit2.Call
import retrofit2.http.GET

interface TCRegreso {
    @GET("tipocambio/")
    fun getTCReal(): Call<String>
}