package com.antoine.goodCount.utils

import com.antoine.goodCount.models.LineCommonPot
import com.antoine.goodCount.models.Participant

object User {

    // Create a user list from a list of participants
    fun createUsernameList(participantList: List<Participant>): ArrayList<String> {
        val usernameList = ArrayList<String>()
        for (participant in participantList){
            usernameList.add(participant.username)
        }
        return usernameList
    }

    // Create a participant map from a list of participants
    fun createParticipantMap(participantList: List<Participant>): HashMap<String, Boolean> {
        val participantSelectedMap = HashMap<String, Boolean>()
        for (participant in participantList){
            participantSelectedMap[participant.id] = true
        }
        return participantSelectedMap
    }

    // Get who paid from a list of participants and the payer's id
    fun getPaidBy(usernameList: List<String>, participantList: List<Participant>, lineCommonPot: LineCommonPot?): Int {
        var usernamePaid = ""
        for (participant in participantList){
            if (participant.id == lineCommonPot?.paidBy){
                usernamePaid = participant.username
            }
        }
        return usernameList.indexOf(usernamePaid)
    }
}