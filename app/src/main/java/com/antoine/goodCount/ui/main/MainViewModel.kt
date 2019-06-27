package com.antoine.goodCount.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.antoine.goodCount.models.CommonPot
import com.antoine.goodCount.models.Participant
import com.antoine.goodCount.repository.CommonPotRepository
import com.antoine.goodCount.repository.ParticipantRepository
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot

private const val TAG = "MAIN_VIEW_MODEL"
class MainViewModel: ViewModel() {

    private val mCommonPotRepository = CommonPotRepository()
    private val mParticipantRepository = ParticipantRepository()
    private val mCommonPotList: MutableLiveData<List<CommonPot>> = MutableLiveData()

    fun getParticipantCommonPot(userId: String): LiveData<List<CommonPot>> {
        val commonPotIdList = ArrayList<String>()

        mParticipantRepository.getManyParticipant(userId).addSnapshotListener(EventListener<QuerySnapshot> { value, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                mCommonPotList.value = null
                return@EventListener
            }

            if (value != null) {
                commonPotIdList.clear()
                for (document in value) {
                    commonPotIdList.add(document.data["commonPotId"].toString())
                }
                if (commonPotIdList.isNotEmpty()){
                    getCommonPot(commonPotIdList)
                }
            }
        })
        return mCommonPotList
    }

    private fun getCommonPot(commonPotIdList: ArrayList<String>) {
        val commonPotList : MutableList<CommonPot> = mutableListOf()

        for (id in commonPotIdList){
            mCommonPotRepository.getCommonPot(id).addSnapshotListener { document, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    mCommonPotList.value = null
                }
                if (document != null){
                    val commonPot = document.toObject(CommonPot::class.java)
                    if (commonPotList.any { commonPotFilter -> commonPotFilter.id == commonPot?.id }){
                        val index = commonPotList.withIndex().filter { it.value.id == commonPot?.id }.map { it.index }.single()
                        commonPotList.removeAt(index)
                        commonPot?.let { commonPotList.add(index, it)}
                    }else {
                        commonPot?.let { commonPotList.add(it) }
                    }
                    mCommonPotList.value = commonPotList
                }
            }
        }
    }

    fun takeOffParticipant(commonPot: CommonPot, userId: String) {
        mParticipantRepository.getParticipant(userId, commonPot.id).get().addOnSuccessListener { value ->
            if (value != null){
                val participant = value.documents[0].toObject(Participant::class.java)
                mParticipantRepository.takeOffGoodCount(participant).addOnSuccessListener {
                    Log.w(TAG, "Remove visibility successful")
                }
            }
        }.addOnFailureListener { e ->
            Log.w(TAG, "Listen failed.", e)
        }
    }
}