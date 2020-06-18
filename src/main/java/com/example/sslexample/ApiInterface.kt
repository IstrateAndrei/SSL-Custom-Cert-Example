package com.example.sslexample

import io.reactivex.rxjava3.core.Observable
import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {

    @GET("v1/login")
    fun login(): Observable<Void>

    @GET("v1/logout")
    fun logout(): Observable<Void>
}