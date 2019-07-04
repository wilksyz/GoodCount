package com.antoine.goodCount


import androidx.core.view.isVisible
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
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


/**
 * Instrumented test, which will execute on an Android device.
 */
@RunWith(AndroidJUnit4::class)
class MainActivityInstrumentedTest {

    @get:Rule
    var activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Test
    fun contextualMenuTest(){
        onView(ViewMatchers.withId(R.id.main_fragment_floating_action_button)).perform(ViewActions.click())
        onView(ViewMatchers.withId(R.id.main_fragment_recycler_view)).perform(ViewActions.click())
        assertTrue(activityRule.activity.main_fragment_floating_action_button.isVisible)
        assertFalse(activityRule.activity.main_fragment_button_add_common_pot.isVisible)
        assertFalse(activityRule.activity.main_fragment_button_join_common_pot.isVisible)
    }

    @Test
    fun recyclerViewTest(){
        val activityMonitor = getInstrumentation().addMonitor(DetailActivity::class.java.name, null, false)
        onView(ViewMatchers.withId(R.id.main_fragment_recycler_view)).perform(actionOnItemAtPosition<MainRecyclerViewHolder>(0, ViewActions.click()))
        val nextActivity = getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000)
        assertNotNull(nextActivity)
        nextActivity .finish()
    }

    @Test
    fun exitButtonTest(){
        val activityMonitor = getInstrumentation().addMonitor(SignInActivity::class.java.name, null, false)
        onView(ViewMatchers.withId(R.id.disconnect_button)).perform(ViewActions.click())
        val nextActivity = getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000)
        assertNotNull(nextActivity)
        nextActivity .finish()
    }
}
