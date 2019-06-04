package com.antoine.goodCount.ui.createAndEdit

import android.util.Log
import androidx.lifecycle.ViewModel
import com.antoine.goodCount.models.CommonPot
import com.antoine.goodCount.models.Participant
import com.antoine.goodCount.repository.CommonPotRepository
import java.util.*

class CreateViewModel: ViewModel() {

    private val mCurrencyList: MutableList<String> = ArrayList()

    fun getAllCurrency(): List<String> {
        val localeList = Locale.getAvailableLocales()
        for (locale in localeList){
            try {
                val currency = Currency.getInstance(locale)
                val string = "${currency.displayName} (${currency.currencyCode})"
                if (!mCurrencyList.contains(string)) mCurrencyList.add(string)
            }catch (e: Exception){ }
        }
        mCurrencyList.sort()
        mCurrencyList.removeAt(0)
        return mCurrencyList
    }

    fun getLocaleCurrency(currencyList: List<String>): Int {
        val currency = Currency.getInstance(Locale.getDefault())
        return currencyList.indexOf("${currency.displayName} (${currency.currencyCode})")
    }

    fun getCurrencyCode(currencyName: String): String{
        val firstPart = currencyName.split("(")
        val currencyCode = firstPart[1].split(")")
        return currencyCode[0]
    }

    fun createCommonPot(commonPot: CommonPot, participant: Participant){
        CommonPotRepository().createCommonPot(commonPot, participant).addOnSuccessListener {
            Log.e("View Model", "Success")
        }
    }
}