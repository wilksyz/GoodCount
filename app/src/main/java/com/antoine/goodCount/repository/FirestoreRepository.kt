package com.antoine.goodCount.repository

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirestoreRepository {

    private var mFirestoreDB = Firebase.firestore

    // get saved common pot from firebase
    fun getCommonPot(commonPotId: String): DocumentReference {
        return mFirestoreDB.collection("commonPot").document(commonPotId)
    }

    fun getParticipant(userId: String): Query {
        return mFirestoreDB.collection("participant").whereEqualTo("userId", userId)
    }
}