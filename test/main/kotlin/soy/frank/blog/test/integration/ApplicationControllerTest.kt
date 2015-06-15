package soy.frank.blog.test.integration

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.boot.test.WebIntegrationTest
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.web.client.RestTemplate
import soy.frank.blog.Application
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.springframework.beans.factory.annotation.Value

RunWith(SpringJUnit4ClassRunner::class)
SpringApplicationConfiguration(classes = arrayOf(Application::class))
WebIntegrationTest("server.port=0", "management.port=0")
public class ApplicationControllerTest {
    Value("\${local.server.port}")
    val port = ""

    Test
    fun shouldAllowHttpGetFromRoot() {
        val rt = RestTemplate()
        val resp = rt.getForEntity("http://localhost:$port/", javaClass<String>())
        assertThat(resp.getBody(), equalTo("Hello from Kotlin"))
    }

}