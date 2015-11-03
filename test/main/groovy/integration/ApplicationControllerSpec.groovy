package integration

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.boot.test.TestRestTemplate
import org.springframework.boot.test.WebIntegrationTest
import soy.frank.blog.Application
import spock.lang.Specification

@SpringApplicationConfiguration(classes = [Application.class])
@WebIntegrationTest(["server.port=0", "management.port=0"])
public class ApplicationControllerSpec extends Specification {
    @Value("\${local.server.port}")
    def port = ""

    def "GET to / returns a title"() {
        given:
        def rt = new TestRestTemplate()
        def resp = rt.getForEntity("http://localhost:$port/", String.class)

        expect:
        resp.body.contains("<title>Frank's Blog</title>")
    }
}