package com.antoine.goodCount.models

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

class LineCommonPot(var id: String,
                    var commonPotId: String,
                    var title: String,
                    var amount: Double,
                    @ServerTimestamp var date: Date,
                    var paidBy: String) {
    constructor():this("", "","",0.0, Date(), "")
}