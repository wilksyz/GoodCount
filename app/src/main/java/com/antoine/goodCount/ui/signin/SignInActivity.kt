package com.antoine.goodCount.ui.signin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.antoine.goodCount.R
import com.antoine.goodCount.ui.main.MainActivity
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.*
import kotlinx.android.synthetic.main.activity_sign_in.*
import java.util.*

private const val RC_SIGN_IN = 1994
private const val ID_USER = "id user"
private const val TAG = "Sign in"
class SignInActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var callbackManager: CallbackManager

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth.currentUser
        this.updateUI(currentUser)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()
        // Initialize Facebook Login button
        this.initFacebookAuth()

        google_sign_in_button.setOnClickListener {
            this.signIn()
        }
        facebook_sign_in_button.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"))
        }
    }

    private fun initFacebookAuth(){
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                val credential = FacebookAuthProvider.getCredential(loginResult.accessToken.token)
                authWithCredential(credential)
            }
            override fun onCancel() {
                Snackbar.make(myCoordinatorLayout, "Authentication cancelled", Snackbar.LENGTH_SHORT).show()
            }
            override fun onError(error: FacebookException) {
                Snackbar.make(myCoordinatorLayout, "Authentication error", Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    private fun signIn() {
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK){
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)
                    val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
                    this.authWithCredential(credential)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Snackbar.make(myCoordinatorLayout, "Authentication failed", Snackbar.LENGTH_SHORT).show()
                }
            }else {
                Snackbar.make(myCoordinatorLayout, "Authentication cancelled", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun authWithCredential(credential: AuthCredential){
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success
                    val user = mAuth.currentUser
                    this.updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Snackbar.make(myCoordinatorLayout, "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val mainActivityIntent = Intent(this@SignInActivity, MainActivity::class.java)
            mainActivityIntent.putExtra(ID_USER, user.uid)
            startActivity(mainActivityIntent)
            finish()
        }
    }
}
