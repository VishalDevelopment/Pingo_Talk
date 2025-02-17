package com.example.pingotalk.Di

import com.example.pingotalk.Repo.PingoRepo
import com.example.pingotalk.Repo.PingoRepoImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object Module {
    @Provides
    fun FirebaseAuthInstance():FirebaseAuth{
        return FirebaseAuth.getInstance()
    }
    @Provides
    fun FirebaseCloudStore():FirebaseFirestore{
        return  Firebase.firestore
    }

}