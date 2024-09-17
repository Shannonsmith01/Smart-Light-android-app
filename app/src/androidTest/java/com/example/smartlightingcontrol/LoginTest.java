package com.example.smartlightingcontrol;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class LoginTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testSuccessfulLogin() {
        // Simulate user input
        onView(withId(R.id.username)).perform(replaceText("Shannon"));
        onView(withId(R.id.password)).perform(replaceText("s4682123"));

        // Simulate button click
        onView(withId(R.id.loginButton)).perform(click());

    }

    @Test
    public void testFailedLogin() {
        // Simulate user input for invalid credentials
        onView(withId(R.id.username)).perform(replaceText("asdffghj"));
        onView(withId(R.id.password)).perform(replaceText("fdgfhdhg"));

        // Simulate button click
        onView(withId(R.id.loginButton)).perform(click());


    }
}
