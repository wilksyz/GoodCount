package com.antoine.goodCount.ui.createAndEdit

import androidx.lifecycle.ViewModel
import com.antoine.goodCount.models.CommonPot
import com.antoine.goodCount.repository.FirestoreRepository
import java.util.*

class CreateViewModel: ViewModel() {

    private val mCurrencyList: MutableList<String> = ArrayList()

    fun getAllCurrency(): List<String> {
        val localeList = Locale.getAvailableLocales()
        for (locale in localeList){
            try {
                val currency = Currency.getInstance(locale)
                val string = "${currency.displayName} (${currency.symbol})"
                if (!mCurrencyList.contains(string)) mCurrencyList.add(string)
            }catch (e: Exception){ }
        }
        mCurrencyList.sort()
        mCurrencyList.removeAt(0)
        return mCurrencyList
    }

    fun getCurrencyCode(currencyName: String): String{
        val firstPart = currencyName.split("(")
        val currencyCode = firstPart[1].split(")")
        return currencyCode[0]
    }

    fun createCommonPot(commonPot: CommonPot){
        FirestoreRepository().createCommonPot(commonPot)
    }
}