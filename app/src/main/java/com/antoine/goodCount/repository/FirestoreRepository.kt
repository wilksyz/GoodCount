package com.antoine.goodCount.repository

import android.util.Log
import com.antoine.goodCount.models.CommonPot
import com.antoine.goodCount.models.Participant
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

    fun createCommonPot(commonPot: CommonPot){
        val batch = mFirestoreDB.batch()
        val newDoc = mFirestoreDB.collection("commonPot").document()
        val newPart = mFirestoreDB.collection("participant").document().id
        val id = newDoc.id
        commonPot.id = id
        val participant = Participant(newPart, id, "wmD7tG6VvbgPNnmlhwriHNRZpHY2", "Antoine", 25.0)
        batch.set(mFirestoreDB.collection("commonPot").document(id), commonPot)
        batch.set(mFirestoreDB.collection("participant").document(newPart), participant)
        batch.commit().addOnSuccessListener {
            Log.e("Repository", "Success Write in database")
        }
    }
}