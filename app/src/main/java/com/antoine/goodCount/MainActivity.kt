package com.antoine.goodCount

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.antoine.goodCount.ui.SignInActivity
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
