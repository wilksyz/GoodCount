package com.antoine.goodCount.ui.createSpent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.antoine.goodCount.R
import kotlinx.android.synthetic.main.activity_create_spent.*

class CreateSpentActivity : AppCompatActivity() {

    private lateinit var mMenu: Menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_spent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar_activity_create, menu)
        menu.setGroupVisible(R.id.create_menu_group, false)
        mMenu = menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.create -> {
            this.createSpent()
            true
        }else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun createSpent(){
        val title = create_spent_title_editext.text.toString()
        val amount =  create_spent_amount_editext.text.toString().toDouble()

    }
}
