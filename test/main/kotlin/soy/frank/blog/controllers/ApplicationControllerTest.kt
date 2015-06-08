package soy.frank.blog.controllers

import org.junit.Test
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*

class ApplicationControllerTest {
    Test
    fun testHello() {
        val applicationController = ApplicationController()
        assertThat(applicationController.greeting(), equalTo("Hello from Kotlin"))
    }
}
