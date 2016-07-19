package it.snipsnap.slyce_messaging_example;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;

/**
 * @Author Matthew Page
 * @Date 7/18/16
 */

@RunWith(AndroidJUnit4.class)
public class FirstMessagesLoadTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void checkNumberOfMessages() {
        // TODO
    }

    @Test
    public void checkOrderOfMessages() {
        // TODO
    }

    @Test
    public void checkIsAtBottom() {
        // TODO
    }
}
