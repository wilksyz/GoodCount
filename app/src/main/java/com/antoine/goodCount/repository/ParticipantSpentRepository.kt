package com.antoine.goodCount.repository

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ParticipantSpentRepository {

    private var mFirestoreDB = Firebase.firestore

    fun getDocumentId(): String {
        return mFirestoreDB.collection("participantSpent").document().id
    }

    fun getParticipantSpent(lineCommonPotId: String): Query {
        return mFirestoreDB.collection("participantSpent").whereEqualTo("lineCommonPotId", lineCommonPotId)
    }

    fun getParticipantSpentRef(id: String): DocumentReference {
        return mFirestoreDB.collection("participantSpent").document(id)
    }
}