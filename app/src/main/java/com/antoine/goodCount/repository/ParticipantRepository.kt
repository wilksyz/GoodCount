package com.antoine.goodCount.repository

import com.antoine.goodCount.models.Participant
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
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
        return mFirestoreDB.collection("participant").whereEqualTo("userId", userId).whereEqualTo("isVisible", true)
    }

    fun getParticipant(userId: String, commonPotId: String): Query {
        return mFirestoreDB.collection("participant").whereEqualTo("commonPotId", commonPotId).whereEqualTo("userId", userId)
    }

    fun getParticipantCommonPot(commonPotId: String): Query {
        return mFirestoreDB.collection("participant").whereEqualTo("commonPotId", commonPotId)
    }

    fun getParticipantRef(id: String): DocumentReference {
        return mFirestoreDB.collection("participant").document(id)
    }

    fun takeOffGoodCount(participant: Participant?): Task<Void> {
        val batch = mFirestoreDB.batch()
        val docRef = participant?.id?.let { mFirestoreDB.collection("participant").document(it) }
        docRef?.let { batch.update(it,"isVisible", false) }
        return batch.commit()
    }
}