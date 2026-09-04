package com.example;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class ExampleRobolectricTest {

    @Test
    public void readStringFromContext() {
        Context context = ApplicationProvider.getApplicationContext();
        String appName = context.getString(R.string.app_name);
        assertEquals("Horários Ônibus", appName);
    }
}
