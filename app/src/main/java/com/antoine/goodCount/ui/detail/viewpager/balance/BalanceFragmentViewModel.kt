package com.antoine.goodCount.ui.detail.viewpager.balance

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.antoine.goodCount.models.LineCommonPot
import com.antoine.goodCount.models.Participant
import com.antoine.goodCount.models.ParticipantSpent
import com.antoine.goodCount.repository.LineCommonPotRepository
import com.antoine.goodCount.repository.ParticipantRepository
import com.antoine.goodCount.repository.ParticipantSpentRepository
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot

private const val TAG = "BALANCE_FRAG_VIEW_MODEL"
class BalanceFragmentViewModel: ViewModel() {

    private val mLineCommonPotRepository = LineCommonPotRepository()
    private val mParticipantRepository = ParticipantRepository()
    private val mParticipantSpentRepository = ParticipantSpentRepository()
    private val mParticipantSpentMapMutable: MutableLiveData<HashMap<String, Double>> = MutableLiveData()
    private val mLineCommonPotList = ArrayList<LineCommonPot>()
    private val mParticipantSpentMap: HashMap<String, Double> = HashMap()
    val mParticipantList = ArrayList<Participant>()

    private fun getLineCommonPot(commonPotId: String) {
        mLineCommonPotRepository.getLineCommonPot(commonPotId).addSnapshotListener(EventListener<QuerySnapshot> { value, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@EventListener
            }
            if (value != null) {
                mLineCommonPotList.clear()
                for (document in value) {
                    val lineCommonPot = document.toObject(LineCommonPot::class.java)
                    mLineCommonPotList.add(lineCommonPot)
                    this.getParticipantSpent(lineCommonPot)
                }
            }
        })
    }

    private fun getParticipantSpent(lineCommonPot: LineCommonPot) {
        val amountPay = mParticipantSpentMap[lineCommonPot.paidBy]?.plus(lineCommonPot.amount)
        if (amountPay != null) {
            mParticipantSpentMap[lineCommonPot.paidBy] = amountPay
        }
        mParticipantSpentRepository.getParticipantSpent(lineCommonPot.id).get().addOnSuccessListener {value ->
            if (value != null){
                val size = value.documents.size
                val amountPerContributor = -Math.abs(lineCommonPot.amount.div(size))
                for (document in value){
                    val participantSpent = document.toObject(ParticipantSpent::class.java)
                    val  amountUse = mParticipantSpentMap[participantSpent.participantId]?.plus(amountPerContributor)
                    if (amountUse != null){
                        mParticipantSpentMap[participantSpent.participantId] = amountUse
                    }
                }
                mParticipantSpentMapMutable.value = mParticipantSpentMap
            }
        }.addOnFailureListener { e ->
            Log.w(TAG, "Listen failed.", e)
        }
    }

    fun getParticipant(commonPotId: String): MutableLiveData<HashMap<String, Double>> {
        mParticipantRepository.getParticipantCommonPot(commonPotId).addSnapshotListener(EventListener<QuerySnapshot> { value, e ->
            if (e != null){
                Log.w(TAG, "Listen failed.", e)
                mParticipantSpentMapMutable.value = null
                return@EventListener
            }
            if (value != null){
                mParticipantSpentMap.clear()
                mParticipantList.clear()
                for (document in value){
                    val participant = document.toObject(Participant::class.java)
                    mParticipantList.add(participant)
                    mParticipantSpentMap[participant.id] = 0.0
                }
                mParticipantSpentMapMutable.value = mParticipantSpentMap
                getLineCommonPot(commonPotId)
            }
        })
        return mParticipantSpentMapMutable
    }
}