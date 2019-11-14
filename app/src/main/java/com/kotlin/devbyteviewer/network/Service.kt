package com.kotlin.devbyteviewer.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

// Since we only have one service, this can all go in one file. If you add more services, split this
// to multiple files and make sure to share the retrofit object between services.

interface DevbyteService {
    @GET("devbytes")
    fun getPlaylistAsync(): Deferred<NetworkVideoContainer>
}

object DevByteNetwork {

    private val retrofit = Retrofit.Builder()
            .baseUrl("https://android-kotlin-fun-mars-server.appspot.com/")
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

    val devbyteService: DevbyteService = retrofit.create(DevbyteService::class.java)
}


