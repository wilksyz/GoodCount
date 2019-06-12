package com.antoine.goodCount.utils

import java.util.*
import java.util.Currency
import kotlin.collections.ArrayList

object Currency {

    fun getAllCurrency(): List<String> {
        val localeList = Locale.getAvailableLocales()
        val currencyList = ArrayList<String>()
        for (locale in localeList){
            try {
                val currency = Currency.getInstance(locale)
                val string = "${currency.displayName} (${currency.currencyCode})"
                if (!currencyList.contains(string)) currencyList.add(string)
            }catch (e: Exception){ }
        }
        currencyList.sort()
        currencyList.removeAt(0)
        return currencyList
    }

    fun getLocaleCurrency(currencyList: List<String>): Int {
        val currency = Currency.getInstance(Locale.getDefault())
        return currencyList.indexOf("${currency.displayName} (${currency.currencyCode})")
    }

    fun getCurrencySelected(currencyList: List<String>, currencyCode: String): Int {
        val currency = Currency.getInstance(currencyCode)
        return currencyList.indexOf("${currency.displayName} (${currency.currencyCode})")
    }

    fun getCurrencyCode(currencyName: String): String{
        val firstPart = currencyName.split("(")
        val currencyCode = firstPart[1].split(")")
        return currencyCode[0]
    }
}