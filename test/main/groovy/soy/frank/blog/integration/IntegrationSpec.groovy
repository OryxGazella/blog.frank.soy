package soy.frank.blog.integration

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.boot.test.TestRestTemplate
import org.springframework.boot.test.WebIntegrationTest
import soy.frank.blog.Application
import spock.lang.Specification

import static groovy.json.JsonOutput.prettyPrint

@SpringApplicationConfiguration(classes = [Application.class])
@WebIntegrationTest(["server.port=0", "management.port=0"])
public class IntegrationSpec extends Specification {
    @Value("\${local.server.port}")
    def port = ""

    def "GET to / returns a title"() {
        given:
        def rt = new TestRestTemplate()
        def resp = rt.getForEntity("http://localhost:$port/", String.class)

        expect:
        resp.body.contains("<title>Frank's Blog</title>")
    }

    def "GET to /posts returns all posts"() {
        given:
        def rt = new TestRestTemplate()
        def resp = rt.getForEntity("http://localhost:$port/api/posts", String.class)

        expect:
        prettyPrint(resp.body) ==
                """[
    {
        "id": 0,
        "title": "TEST_TITLE",
        "renderedBody": "TEST_RENDERED",
        "createdAt": "2013-02-07T23:57:19.147154"
    }
]"""
    }
}