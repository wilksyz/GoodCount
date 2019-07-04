package com.antoine.goodCount

import com.antoine.goodCount.models.LineCommonPot
import com.antoine.goodCount.models.Participant
import com.antoine.goodCount.utils.User
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class UserUnitTest {

    @Test
    fun getUsernameListTest(){
        val usernameListCheck = createUsernameList()
        val usernameList = User.createUsernameList(createParticipantList())

        assertEquals(usernameListCheck, usernameList)
    }

    @Test
    fun getParticipantMapTest(){
        val participantMapCheck = createHashMap()
        val participantMap = User.createParticipantMap(createParticipantList())

        assertEquals(participantMapCheck, participantMap)
    }

    @Test
    fun getPaidByTest(){
        val lineCommonPot = LineCommonPot("5wp0S4M1ZQr0JGEuJMXz","0RQ9yx6NlCwx9NyqjDrB","Good Count",0.0, Date(),"7XGuHK8Es9cKyKcZgF4A")
        val paidBy = User.getPaidBy(createUsernameList(), createParticipantList(), lineCommonPot)

        assertEquals(1, paidBy)
    }

    private fun createParticipantList(): ArrayList<Participant> {
        val participantList = ArrayList<Participant>()
        participantList.add(Participant("6qkPpXHxweUHfs8FKThK","","","Peter Griffin",true))
        participantList.add(Participant("7XGuHK8Es9cKyKcZgF4A","","","Homer Simpson",true))
        participantList.add(Participant("G7plga6AwyQ5jnAO20wF","","","Stan Smith",true))
        participantList.add(Participant("UAi1hfcZC1AmQysQyPNF","","","Philip Fry",true))
        participantList.add(Participant("VC55YjRt46LtdYoHaeLT","","","Eric Cartman",true))
        return participantList
    }

    private fun createUsernameList(): List<String> {
        return listOf("Peter Griffin","Homer Simpson","Stan Smith","Philip Fry","Eric Cartman")
    }

    private fun createHashMap(): HashMap<String, Boolean> {
        val map = HashMap<String, Boolean>()
        map["6qkPpXHxweUHfs8FKThK"] = true
        map["7XGuHK8Es9cKyKcZgF4A"] = true
        map["G7plga6AwyQ5jnAO20wF"] = true
        map["UAi1hfcZC1AmQysQyPNF"] = true
        map["VC55YjRt46LtdYoHaeLT"] = true
        return map
    }
}
