package com.antoine.goodCount


import androidx.core.view.isVisible
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.ActivityTestRule
import com.antoine.goodCount.ui.main.MainActivity
import com.antoine.goodCount.ui.main.recyclerView.MainRecyclerViewHolder
import kotlinx.android.synthetic.main.fragment_main.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import org.junit.Rule
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import com.antoine.goodCount.ui.detail.DetailActivity
import com.antoine.goodCount.ui.signin.SignInActivity
import com.google.firebase.auth.FirebaseAuth
import org.junit.FixMethodOrder
import org.junit.runners.MethodSorters
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*

/**
 * Instrumented test, which will execute on an Android device.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4::class)
class MainActivityInstrumentedTest {

    @get:Rule
    var mActivityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Test
    fun contextualMenuTest(){
        if (FirebaseAuth.getInstance().currentUser != null){
            onView(withId(R.id.main_fragment_floating_action_button)).perform(ViewActions.click())
            onView(withId(R.id.main_fragment_recycler_view)).perform(ViewActions.click())
            assertTrue(mActivityRule.activity.main_fragment_floating_action_button.isVisible)
            assertFalse(mActivityRule.activity.main_fragment_button_add_common_pot.isVisible)
            assertFalse(mActivityRule.activity.main_fragment_button_join_common_pot.isVisible)
        }
    }

    @Test
    fun clickRecyclerViewTest(){
        if (FirebaseAuth.getInstance().currentUser != null){
            if (getRecyclerViewCount() > 0){
                val activityMonitor = getInstrumentation().addMonitor(DetailActivity::class.java.name, null, false)
                onView(withId(R.id.main_fragment_recycler_view)).perform(actionOnItemAtPosition<MainRecyclerViewHolder>(0, ViewActions.click()))
                val nextActivity = getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000)
                assertNotNull(nextActivity)
                nextActivity .finish()
            }
        }
    }

    @Test
    fun exitButtonTest(){
        if (FirebaseAuth.getInstance().currentUser != null){
            val activityMonitor = getInstrumentation().addMonitor(SignInActivity::class.java.name, null, false)
            onView(withId(R.id.disconnect_button)).perform(ViewActions.click())
            val nextActivity = getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000)
            assertNotNull(nextActivity)
            nextActivity .finish()
        }
    }

    @Test
    fun checkDisplayedOnRecyclerView(){
        if (FirebaseAuth.getInstance().currentUser != null){
            if (getRecyclerViewCount() > 0){
                onView(withId(R.id.main_view_holder_tittle_textView)).check(matches(isDisplayed()))
                onView(withId(R.id.main_view_holder_description_textView)).check(matches(isDisplayed()))
                onView(withId(R.id.view_holder_price_textView)).check(matches(withEffectiveVisibility(Visibility.GONE)))
                onView(withId(R.id.view_holder_date_textView)).check(matches(withEffectiveVisibility(Visibility.GONE)))
            }
        }
    }

    private fun getRecyclerViewCount(): Int {
        val recyclerView: RecyclerView = mActivityRule.activity.findViewById(R.id.main_fragment_recycler_view)
        return recyclerView.adapter?.itemCount ?: 0
    }
}
