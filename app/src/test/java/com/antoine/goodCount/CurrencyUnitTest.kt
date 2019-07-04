package com.antoine.goodCount

import com.antoine.goodCount.utils.Currency
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class CurrencyUnitTest {

    @Test
    fun getAllCurrencyTest(){
        val currency = getCurrency()
        val currencyList = Currency.getAllCurrency()

        assertEquals(true, currencyList.contains(currency))
    }

    @Test
    fun getLocaleCurrencyTest(){
        val localeCurrencyCheck = getCurrency()
        val currencyList = Currency.getAllCurrency()
        val localeCurrencyIndex = Currency.getLocaleCurrency(currencyList)

        assertEquals(localeCurrencyCheck, currencyList[localeCurrencyIndex])
    }

    @Test
    fun getCurrencySelectedTest(){
        val currencyList = Currency.getAllCurrency()
        val indexOfCurrency = currencyList.indexOf(getCurrencyCanada())
        val currencySelected = Currency.getCurrencySelected(currencyList, "CAD")

        assertEquals(indexOfCurrency, currencySelected)
    }

    @Test
    fun getCurrencyCodeTest(){
        val currencyCode = Currency.getCurrencyCode(getCurrencyCanada())

        assertEquals("CAD", currencyCode)
    }

    private fun getCurrency(): String {
        val currency = java.util.Currency.getInstance(Locale.getDefault())
        return "${currency.displayName} (${currency.currencyCode})"
    }

    private fun getCurrencyCanada(): String {
        val currency = java.util.Currency.getInstance(Locale.CANADA)
        return "${currency.displayName} (${currency.currencyCode})"
    }
}