package com.antoine.goodCount

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.antoine.goodCount.ui.createAndEdit.create.CreateCommonPotActivity
import kotlinx.android.synthetic.main.activity_create_common_pot.*
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class CreateCommonPotActivityInstrumentTest {

    @get:Rule
    var mActivityRule: ActivityTestRule<CreateCommonPotActivity> = ActivityTestRule(CreateCommonPotActivity::class.java)

    @Test
    fun interfaceTest(){
        onView(withId(R.id.create_activity_title_editext)).perform(ViewActions.typeText("Instrument test"))
        onView(withId(R.id.create_activity_title_editext)).check(matches(withText("Instrument test")))

        onView(withId(R.id.create_activity_description_editext)).perform(ViewActions.typeText("I test the user interface"))
        onView(withId(R.id.create_activity_description_editext)).check(matches(withText("I test the user interface")))

        onView(withId(R.id.create_activity_spinner)).check(matches(withText(getLocaleCurrency())))

        onView(withId(R.id.create_activity_your_name_editext)).perform(ViewActions.typeText("Scarface"))
        onView(withId(R.id.create_activity_your_name_editext)).check(matches(withText("Scarface")))

        Assert.assertTrue(mActivityRule.activity.create_activity_add_common_pot_button.isEnabled)
    }

    private fun getLocaleCurrency(): String {
        val currency = Currency.getInstance(Locale.getDefault())
        return "${currency.displayName} (${currency.currencyCode})"
    }
}