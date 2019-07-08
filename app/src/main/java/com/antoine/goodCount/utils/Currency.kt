package com.antoine.goodCount.utils

import java.text.NumberFormat
import java.util.*
import java.util.Currency
import kotlin.collections.ArrayList

object Currency {

    // Get all currency actually use into the world
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
        return currencyList
    }

    // Get the local currency
    fun getLocaleCurrency(currencyList: List<String>): Int {
        val currency = Currency.getInstance(Locale.getDefault())
        return currencyList.indexOf("${currency.displayName} (${currency.currencyCode})")
    }

    // Get the currency selected by the user
    fun getCurrencySelected(currencyList: List<String>, currencyCode: String): Int {
        val currency = Currency.getInstance(currencyCode)
        return currencyList.indexOf("${currency.displayName} (${currency.currencyCode})")
    }

    // Split the string for get the currency code
    fun getCurrencyCode(currencyName: String): String{
        val firstPart = currencyName.split("(")
        val currencyCode = firstPart[1].split(")")
        return currencyCode[0]
    }

    // Change a single Double in the format of the currency and the appropriate locality
    fun formatAtCurrency(currency: String?, amount: Double): String{
        val format = NumberFormat.getCurrencyInstance(Locale.getDefault())
        format.currency = Currency.getInstance(currency)
        return format.format(amount)
    }
}