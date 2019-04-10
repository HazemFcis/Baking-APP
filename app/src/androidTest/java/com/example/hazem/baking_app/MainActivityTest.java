package com.example.hazem.baking_app;

import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.*;

/**
 * Created by Hazem on 6/23/2018.
 */
public class MainActivityTest {

    @Before
    public void setUp() throws Exception {
    }
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void mainActivityTest() {
        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.TV_ingred), withText("Ingredients: \n"),
                        withParent(withId(R.id.sc))));
        appCompatTextView.perform(scrollTo(),
                replaceText("Ingredients: \nBittersweet chocolate (60-70% cacao)" +
                        " : 350.0 G\nunsalted butter : 226.0 G\ngranulated sugar" +
                        " : 300.0 G\nlight brown sugar : " +
                        "100.0 G\nlarge eggs :" +
                        " 5.0 UNIT\nvanilla extract" +
                        " : 1.0 TBLSP\nall purpose flour : 140.0 G\ncocoa powder " +
                        ": 40.0 G\nsalt : 1.5 TSP\nsemisweet chocolate chips : 350.0 G\n"),
                         closeSoftKeyboard());

    }
    @After
    public void tearDown() throws Exception {
    }

}