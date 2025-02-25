package com.example.pingotalk.Di

import com.example.pingotalk.Repo.PingoRepo
import com.example.pingotalk.Repo.PingoRepoImpl
import com.example.pingotalk.Retrofit.ApiServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object Module {
    const val BASE_URL = "https://bimol61845.pythonanywhere.com"

    @Provides
    @Singleton
    fun FirebaseAuthInstance(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun FirebaseCloudStore(): FirebaseFirestore {
        return Firebase.firestore
    }

    @Provides
    @Singleton
    fun providePingoRepo(
        firebaseAuth: FirebaseAuth,
        firebaseStore: FirebaseFirestore,
        firebaseMessaging: FirebaseMessaging,
        pingoServices: ApiServices
    ): PingoRepoImpl {
        return PingoRepoImpl(pingoServices,firebaseAuth, firebaseStore, firebaseMessaging)
    }

    @Provides
    @Singleton
    fun FirebaseFcm(): FirebaseMessaging {
        return FirebaseMessaging.getInstance()
    }

    @Provides
    @Singleton
    fun RetrofitInstance(): ApiServices {
        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)  // Replace with your API base URL
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ApiServices::class.java)
        return api
    }

}