package com.antoine.goodCount.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.antoine.goodCount.models.CommonPot
import com.antoine.goodCount.repository.CommonPotRepository
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot

private const val TAG = "FIRESTORE_VIEW_MODEL"
class MainViewModel: ViewModel() {

    private val mFirebaseRepository = CommonPotRepository()
    private val mCommonPotList: MutableLiveData<List<CommonPot>> = MutableLiveData()

    fun getParticipantCommonPot(userId: String): LiveData<List<CommonPot>> {
        val commonPotIdList = ArrayList<String>()

        mFirebaseRepository.getParticipant(userId).addSnapshotListener(EventListener<QuerySnapshot> { value, e ->
            if (e != null) {
                Log.e(TAG, "Listen failed.", e)
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
        commonPotList.clear()
        for (id in commonPotIdList){
            mFirebaseRepository.getCommonPot(id).addSnapshotListener { document, e ->
                if (e != null) {
                    Log.e(TAG, "Listen failed.", e)
                    mCommonPotList.value = null
                }
                if (document != null){
                    val commonPot = document.toObject(CommonPot::class.java)
                    if (commonPotList.any { commonPotFilter -> commonPotFilter.id == document.id }){
                        val index = commonPotList.withIndex().filter { it.value.id == document.id }.map { it.index }.single()
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
}