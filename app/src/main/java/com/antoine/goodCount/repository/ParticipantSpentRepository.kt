package com.antoine.goodCount.repository

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ParticipantSpentRepository {

    private var mFirestoreDB = Firebase.firestore

    fun getDocumentId(): String {
        return mFirestoreDB.collection("participantSpent").document().id
    }
}