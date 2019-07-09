package com.antoine.goodCount.ui.main

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.antoine.goodCount.R
import com.antoine.goodCount.models.Participant
import com.antoine.goodCount.ui.BaseActivity
import com.antoine.goodCount.ui.detail.DetailActivity
import com.antoine.goodCount.ui.main.fragment.MainFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import kotlinx.android.synthetic.main.dialog_request_username.view.*

private const val USER = "user"
private const val USER_ID = "user id"
private const val COMMON_POT_ID = "common pot id"
private const val ANSWER_WRITING_REQUEST = "answer writing request"
private const val ANSWER_REQUEST = 1919
private const val TAG = "MAIN_ACTIVITY"
class MainActivity : BaseActivity() {

    private lateinit var mMainActivityViewModel: MainActivityViewModel
    private var mUserId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sharedPref: SharedPreferences = getSharedPreferences(USER, MODE_PRIVATE)
        mUserId = sharedPref.getString(USER_ID, null)
        mUserId?.let { this.configureFragment(it) }
        this.configureViewModel()
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

    private fun configureViewModel(){
        mMainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
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

    // Get the dynamic link when the user clicks on it
    private fun receiveDynamicLinks() {
        FirebaseDynamicLinks.getInstance()
            .getDynamicLink(intent)
            .addOnSuccessListener(this) { pendingDynamicLinkData ->
                // Get deep link from result (may be null if no link is found)
                if (pendingDynamicLinkData != null) {
                    val deepLink: Uri = pendingDynamicLinkData.link
                    val commonPotId = deepLink.getQueryParameter("GoodCount")
                    if (commonPotId != null) this.accessToNewCount(commonPotId)
                }else if (FirebaseAuth.getInstance().currentUser == null){
                    startSignInActivity(null)
                }
            }
            .addOnFailureListener(this) { e ->
                Log.w(TAG, "getDynamicLink:onFailure", e)
            }
    }

    // Check if you are sign in and if a participant already exists for this common pot with your id
    private fun accessToNewCount(commonPotId: String) {
        if (FirebaseAuth.getInstance().currentUser != null){
            mUserId?.let { mMainActivityViewModel.checkParticipantCommonPot(commonPotId, it) }?.observe(this, Observer {
                if (it != null){
                    if (it){
                        this.startDetailActivity(commonPotId)
                    }else{
                        this.requestUsername(commonPotId)
                    }
                }
            })
        }else{
            startSignInActivity(commonPotId)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ANSWER_REQUEST){
            if (resultCode == Activity.RESULT_OK){
                val commonPotId = data?.getStringExtra(ANSWER_WRITING_REQUEST)
                commonPotId?.let { this.accessToNewCount(it) }
            }
        }
    }

    // Display an alertDialog to ask for your username
    @SuppressLint("InflateParams")
    private fun requestUsername(commonPotId: String) {
        val builder = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.dialog_request_username, null)
        builder.setView(view)
        builder.setTitle(getString(R.string.enter_your_name))
        builder.setPositiveButton(getString(R.string.create_user)) { _, _ -> }
        val dialog = builder.create()
        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val username = view.alert_dialog_username_editText.text.toString()
            this.createParticipant(username, commonPotId)
            dialog.dismiss()
        }
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled =
                    view.alert_dialog_username_editText.text.toString().isNotEmpty()
            }
        })
    }

    // Create a new Participant in database
    private fun createParticipant(username: String, commonPotId: String) {
        val participant = mUserId?.let { Participant("", commonPotId, it, username, true) }
        mMainActivityViewModel.createParticipant(participant)?.addOnSuccessListener {
            this.startDetailActivity(commonPotId)
        }?.addOnFailureListener { e ->
            Log.w(TAG, "Listen Participant Failed", e)
        }
    }

    private fun startDetailActivity(commonPotId: String) {
        val detailActivityIntent = Intent(this, DetailActivity::class.java)
        detailActivityIntent.putExtra(COMMON_POT_ID, commonPotId)
        startActivity(detailActivityIntent)
    }
}
