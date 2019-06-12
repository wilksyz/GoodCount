package com.antoine.goodCount.ui.createAndEdit.edit

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.antoine.goodCount.models.CommonPot
import com.antoine.goodCount.models.Participant
import com.antoine.goodCount.repository.CommonPotRepository
import com.antoine.goodCount.repository.ParticipantRepository
import com.google.android.gms.tasks.Task

private const val TAG = "EDIT_VIEW_MODEL"
class EditViewModel: ViewModel() {

    private val mCommonPotRepository = CommonPotRepository()
    private val mParticipantRepository = ParticipantRepository()
    private val mCommonPot: MutableLiveData<CommonPot> = MutableLiveData()
    private var mParticipant: MutableLiveData<Participant> = MutableLiveData()

    fun getCommonPot(mCommonPotId: String): MutableLiveData<CommonPot> {
        mCommonPotRepository.getCommonPot(mCommonPotId).get().addOnSuccessListener { documentSnapshot ->
            val commonPot = documentSnapshot.toObject(CommonPot::class.java)
            mCommonPot.value = commonPot
        }.addOnFailureListener { e ->
            Log.w(TAG, "Listen failed ", e)
        }
        return mCommonPot
    }

    fun getUsername(userId: String, mCommonPotId: String): MutableLiveData<Participant> {
        mParticipantRepository.getParticipant(userId, mCommonPotId).get().addOnSuccessListener { value ->
            if (value != null){
                mParticipant.value = value.documents[0].toObject(Participant::class.java)
            }
        }.addOnFailureListener { e ->
            Log.w(TAG, "Listen failed ", e)
        }
        return mParticipant
    }

    fun updateCommonPot(mCommonPot: CommonPot, mParticipant: Participant): Task<Void> {
        return mCommonPotRepository.updateCommonPot(mCommonPot, mParticipant)
    }
}