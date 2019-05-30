package com.antoine.goodCount.ui.main

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.antoine.goodCount.R
import com.antoine.goodCount.ui.signin.SignInActivity
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

private const val USER = "user"
private const val USER_ID = "user id"
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sharedPref: SharedPreferences = getSharedPreferences(USER, MODE_PRIVATE)
        val userId = sharedPref.getString(USER_ID, null)
        userId?.let { this.configureFragment(it) }
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
        val signInActivityIntent = Intent(this@MainActivity, SignInActivity::class.java)
        startActivity(signInActivityIntent)
    }
}
