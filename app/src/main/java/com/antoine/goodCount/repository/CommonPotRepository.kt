package com.antoine.goodCount.repository

import android.net.Uri
import com.antoine.goodCount.models.CommonPot
import com.antoine.goodCount.models.Participant
import com.google.android.gms.tasks.Task
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
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
        commonPot.shareLink = createLink(commonPot.id)
        participant.commonPotId = newDocRef.id
        participant.id = ParticipantRepository().createParticipantId()
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

    private fun createLink(id: String): String {
        val dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
            .setLink(Uri.parse("https://goodcount.page.link/?GoodCount=$id"))
            .setDomainUriPrefix("https://goodcount.page.link")
            // Open links with this app on Android
            .setAndroidParameters(DynamicLink.AndroidParameters.Builder().build())
            .buildDynamicLink()
        return dynamicLink.uri.toString()
    }

    fun deleteCommonPot(hashMapId: HashMap<String, MutableList<String>>, commonPotId: String): Task<Void> {
        val batch = mFirestoreDB.batch()
        val commonPotRef = mFirestoreDB.collection("commonPot").document(commonPotId)
        batch.delete(commonPotRef)
        val participantIdList: MutableList<String>? = hashMapId["participant"]
        if (participantIdList != null) {
            for (participantId in participantIdList){
                val participantRef = ParticipantRepository().getParticipantRef(participantId)
                batch.delete(participantRef)
            }
        }
        val lineCommonPotIdList = hashMapId["lineCommonPot"]
        if (lineCommonPotIdList != null) {
            for (lineCommonPotId in lineCommonPotIdList){
                val lineCommonPotRef = LineCommonPotRepository().getLineCommonPotRef(lineCommonPotId)
                batch.delete(lineCommonPotRef)
            }
        }
        val participantSpentIdList = hashMapId["participantSpent"]
        if (participantSpentIdList != null) {
            for (participantSpentId in participantSpentIdList){
                val participantSpentRef = ParticipantSpentRepository().getParticipantSpentRef(participantSpentId)
                batch.delete(participantSpentRef)
            }
        }
        return batch.commit()
    }
}