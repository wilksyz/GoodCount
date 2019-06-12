package com.antoine.goodCount.repository

import com.antoine.goodCount.models.CommonPot
import com.antoine.goodCount.models.Participant
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CommonPotRepository {

    private var mFirestoreDB = Firebase.firestore

    // get saved common pot from firebase
    fun getCommonPot(commonPotId: String): DocumentReference {
        return mFirestoreDB.collection("commonPot").document(commonPotId)
    }

    fun createCommonPot(commonPot: CommonPot, participant: Participant): Task<Void> {
        val batch = mFirestoreDB.batch()
        val newDocRef = mFirestoreDB.collection("commonPot").document()
        commonPot.id = newDocRef.id
        participant.commonPotId = newDocRef.id
        participant.id = ParticipantRepository().createParticipant()
        batch.set(mFirestoreDB.collection("commonPot").document(newDocRef.id), commonPot)
        batch.set(mFirestoreDB.collection("participant").document(participant.id), participant)
        return batch.commit()
    }

    fun updateCommonPot(mCommonPot: CommonPot, mParticipant: Participant): Task<Void> {
        val batch = mFirestoreDB.batch()
        val commonPotRef = mFirestoreDB.collection("commonPot").document(mCommonPot.id)
        val participantRef = ParticipantRepository().getParticipantRef(mParticipant.id)
        batch.set(commonPotRef, mCommonPot)
        batch.set(participantRef, mParticipant)
        return batch.commit()
    }
}