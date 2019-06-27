package com.antoine.goodCount.ui.main

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.antoine.goodCount.R
import com.antoine.goodCount.ui.BaseActivity
import com.antoine.goodCount.ui.detail.DetailActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks

private const val USER = "user"
private const val USER_ID = "user id"
private const val COMMON_POT_ID = "common pot id"
class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sharedPref: SharedPreferences = getSharedPreferences(USER, MODE_PRIVATE)
        val userId = sharedPref.getString(USER_ID, null)
        userId?.let { this.configureFragment(it) }
        this.receiveDynamicLinks()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar_activity_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when(item.itemId) {
        R.id.disconnect_button -> {
            disconnect()
            true
        }else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun configureFragment(userId: String){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val args = Bundle()
        args.putString(USER_ID, userId)
        val mainFragment = MainFragment()
        mainFragment.arguments = args
        fragmentTransaction.replace(R.id.fragment_main_container, mainFragment)
        fragmentTransaction.commit()
    }

    private fun receiveDynamicLinks() {
        FirebaseDynamicLinks.getInstance()
            .getDynamicLink(intent)
            .addOnSuccessListener(this) { pendingDynamicLinkData ->
                // Get deep link from result (may be null if no link is found)
                if (pendingDynamicLinkData != null) {
                    val deepLink: Uri = pendingDynamicLinkData.link
                    val commonPotId = deepLink.getQueryParameter("GoodCount")
                    Log.e("TAG Links", "id= ${deepLink.getQueryParameter("GoodCount")}")
                    this.accessToNewCount(commonPotId)
                }
            }
            .addOnFailureListener(this) { e ->
                Log.w("Dynamic Links", "getDynamicLink:onFailure", e)
            }
    }

    private fun accessToNewCount(commonPotId: String?) {
        if (FirebaseAuth.getInstance().currentUser != null){
            val detailActivityIntent = Intent(this, DetailActivity::class.java)
            detailActivityIntent.putExtra(COMMON_POT_ID, commonPotId)
            startActivity(detailActivityIntent)
        }else{
            startSignInActivity()
        }
    }
}
