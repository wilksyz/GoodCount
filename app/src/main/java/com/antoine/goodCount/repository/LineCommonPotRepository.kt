package com.antoine.goodCount.repository

import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LineCommonPotRepository {

    private var mFirestoreDB = Firebase.firestore

    // get saved line common pot from firebase
    fun getLineCommonPot(commonPotId: String): Query {
        return mFirestoreDB.collection("lineCommonPot").whereEqualTo("commonPotId", commonPotId)
    }
}