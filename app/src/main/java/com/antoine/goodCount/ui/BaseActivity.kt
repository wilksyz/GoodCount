package com.antoine.goodCount.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.antoine.goodCount.R
import com.antoine.goodCount.ui.signin.SignInActivity
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

private const val COMMON_POT_ID = "common pot id"
private const val ANSWER_REQUEST = 1919
abstract class BaseActivity: AppCompatActivity() {

    protected fun disconnect(){
        val auth = FirebaseAuth.getInstance()
        val firebaseUser = auth.currentUser
        if (firebaseUser != null){
            for (user in firebaseUser.providerData) {
                if (user.providerId == "facebook.com") {
                    auth.signOut()
                    LoginManager.getInstance().logOut()
                    startSignInActivity(null)
                }else if (user.providerId == "google.com"){
                    auth.signOut()
                    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build()
                    val googleSignInClient = GoogleSignIn.getClient(this, gso)
                    googleSignInClient.revokeAccess()
                    startSignInActivity(null)
                }
            }
        }
    }

    protected fun startSignInActivity(commonPotId: String?){
        val signInActivityIntent = Intent(this, SignInActivity::class.java)
        if (commonPotId != null){
            signInActivityIntent.putExtra(COMMON_POT_ID, commonPotId)
            startActivityForResult(signInActivityIntent, ANSWER_REQUEST)
        }else{
            startActivity(signInActivityIntent)
        }
    }
}