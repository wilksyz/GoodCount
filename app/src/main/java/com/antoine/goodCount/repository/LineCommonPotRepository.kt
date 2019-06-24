package com.antoine.goodCount.repository

import com.antoine.goodCount.models.LineCommonPot
import com.antoine.goodCount.models.ParticipantSpent
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LineCommonPotRepository {

    private var mFirestoreDB = Firebase.firestore

    // get saved line common pot from firebase
    fun getLineCommonPot(commonPotId: String): Query {
        return mFirestoreDB.collection("lineCommonPot").whereEqualTo("commonPotId", commonPotId)
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
}