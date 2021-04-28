package com.sph.sphtech

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.PerformException
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.hasSibling
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.sp.sphtech.ui.activity.MainActivity
import com.sp.sphtech.ui.adapter.YearAdapter
import com.sp.sphtech.utils.EspressoIdlingResource
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters


/**
 * Test class showcasing some [RecyclerViewActions] from Espresso.
 */
@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.JVM)
@LargeTest
class MainUiTest {

    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister()
    }

    /**
     * Use [ActivityScenario] to create and launch the activity under test. This is a
     * replacement for [androidx.test.rule.ActivityTestRule].
     */
    @get:Rule
    var activityScenarioRule =
        ActivityScenarioRule(
            MainActivity::class.java
        )

    @Test
    fun itemWithText_doesNotExist() {
        // Attempt to scroll to an item that contains the special text.
        Espresso.onView(ViewMatchers.withId(R.id.list)) // scrollTo will fail the test if no item matches.
            .perform(
                RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(
                    ViewMatchers.hasDescendant(ViewMatchers.withText("year: 2015"))
                )
            )
    }

    @Test(expected = PerformException::class)
    fun itemWith_loadData() { // no 2019
        // Attempt to scroll to an item that contains the special text.
        Espresso.onView(ViewMatchers.withId(R.id.list)) // scrollTo will fail the test if no item matches.
            .perform(
                RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(
                    ViewMatchers.hasDescendant(ViewMatchers.withText("year: 2019"))
                )
            )
    }


    /**
     * There are warnings for 2015
     */
    @Test
    fun itemWith_warn() {
        // Attempt to scroll to an item that contains the special text.
        Espresso.onView(ViewMatchers.withId(R.id.list)) // scrollTo will fail the test if no item matches.
            .perform(
                RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(
                    ViewMatchers.hasDescendant(tagMatch())
                )
            )

    }

    /**
     * Matches the warn show in the  the list.
     */
    private fun tagMatch(): Matcher<View> {
        return object : TypeSafeMatcher<View>() {

            override fun matchesSafely(item: View): Boolean {
                if (item is TextView) {
                    return item.compoundDrawables[2] != null && item.text.toString() == "year: 2015"
                }
                return false
            }

            override fun describeTo(description: Description?) {
//                description?.appendText(" load success");
            }
        }
    }

    /**
     * click and show dialog
     * test data load success
     */
    @Test
    fun item_click() {
        // First scroll to the position that needs to be matched and click on it.
        Espresso.onView(ViewMatchers.withId(R.id.list))
            .perform(
//                RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(ViewMatchers.withText("year: 2015"),ViewActions.click())
                RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                    ViewMatchers.hasDescendant(ViewMatchers.withText("year: 2015")),
                    ViewActions.click()
                )
            )
        val itemElementText = "close"
        Espresso.onView(ViewMatchers.withText(itemElementText)).inRoot(isDialog())
    }

}