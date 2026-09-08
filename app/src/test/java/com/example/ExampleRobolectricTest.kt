package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("hollam", appName)
    val loginTitle = context.getString(R.string.login_title)
    assertEquals("hollam", loginTitle)
    val loginButton = context.getString(R.string.login_button)
    assertEquals("تسجيل الدخول", loginButton)
  }
}
