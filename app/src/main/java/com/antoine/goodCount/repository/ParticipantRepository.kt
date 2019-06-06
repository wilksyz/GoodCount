package com.antoine.goodCount.repository

import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ParticipantRepository {

    private var mFirestoreDB = Firebase.firestore

    fun createParticipant(): String {
        val newDocRef = mFirestoreDB.collection("participant").document()
        return newDocRef.id
    }

    fun getManyParticipant(userId: String): Query {
        return mFirestoreDB.collection("participant").whereEqualTo("userId", userId)
    }

    fun getParticipant(userId: String, commonPotId: String): Query {
        return mFirestoreDB.collection("participant").whereEqualTo("commonPotId", commonPotId).whereEqualTo("userId", userId)
    }
}