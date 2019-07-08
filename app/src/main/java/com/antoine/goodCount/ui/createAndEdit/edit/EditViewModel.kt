package com.antoine.goodCount.ui.createAndEdit.edit

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.antoine.goodCount.models.CommonPot
import com.antoine.goodCount.models.Participant
import com.antoine.goodCount.repository.CommonPotRepository
import com.antoine.goodCount.repository.LineCommonPotRepository
import com.antoine.goodCount.repository.ParticipantRepository
import com.antoine.goodCount.repository.ParticipantSpentRepository
import com.google.android.gms.tasks.Task

private const val TAG = "EDIT_VIEW_MODEL"
class EditViewModel: ViewModel() {

    private val mCommonPotRepository = CommonPotRepository()
    private val mParticipantRepository = ParticipantRepository()
    private val mLineCommonPotRepository = LineCommonPotRepository()
    private val mParticipantSpentRepository = ParticipantSpentRepository()
    private val mCommonPot: MutableLiveData<CommonPot> = MutableLiveData()
    private var mParticipant: MutableLiveData<Participant> = MutableLiveData()
    private val mLiveDataMapId: MutableLiveData<HashMap<String, MutableList<String>>> = MutableLiveData()
    private val mMapId = HashMap<String, MutableList<String>>()
    private var mCounter: Int = 0

    // Get the CommonPot to edit
    fun getCommonPot(mCommonPotId: String): MutableLiveData<CommonPot> {
        mCommonPotRepository.getCommonPot(mCommonPotId).get().addOnSuccessListener { documentSnapshot ->
            val commonPot = documentSnapshot.toObject(CommonPot::class.java)
            mCommonPot.value = commonPot
        }.addOnFailureListener { e ->
            Log.w(TAG, "Listen failed ", e)
        }
        return mCommonPot
    }

    // Get the username of current user
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

    // Update CommonPot in database
    fun updateCommonPot(mCommonPot: CommonPot, mParticipant: Participant): Task<Void> {
        return mCommonPotRepository.updateCommonPot(mCommonPot, mParticipant)
    }

    // Get Participant id for delete
    fun getParticipantId(commonPotId: String): MutableLiveData<HashMap<String, MutableList<String>>> {
        mMapId["participant"] = ArrayList<String>()
        mMapId["lineCommonPot"] = ArrayList<String>()
        mMapId["participantSpent"] = ArrayList<String>()
        mParticipantRepository.getParticipantCommonPot(commonPotId).get().addOnSuccessListener { value ->
            if (value != null){
                for (document in value){
                    mMapId["participant"]?.add(document.id)
                }
                this.getLineCommonPOtId(commonPotId)
            }
        }.addOnFailureListener {e ->
            Log.w(TAG, "Listen failed ", e)
        }
        return mLiveDataMapId
    }

    // Get LineCommonPot id for delete
    private fun getLineCommonPOtId(commonPotId: String) {
        mLineCommonPotRepository.getLineCommonPot(commonPotId).get().addOnSuccessListener { value ->
            if (value != null){
                val numberOfLineCommonPot = value.documents.size
                mCounter = 0
                for (document in value){
                    mMapId["lineCommonPot"]?.add(document.id)
                    this.getParticipantSpentId(document.id, numberOfLineCommonPot)
                }
                if (numberOfLineCommonPot == 0 && !mMapId["participant"].isNullOrEmpty()){
                    mLiveDataMapId.value = mMapId
                }
            }
        }.addOnFailureListener {e ->
            Log.w(TAG, "Listen failed ", e)
        }
    }

    // Get ParticipantSpent id for delete
    private fun getParticipantSpentId(lineCommonPotId: String, numberOfLineCommonPot: Int) {
        mParticipantSpentRepository.getParticipantSpent(lineCommonPotId).get().addOnSuccessListener { value ->
            if (value != null){
                for (document in value){
                    mMapId["participantSpent"]?.add(document.id)
                }
                mCounter++
                if (mCounter == numberOfLineCommonPot){
                    mLiveDataMapId.value = mMapId
                }
            }
        }.addOnFailureListener {e ->
            Log.w(TAG, "Listen failed ", e)
        }
    }

    fun deleteCommonPot(hashMapId: HashMap<String, MutableList<String>>, commonPotId: String): Task<Void> {
        return mCommonPotRepository.deleteCommonPot(hashMapId, commonPotId)
    }
}