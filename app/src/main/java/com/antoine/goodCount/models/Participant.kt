package com.antoine.goodCount.models

class Participant(var id: String?,
                  var commonPotId: String,
                  var userId: String,
                  var username: String,
                  var yourCost: Double) {
    constructor():this("", "","","",0.0)
}