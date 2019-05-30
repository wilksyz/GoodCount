package com.antoine.goodCount.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.antoine.goodCount.R
import com.antoine.goodCount.ui.detail.viewpager.SectionsPagerAdapter
import com.antoine.goodCount.ui.signin.SignInActivity
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        this.configureViewPager()


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar_activity_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when(item.itemId) {
        R.id.disconnect_button -> {
            disconnect()
            true
        }
        R.id.share_button -> {

             true
        }
        R.id.edit_button -> {

             true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun disconnect(){
        val auth = FirebaseAuth.getInstance()
        for (user in auth.currentUser!!.providerData) {
            if (user.providerId == "facebook.com") {
                auth.signOut()
                LoginManager.getInstance().logOut()
                startSignInActivity()
            }else if (user.providerId == "google.com"){
                auth.signOut()
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()
                val googleSignInClient = GoogleSignIn.getClient(this, gso)
                googleSignInClient.revokeAccess()
                startSignInActivity()
            }
        }
    }

    private fun startSignInActivity(){
        val signInActivityIntent = Intent(this@DetailActivity, SignInActivity::class.java)
        startActivity(signInActivityIntent)
    }

    private fun configureViewPager(){
        val sectionsPagerAdapter =
            SectionsPagerAdapter(this, supportFragmentManager)
        detail_activity_view_pager.adapter = sectionsPagerAdapter
        detail_activity_tablayout.setupWithViewPager(detail_activity_view_pager)
    }
}