package com.antoine.goodCount

import android.content.Intent
import android.os.SystemClock
import android.text.SpannableStringBuilder
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.antoine.goodCount.ui.createAndEdit.edit.EditCommonPotActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_create_common_pot.*
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

private const val COMMON_POT_ID = "common pot id"
@RunWith(AndroidJUnit4::class)
class EditCommonPotActivityInstrumentTest {

    @get:Rule
    var mActivityRule: ActivityTestRule<EditCommonPotActivity> = ActivityTestRule(EditCommonPotActivity::class.java)

    @Before
    fun before(){
        val i = Intent()
        i.putExtra(COMMON_POT_ID, "dTVGlkmbWCg5qA8jNEhV")
        mActivityRule.launchActivity(i)
    }

    @Test
    fun checkInterfaceTest(){
        if (FirebaseAuth.getInstance().currentUser != null){
            Espresso.onView(ViewMatchers.withId(R.id.create_activity_title_editext)).check(ViewAssertions.matches(ViewMatchers.withText("Road trip")))

            Espresso.onView(ViewMatchers.withId(R.id.create_activity_description_editext)).check(ViewAssertions.matches(ViewMatchers.withText("Tous en vacances")))

            Espresso.onView(ViewMatchers.withId(R.id.create_activity_spinner)).check(ViewAssertions.matches(ViewMatchers.withText(getCurrency("EUR"))))

            Espresso.onView(ViewMatchers.withId(R.id.create_activity_your_name_editext)).check(ViewAssertions.matches(ViewMatchers.withText("")))

            Espresso.onView(ViewMatchers.withId(R.id.create_activity_add_common_pot_button)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

            Espresso.onView(ViewMatchers.withId(R.id.delete_common_pot_button)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        }
    }

    @Test
    fun changeInformationInEditTextTest(){
        if (FirebaseAuth.getInstance().currentUser != null){
            SystemClock.sleep(2000)
            Espresso.onView(ViewMatchers.withId(R.id.create_activity_title_editext)).perform(ViewActions.clearText())
            Espresso.onView(ViewMatchers.withId(R.id.create_activity_title_editext)).perform(ViewActions.typeText("Instrument test"))
            Espresso.onView(ViewMatchers.withId(R.id.create_activity_title_editext)).check(ViewAssertions.matches(ViewMatchers.withText("Instrument test")))

            Espresso.onView(ViewMatchers.withId(R.id.create_activity_description_editext)).perform(ViewActions.clearText())
            Espresso.onView(ViewMatchers.withId(R.id.create_activity_description_editext)).perform(ViewActions.typeText("I test the user interface"))
            Espresso.onView(ViewMatchers.withId(R.id.create_activity_description_editext)).check(ViewAssertions.matches(ViewMatchers.withText("I test the user interface")))

            mActivityRule.activity.create_activity_spinner.text = SpannableStringBuilder(getCurrency("USD"))
            Espresso.onView(ViewMatchers.withId(R.id.create_activity_spinner)).check(ViewAssertions.matches(ViewMatchers.withText(getCurrency("USD"))))

            Espresso.onView(ViewMatchers.withId(R.id.create_activity_your_name_editext)).perform(ViewActions.clearText())
            Espresso.onView(ViewMatchers.withId(R.id.create_activity_your_name_editext)).perform(ViewActions.typeText("Scarface"))
            Espresso.onView(ViewMatchers.withId(R.id.create_activity_your_name_editext)).check(ViewAssertions.matches(ViewMatchers.withText("Scarface")))

            Assert.assertTrue(mActivityRule.activity.create_activity_add_common_pot_button.isEnabled)
        }
    }

    private fun getCurrency(currencyCode: String): String {
        val currency = Currency.getInstance(currencyCode)
        return "${currency.displayName} (${currency.currencyCode})"
    }
}