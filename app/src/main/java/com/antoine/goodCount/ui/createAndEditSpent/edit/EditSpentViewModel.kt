package com.antoine.goodCount.ui.createAndEditSpent.edit

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.antoine.goodCount.models.LineCommonPot
import com.antoine.goodCount.models.Participant
import com.antoine.goodCount.models.ParticipantSpent
import com.antoine.goodCount.repository.LineCommonPotRepository
import com.antoine.goodCount.repository.ParticipantRepository
import com.antoine.goodCount.repository.ParticipantSpentRepository
import com.google.android.gms.tasks.Task
import java.text.DateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

private const val TAG = "EDIT_SPENT_VIEW_MODEL"
class EditSpentViewModel: ViewModel() {

    private val mParticipantRepository = ParticipantRepository()
    private val mLineCommonPotRepository = LineCommonPotRepository()
    private val mParticipantSpentRepository = ParticipantSpentRepository()
    private val mParticipantList: MutableLiveData<List<Participant>> = MutableLiveData()
    private val mLineCommonPot: MutableLiveData<LineCommonPot> = MutableLiveData()
    private val mParticipantSelectedMap: MutableLiveData<HashMap<String, Boolean>> = MutableLiveData()
    private val mParticipantSpentIdList = ArrayList<String>()
    var mDateOfSpent: Date? = null

    fun getParticipantCommonPot(commonPotId: String): MutableLiveData<List<Participant>> {
        val participantList = ArrayList<Participant>()
        mParticipantRepository.getParticipantCommonPot(commonPotId).get().addOnSuccessListener { documents ->
            if (documents != null){
                for (document in documents){
                    val participant = document.toObject(Participant::class.java)
                    participantList.add(participant)
                }
                mParticipantList.value = participantList
            }
        }
        return mParticipantList
    }

    fun createMapParticipant(participantList: List<Participant>, lineCommonPotId: String): MutableLiveData<HashMap<String, Boolean>> {
        val participantSelectedMap = HashMap<String, Boolean>()
        for (participant in participantList){
            participantSelectedMap[participant.id] = false
        }
        getParticipantSpent(participantSelectedMap, lineCommonPotId)
        return mParticipantSelectedMap
    }

    private fun getParticipantSpent(participantSelectedMap: HashMap<String, Boolean>, lineCommonPotId: String) {
        mParticipantSpentRepository.getParticipantSpent(lineCommonPotId).get().addOnSuccessListener { value ->
            mParticipantSpentIdList.clear()
            for (document in value){
                val participantSpent = document.toObject(ParticipantSpent::class.java)
                participantSelectedMap[participantSpent.participantId] = true
                mParticipantSpentIdList.add(participantSpent.id)
            }
            mParticipantSelectedMap.value = participantSelectedMap
        }.addOnFailureListener { e ->
            Log.w(TAG, "Listen failed.", e)
        }
    }

    fun getLineCommonPot(mLineCommonPotId: String): MutableLiveData<LineCommonPot> {
        mLineCommonPotRepository.getOneLineCommonPot(mLineCommonPotId).get().addOnSuccessListener {value ->
            mLineCommonPot.value = value.toObject(LineCommonPot::class.java)
        }.addOnFailureListener {e ->
            Log.w(TAG, "Listen failed.", e)
        }
        return mLineCommonPot
    }

    fun formatDate(): String {
        val dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT, Locale.getDefault())
        return dateFormat.format(mDateOfSpent)
    }

    fun editSpentInDatabase(lineCommonPot: LineCommonPot, participantSpentList: List<ParticipantSpent>): Task<Void> {
        return mLineCommonPotRepository.editLineCommonPot(lineCommonPot, participantSpentList, mParticipantSpentIdList)
    }
}