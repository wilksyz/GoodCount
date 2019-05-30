package com.antoine.goodCount.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.antoine.goodCount.R
import com.antoine.goodCount.ui.signin.SignInActivity
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

abstract class BaseActivity: AppCompatActivity() {

    protected fun disconnect(){
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
        val signInActivityIntent = Intent(this, SignInActivity::class.java)
        startActivity(signInActivityIntent)
    }
}