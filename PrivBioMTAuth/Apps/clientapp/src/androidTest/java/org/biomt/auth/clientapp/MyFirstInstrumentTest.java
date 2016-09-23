package org.biomt.auth.clientapp;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.filters.SdkSuppress;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

import org.junit.*;
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
 * Created by hasini on 9/17/16.
 */
@RunWith(Parameterized.class)
@SdkSuppress(minSdkVersion = 18)
@LargeTest
public class MyFirstInstrumentTest {
    private UiDevice mDevice;

    @Parameterized.Parameters
    public static List<Object[]> data() {
        return Arrays.asList(new Object[2][0]);
    }

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @org.junit.Test
    public void testSignup() throws InterruptedException, UiObjectNotFoundException {
        String identity = "hasi7786@gg.com";
        String password = "xyzacb";

        //type a name and press auth button
        String rName = UUID.randomUUID().toString();
        onView(withId(R.id.editTextUserName)).perform(typeText(rName), closeSoftKeyboard());

        onView(withId(R.id.auth_button)).perform(click());

        /**get hold of the UI controls of the Auth App UIs through the uiautomator API.**/

        // Initialize UiDevice instance
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        UiObject identityTextField = mDevice.findObject(new UiSelector().text("hasi@abc.com"));
        identityTextField.setText(identity);

        UiObject passwordTextField = mDevice.findObject(new UiSelector().text("abc"));
        passwordTextField.setText(password);

        UiObject enrollButton = mDevice.findObject(new UiSelector().text("Enroll"));
        enrollButton.click();

        //Wait since it takes a while to get a response and save the token
        Thread.sleep(200);

        //see if the thread waits till it gets to the auth page.
        UiObject auth_identityTextField = mDevice.findObject(new UiSelector().text("hasi@abc.com"));
        auth_identityTextField.setText(identity);

        UiObject auth_passwordTextField = mDevice.findObject(new UiSelector().text("abc"));
        auth_passwordTextField.setText(password);

        UiObject auth_authButton = mDevice.findObject(new UiSelector().text("Authenticate"));
        auth_authButton.click();
        //onView(withId(R.id.auth_identityText)).perform(typeText(identity), closeSoftKeyboard());
        //onView(withId(R.id.auth_passwordText)).perform(typeText(password), closeSoftKeyboard());
        //onView(withId(R.id.auth_button)).perform(click());

        //wait since I need to record the response received.
        Thread.sleep(200);

        Context context = InstrumentationRegistry.getContext();
        final Intent intent = context.getPackageManager().getLaunchIntentForPackage("org.biomt.auth.clientapp");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

    }

}
