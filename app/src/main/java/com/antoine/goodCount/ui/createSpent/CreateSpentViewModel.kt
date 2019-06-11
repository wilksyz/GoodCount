package com.antoine.goodCount.ui.createSpent

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.antoine.goodCount.models.Participant
import com.antoine.goodCount.repository.ParticipantRepository
import java.text.DateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class CreateSpentViewModel: ViewModel() {

    private val mParticipantRepository = ParticipantRepository()
    private val mParticipantList: MutableLiveData<List<Participant>> = MutableLiveData()
    var mDateOfSpent: Date = Date()

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

    fun createListUsername(participantList: List<Participant>): ArrayList<String> {
        val usernameList = ArrayList<String>()
        for (participant in participantList){
            usernameList.add(participant.username)
        }
        return usernameList
    }

    fun createMapParticipant(participantList: List<Participant>): HashMap<String, Boolean> {
        val participantSelectedMap = HashMap<String, Boolean>()
        for (participant in participantList){
            participantSelectedMap[participant.id] = true
        }
        return participantSelectedMap
    }

    fun formatDate(): String {
        val dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT, Locale.getDefault())
        return dateFormat.format(mDateOfSpent)
    }
}