package com.example.pingotalk.Di

import com.example.pingotalk.Repo.PingoRepo
import com.example.pingotalk.Repo.PingoRepoImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object Module {
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
        firebaseMessaging: FirebaseMessaging
    ): PingoRepoImpl {
        return PingoRepoImpl(firebaseAuth, firebaseStore,firebaseMessaging)
    }

    @Provides
    @Singleton
    fun FirebaseFcm(): FirebaseMessaging {
        return FirebaseMessaging.getInstance()
    }
}