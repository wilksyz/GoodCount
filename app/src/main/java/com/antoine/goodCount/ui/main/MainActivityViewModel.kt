package com.antoine.goodCount.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.antoine.goodCount.models.Participant
import com.antoine.goodCount.repository.ParticipantRepository
import com.google.android.gms.tasks.Task

private const val TAG = "MAIN_VIEW_MODEL"
class MainActivityViewModel: ViewModel() {

    private val mParticipantRepository = ParticipantRepository()
    private val mParticipantIsPresent: MutableLiveData<Boolean> = MutableLiveData()

    // Check if a Participant exist with your id
    fun checkParticipantCommonPot(commonPotId: String, userId: String):LiveData<Boolean> {
        val participantList = ArrayList<Participant>()
        mParticipantRepository.getParticipant(userId, commonPotId).get().addOnSuccessListener { value ->
            if (value != null){
                for (document in value){
                    participantList.add(document.toObject(Participant::class.java))
                }
                if (participantList.isNotEmpty()){
                    if (!participantList[0].visible){
                        this.updateParticipant(participantList[0])
                    }
                    mParticipantIsPresent.value = true
                }else{
                    mParticipantIsPresent.value = false
                }
            }
        }.addOnFailureListener { e ->
            Log.w(TAG, "Listen Participant Failed", e)
        }
        return mParticipantIsPresent
    }

    // Change the visibility of common pot in your list
    private fun updateParticipant(participant: Participant) {
        mParticipantRepository.takeOnGoodCount(participant).addOnSuccessListener {
            Log.w(TAG, "Update successful")
        }
    }

    // Create Participant in database
    fun createParticipant(participant: Participant?): Task<Void>? {
        return participant?.let { mParticipantRepository.createParticipant(it) }
    }
}