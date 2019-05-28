package com.antoine.goodCount.ui.createAndEdit

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.antoine.goodCount.R
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_create_common_pot.*
import java.util.*
import kotlin.collections.ArrayList

class CreateCommonPotActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_common_pot)
        this.configureSpinner()


    }

    private fun configureSpinner(){
        val localeList = Locale.getAvailableLocales()
        val currencyList = ArrayList<String>()
        for (locale in localeList){
            try {
                val currency = Currency.getInstance(locale)
                val string = "${currency.displayName} (${currency.symbol})"
                if (!currencyList.contains(string)) currencyList.add(string)
            }catch (e: Exception){ }
        }
        currencyList.sort()
        currencyList.removeAt(0)
        val adapter = ArrayAdapter(this, R.layout.dropdown_menu_popup_item, currencyList)
        create_activity_spinner.setAdapter(adapter)
    }
}
