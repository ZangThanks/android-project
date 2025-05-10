package com.poly.huynhthanhgiang_22716371;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
//import com.poly.huynhthanhgiang_22716371.util.EspressoIdlingResource;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.greaterThan;

import static com.poly.huynhthanhgiang_22716371.RecyclerViewItemCountAssertion.withItemCount;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class ProductListActivityTest {

    @Rule
    public ActivityScenarioRule<ProductListActivity> activityRule =
            new ActivityScenarioRule<>(ProductListActivity.class);

    @Before
    public void setUp() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void test_UIComponents_AreDisplayed() {
        onView(withId(R.id.autoSearch)).check(matches(isDisplayed()));
        onView(withId(R.id.recyclerProducts)).check(matches(isDisplayed()));
        onView(withId(R.id.btnCart)).check(matches(isDisplayed()));
        onView(withId(R.id.btnOrderHistory)).check(matches(isDisplayed()));
    }

    @Test
    public void test_loadProducts_PopulatesRecyclerView() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.recyclerProducts)).check(withItemCount(greaterThan(0)));
    }

    @Test
    public void test_searchFunctionality_TypeInAutoComplete() {
        onView(withId(R.id.autoSearch)).perform(typeText("Sample Product Search"), closeSoftKeyboard());
        onView(withId(R.id.autoSearch)).check(matches(withText("Sample Product Search")));
    }


    @Test
    public void test_navigateToCartActivity_OnCartButtonClick() {
        onView(withId(R.id.btnCart)).perform(click());
        intended(hasComponent(CartActivity.class.getName()));
    }

    @Test
    public void test_navigateToOrderHistoryActivity_OnOrderHistoryButtonClick() {
        onView(withId(R.id.btnOrderHistory)).perform(click());
        intended(hasComponent(OrderHistoryActivity.class.getName()));
    }
}