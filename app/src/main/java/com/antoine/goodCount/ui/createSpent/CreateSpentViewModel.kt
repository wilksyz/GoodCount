package com.antoine.goodCount.ui.createSpent

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.antoine.goodCount.models.Participant
import com.antoine.goodCount.repository.ParticipantRepository
import java.text.DateFormat
import java.util.*
import kotlin.collections.ArrayList

class CreateSpentViewModel: ViewModel() {

    private val mParticipantRepository = ParticipantRepository()
    private val mParticipantList: MutableLiveData<List<String>> = MutableLiveData()
    var mDateOfSpent: Date = Date()

    fun getParticipantCommonPot(commonPotId: String): MutableLiveData<List<String>> {
        val participantList = ArrayList<String>()
        mParticipantRepository.getParticipantCommonPot(commonPotId).get().addOnSuccessListener { documents ->
            if (documents != null){
                for (document in documents){
                    val participant = document.toObject(Participant::class.java)
                    participantList.add(participant.username)
                }
                mParticipantList.value = participantList
            }
        }
        return mParticipantList
    }

    fun formatDate(): String {
        val dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT, Locale.getDefault())
        return dateFormat.format(mDateOfSpent)
    }
}