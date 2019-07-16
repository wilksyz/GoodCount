package com.antoine.goodCount.ui.main.fragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.antoine.goodCount.models.CommonPot
import com.antoine.goodCount.models.Participant
import com.antoine.goodCount.repository.CommonPotRepository
import com.antoine.goodCount.repository.ParticipantRepository
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot

private const val TAG = "MAIN_VIEW_MODEL"
class MainFragmentViewModel: ViewModel() {

    private val mCommonPotRepository = CommonPotRepository()
    private val mParticipantRepository = ParticipantRepository()
    private val mCommonPotList: MutableLiveData<List<CommonPot>> = MutableLiveData()
    private val mIndicator: MutableLiveData<Boolean> = MutableLiveData()
    private var mListenerList = ArrayList<ListenerRegistration>()

    // Get the common pot in which you participate
    fun getParticipantCommonPot(userId: String): LiveData<List<CommonPot>> {
        val commonPotIdList = ArrayList<String>()

        mListenerList.add(mParticipantRepository.getManyParticipant(userId).addSnapshotListener(EventListener<QuerySnapshot> { value, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed Participant.")
                    mCommonPotList.value = null
                    return@EventListener
                }

                if (value != null) {
                    commonPotIdList.clear()
                    for (document in value) {
                        commonPotIdList.add(document.data["commonPotId"].toString())
                    }
                    if (commonPotIdList.isNotEmpty()) {
                        getCommonPot(commonPotIdList)
                    }
                }
            }))
        return mCommonPotList
    }

    private fun getCommonPot(commonPotIdList: ArrayList<String>) {
        val commonPotList: MutableList<CommonPot> = mutableListOf()

        for (id in commonPotIdList) {
            mListenerList.add(mCommonPotRepository.getCommonPot(id).addSnapshotListener(EventListener<DocumentSnapshot> { document, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed CommonPot.")
                    mCommonPotList.value = null
                    return@EventListener
                }
                if (document != null) {
                    val commonPot = document.toObject(CommonPot::class.java)
                    if (commonPotList.any { commonPotFilter -> commonPotFilter.id == commonPot?.id }) {
                        val index =
                            commonPotList.withIndex().filter { it.value.id == commonPot?.id }.map { it.index }.single()
                        commonPotList.removeAt(index)
                        commonPot?.let { commonPotList.add(index, it) }
                    } else {
                        commonPot?.let { commonPotList.add(it) }
                    }
                    mCommonPotList.value = commonPotList
                }
            }))
        }
    }

    // Makes the common pot invisible in your list
    fun takeOffParticipant(commonPot: CommonPot, userId: String): MutableLiveData<Boolean> {
        mParticipantRepository.getParticipant(userId, commonPot.id).get().addOnSuccessListener { value ->
            if (value != null) {
                val participant = value.documents[0].toObject(Participant::class.java)
                mParticipantRepository.takeOffGoodCount(participant).addOnSuccessListener {
                    mIndicator.value = true
                }
            }
        }.addOnFailureListener { e ->
            Log.w(TAG, "Listen failed.", e)
            mIndicator.value = false
        }
        return mIndicator
    }

    fun removeListener(){
        for (listener in mListenerList){
            listener.remove()
        }
        mListenerList.clear()
    }
}