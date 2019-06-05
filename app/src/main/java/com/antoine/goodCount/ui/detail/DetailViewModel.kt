package com.antoine.goodCount.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.antoine.goodCount.models.CommonPot
import com.antoine.goodCount.models.LineCommonPot
import com.antoine.goodCount.repository.CommonPotRepository
import com.antoine.goodCount.repository.LineCommonPotRepository
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot

private const val TAG = "DETAIL_VIEW_MODEL"
class DetailViewModel : ViewModel() {

    private val mCommonPotRepository = CommonPotRepository()
    private val mLineCommonPotRepository = LineCommonPotRepository()
    private val mLineCommonPotList: MutableLiveData<List<LineCommonPot>> = MutableLiveData()
    private var mCommonPot: MutableLiveData<CommonPot> = MutableLiveData()

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
}