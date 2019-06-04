package com.antoine.goodCount.repository

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ParticipantRepository {

    private var mFirestoreDB = Firebase.firestore

    fun createParticipant(): String {
        val newDocRef = mFirestoreDB.collection("participant").document()
        return newDocRef.id
    }
}