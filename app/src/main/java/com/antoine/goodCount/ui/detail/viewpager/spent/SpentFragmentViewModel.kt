package com.antoine.goodCount.ui.detail.viewpager.spent

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.antoine.goodCount.models.CommonPot
import com.antoine.goodCount.models.LineCommonPot
import com.antoine.goodCount.models.Participant
import com.antoine.goodCount.repository.CommonPotRepository
import com.antoine.goodCount.repository.LineCommonPotRepository
import com.antoine.goodCount.repository.ParticipantRepository
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

private const val USER_APP = "user app"
private const val TAG = "SPENT_FRAG_VIEW_MODEL"
class SpentFragmentViewModel : ViewModel() {

    private val mCommonPotRepository = CommonPotRepository()
    private val mLineCommonPotRepository = LineCommonPotRepository()
    private val mParticipantRepository = ParticipantRepository()
    private val mLineCommonPotList: MutableLiveData<List<LineCommonPot>> = MutableLiveData()
    private var mCommonPot: MutableLiveData<CommonPot> = MutableLiveData()
    private var mParticipantMap: MutableLiveData<HashMap<String, Participant>> = MutableLiveData()

    fun getLineCommonPot(commonPotId: String): LiveData<List<LineCommonPot>> {
        val lineCommonPotList = ArrayList<LineCommonPot>()

        mLineCommonPotRepository.getLineCommonPot(commonPotId).addSnapshotListener(EventListener<QuerySnapshot> { value, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                mLineCommonPotList.value = null
                return@EventListener
            }

            if (value != null) {
                lineCommonPotList.clear()
                for (document in value) {
                    lineCommonPotList.add(document.toObject(LineCommonPot::class.java))

                }
                mLineCommonPotList.value = lineCommonPotList
            }
        })
        return mLineCommonPotList
    }

    fun getCommonPot(commonPotId: String): MutableLiveData<CommonPot> {
        mCommonPotRepository.getCommonPot(commonPotId).addSnapshotListener(EventListener<DocumentSnapshot>{ document, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@EventListener
            }

            if (document != null && document.exists()) {
                mCommonPot.value =  document.toObject(CommonPot::class.java)
            }
        })
        return mCommonPot
    }

    fun getParticipant(userId: String, commonPotId: String): MutableLiveData<HashMap<String, Participant>> {
        val participantMap = HashMap<String, Participant>()
        mParticipantRepository.getParticipantCommonPot(commonPotId).addSnapshotListener(EventListener<QuerySnapshot>{ value, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@EventListener
            }

            if (value != null){
                participantMap.clear()
                for (document in value){
                    val participant = document.toObject(Participant::class.java)
                    participantMap[getKey(participant, userId)] = participant
                }
                mParticipantMap.value = participantMap
            }
        })
        return mParticipantMap
    }

    private fun getKey(participant: Participant, userId: String): String {
        return if (participant.userId == userId){
            USER_APP
        }else{
            participant.id
        }
    }
}