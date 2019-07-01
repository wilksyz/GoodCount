package com.antoine.goodCount.models

class Participant(var id: String,
                  var commonPotId: String,
                  var userId: String,
                  var username: String,
                  var visible: Boolean) {
    constructor():this("", "","","", true)
}