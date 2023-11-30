package com.example.reto

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

object FirestoreManager {
    val firestore: FirebaseFirestore by lazy {
        val instance = FirebaseFirestore.getInstance()

        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()

        instance.firestoreSettings = settings

        instance
    }
}