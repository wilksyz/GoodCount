package com.antoine.goodCount

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.antoine.goodCount.ui.detail.DetailActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.antoine.goodCount.ui.createAndEditSpent.create.CreateSpentActivity
import com.antoine.goodCount.ui.createAndEditSpent.edit.EditSpentActivity
import com.antoine.goodCount.ui.detail.viewpager.spent.recyclerView.SpentFragmentRecyclerViewHolder
import kotlinx.android.synthetic.main.activity_detail.*
import org.junit.Assert.assertNotNull
import org.junit.Before
import java.text.NumberFormat
import java.util.*
import org.hamcrest.Matchers
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import org.hamcrest.Matchers.allOf


private const val COMMON_POT_ID = "common pot id"
@RunWith(AndroidJUnit4::class)
class DetailActivityInstrumentTest {

    @get:Rule
    var mActivityRule: ActivityTestRule<DetailActivity> = ActivityTestRule(DetailActivity::class.java)

    @Before
    fun before(){
        val i = Intent()
        i.putExtra(COMMON_POT_ID, "dTVGlkmbWCg5qA8jNEhV")
        mActivityRule.launchActivity(i)
    }

    @Test
    fun checkTotalCost(){
        onView(withId(R.id.spent_fragment_total_cost_textView)).check(matches(isDisplayed()))
        val totalCost = formatAtCurrency("EUR", 500.59)
        onView(withId(R.id.spent_fragment_total_cost_textView))
            .check(matches(withText("${mActivityRule.activity.resources.getString(R.string.total_cost)}\n$totalCost")))
        onView(withId(R.id.spent_fragment_my_cost_textView)).check(matches(isDisplayed()))
        val cost = formatAtCurrency("EUR", 0.0)
        onView(withId(R.id.spent_fragment_my_cost_textView))
            .check(matches(withText("${mActivityRule.activity.resources.getString(R.string.personal_cost)}\n$cost")))
    }

    @Test
    fun openCreateSpentActivityTest(){
        val activityMonitor = InstrumentationRegistry.getInstrumentation().addMonitor(CreateSpentActivity::class.java.name, null, false)
        onView(withId(R.id.spent_fragment_add_spent_fab)).perform(click())
        val nextActivity = InstrumentationRegistry.getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000)
        assertNotNull(nextActivity)
        nextActivity .finish()
    }

    @Test
    fun openEditSpentActivity(){
        if (getRecyclerViewCount() > 0){
            val activityMonitor = getInstrumentation().addMonitor(EditSpentActivity::class.java.name, null, false)
            onView(withId(R.id.spent_fragment_recyclerview)).perform(actionOnItemAtPosition<SpentFragmentRecyclerViewHolder>(0, click()))
            val nextActivity = getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000)
            assertNotNull(nextActivity)
            nextActivity .finish()
        }
    }

    @Test
    fun checkInitialPositionTabLayoutTest(){
        onView(withId(R.id.detail_activity_tablayout)).check(matches(isDisplayed()))
        val selectedTabPosition = mActivityRule.activity.detail_activity_tablayout.selectedTabPosition
        assertThat(selectedTabPosition, Matchers.equalTo(0))
        val countTab = mActivityRule.activity.detail_activity_tablayout.tabCount
        assertThat(countTab, Matchers.equalTo(2))
    }

    @Test
    fun navigationTabLayoutTest(){
        val matcherView1 = allOf(
            withText(mActivityRule.activity.resources.getString(R.string.tab_title_2)),
            isDescendantOfA(withId(R.id.detail_activity_tablayout)))
        onView(matcherView1).perform(click())
        assertThat(mActivityRule.activity.detail_activity_tablayout.selectedTabPosition, Matchers.equalTo(1))

        val matcherView0 = allOf(
            withText(mActivityRule.activity.resources.getString(R.string.tab_title_1)),
            isDescendantOfA(withId(R.id.detail_activity_tablayout)))
        onView(matcherView0).perform(click())
        assertThat(mActivityRule.activity.detail_activity_tablayout.selectedTabPosition, Matchers.equalTo(0))
    }

    private fun formatAtCurrency(currency: String?, amount: Double): String{
        val format = NumberFormat.getCurrencyInstance(Locale.getDefault())
        format.currency = Currency.getInstance(currency)
        return format.format(amount)
    }

    private fun getRecyclerViewCount(): Int {
        val recyclerView: RecyclerView = mActivityRule.activity.findViewById(R.id.spent_fragment_recyclerview)
        return recyclerView.adapter?.itemCount ?: 0
    }
}