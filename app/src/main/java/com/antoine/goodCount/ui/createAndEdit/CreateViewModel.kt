package com.antoine.goodCount.ui.createAndEdit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
}