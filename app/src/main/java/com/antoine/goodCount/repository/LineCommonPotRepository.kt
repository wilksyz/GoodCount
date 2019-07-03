package com.antoine.goodCount.repository

import com.antoine.goodCount.models.LineCommonPot
import com.antoine.goodCount.models.ParticipantSpent
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LineCommonPotRepository {

    private var mFirestoreDB = Firebase.firestore

    // get saved line common pot from firebase
    fun getLineCommonPot(commonPotId: String): Query {
        return mFirestoreDB.collection("lineCommonPot").whereEqualTo("commonPotId", commonPotId)
    }

    fun getOneLineCommonPot(lineCommonPotId: String): DocumentReference {
        return mFirestoreDB.collection("lineCommonPot").document(lineCommonPotId)
    }

    fun createLineCommonPot(lineCommonPot: LineCommonPot, participantSpentList: List<ParticipantSpent>): Task<Void> {
        val batch = mFirestoreDB.batch()
        val newDocRef = mFirestoreDB.collection("lineCommonPot").document()
        lineCommonPot.id = newDocRef.id
        batch.set(mFirestoreDB.collection("lineCommonPot").document(newDocRef.id), lineCommonPot)
        for (participantSpent in participantSpentList){
            participantSpent.lineCommonPotId = newDocRef.id
            participantSpent.id = ParticipantSpentRepository().getDocumentId()
            batch.set(mFirestoreDB.collection("participantSpent").document(participantSpent.id), participantSpent)
        }
        return batch.commit()
    }

    fun editLineCommonPot(lineCommonPot: LineCommonPot, participantSpentList: List<ParticipantSpent>, participantSpentIdList: ArrayList<String>): Task<Void>{
        val batch = mFirestoreDB.batch()
        val docRef = mFirestoreDB.collection("lineCommonPot").document(lineCommonPot.id)
        batch.set(docRef,lineCommonPot)
        for (id in participantSpentIdList){
            val docParticipantSpentRef = mFirestoreDB.collection("participantSpent").document(id)
            batch.delete(docParticipantSpentRef)
        }
        for (participantSpent in participantSpentList){
            participantSpent.id = ParticipantSpentRepository().getDocumentId()
            batch.set(mFirestoreDB.collection("participantSpent").document(participantSpent.id), participantSpent)
        }
        return batch.commit()
    }

    fun removeLineCommonPot(participantSpentIdList: List<String>, lineCommonPotId: String): Task<Void> {
        val batch = mFirestoreDB.batch()
        val docRef = mFirestoreDB.collection("lineCommonPot").document(lineCommonPotId)
        batch.delete(docRef)
        for (participantSpentId in participantSpentIdList){
            val docParticipantSpentRef = mFirestoreDB.collection("participantSpent").document(participantSpentId)
            batch.delete(docParticipantSpentRef)
        }
        return batch.commit()
    }

    fun getLineCommonPotRef(id: String): DocumentReference {
        return mFirestoreDB.collection("lineCommonPot").document(id)
    }
}