package com.antoine.goodCount

import android.os.SystemClock
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import com.antoine.goodCount.ui.signin.SignInActivity
import com.google.firebase.auth.FirebaseAuth
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SignInActivityInstrumentTest {

    private var mUiDevice: UiDevice? = null

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(SignInActivity::class.java)

    @Before
    @Throws(Exception::class)
    fun before() {
        mUiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    }

    @Test
    fun signInActivityTest(){
        if (FirebaseAuth.getInstance().currentUser == null){
            Espresso.onView(ViewMatchers.withId(R.id.google_sign_in_button)).perform(ViewActions.click())
            SystemClock.sleep(1500)
            mUiDevice?.findObject(UiSelector().text("Aiden PARIS"))?.click()
            SystemClock.sleep(2500)
        }else{
            Espresso.onView(ViewMatchers.withId(R.id.disconnect_button)).perform(ViewActions.click())
            SystemClock.sleep(1500)
            Espresso.onView(ViewMatchers.withId(R.id.google_sign_in_button)).perform(ViewActions.click())
            SystemClock.sleep(1500)
            mUiDevice?.findObject(UiSelector().text("Aiden PARIS"))?.click()
            SystemClock.sleep(2500)
        }
        assertNotNull(FirebaseAuth.getInstance().currentUser)
    }
}