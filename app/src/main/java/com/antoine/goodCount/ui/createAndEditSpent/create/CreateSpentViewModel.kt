package com.antoine.goodCount.ui.createAndEditSpent.create

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.antoine.goodCount.models.LineCommonPot
import com.antoine.goodCount.models.Participant
import com.antoine.goodCount.models.ParticipantSpent
import com.antoine.goodCount.repository.LineCommonPotRepository
import com.antoine.goodCount.repository.ParticipantRepository
import com.google.android.gms.tasks.Task
import java.text.DateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class CreateSpentViewModel: ViewModel() {

    private val mParticipantRepository = ParticipantRepository()
    private val mLineCommonPotRepository = LineCommonPotRepository()
    private val mParticipantList: MutableLiveData<List<Participant>> = MutableLiveData()
    var mDateOfSpent: Date = Date()

    // Get the Participants to the CommonPot
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

    // Get the date in the right format
    fun formatDate(): String {
        val dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT, Locale.getDefault())
        return dateFormat.format(mDateOfSpent)
    }

    fun createSpentInDatabase(lineCommonPot: LineCommonPot, participantSpentList: List<ParticipantSpent>): Task<Void> {
        return mLineCommonPotRepository.createLineCommonPot(lineCommonPot, participantSpentList)
    }
}