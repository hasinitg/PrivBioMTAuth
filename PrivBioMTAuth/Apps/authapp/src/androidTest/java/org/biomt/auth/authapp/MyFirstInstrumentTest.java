package org.biomt.auth.authapp;

/**
 * Created by hasini on 9/8/16.
 */

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * This test will
 */
//@RunWith(AndroidJUnit4.class)
@RunWith(Parameterized.class)
@LargeTest
public class MyFirstInstrumentTest {

    @Parameterized.Parameters
    public static List<Object[]> data() {
        return Arrays.asList(new Object[5][0]);
    }

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Test
    public void testAuthApp() throws InterruptedException {
        //type a name and press auth button
        System.out.println("RUNNING TEST.");
        String rName = UUID.randomUUID().toString();
        onView(withId(R.id.main_editText))
                .perform(typeText(rName), closeSoftKeyboard());

        onView(withId(R.id.main_auth_button)).perform(click());

        //enter data on the resulting activity and submit for enroll
        String identity = "hasi7786@gg.com";
        String password = "xyzacb";
        onView(withId(R.id.enroll_editTextIdentity)).perform(typeText(identity));
        onView(withId(R.id.enroll_editTextPassword)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.enroll_button)).perform(click());

        //Wait since it takes a while to get a response and save the token
        Thread.sleep(200);

        //see if the thread waits till it gets to the auth page.
        onView(withId(R.id.auth_identityText)).perform(typeText(identity), closeSoftKeyboard());
        onView(withId(R.id.auth_passwordText)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.auth_button)).perform(click());

        //wait since I need to record the response received.
        Thread.sleep(200);

    }
}
