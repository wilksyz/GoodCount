package com.antoine.goodCount.models

class CommonPot(var id: String,
                var title: String,
                var description: String,
                var currency: String,
                var shareLink: String) {
    constructor():this("","","","", "")
}