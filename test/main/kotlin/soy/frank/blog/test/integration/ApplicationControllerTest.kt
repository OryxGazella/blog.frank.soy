package soy.frank.blog.test.integration

import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.boot.test.TestRestTemplate
import org.springframework.boot.test.WebIntegrationTest
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import soy.frank.blog.Application

@RunWith(SpringJUnit4ClassRunner::class)
@SpringApplicationConfiguration(classes = arrayOf(Application::class))
@WebIntegrationTest("server.port=0", "management.port=0")
public class ApplicationControllerTest {
    @Value("\${local.server.port}")
    val port = ""

    @Test
    fun shouldAllowHttpGetFromRoot() {
        val rt = TestRestTemplate()
        val resp = rt.getForEntity("http://localhost:$port/", String::class.java)
        assertTrue("Expecting an html page",
                resp.body.contains("<title>Frank's Blog</title>"))
    }

}