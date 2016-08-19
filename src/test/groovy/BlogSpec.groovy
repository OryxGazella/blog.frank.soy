import geb.spock.GebSpec
import ratpack.groovy.test.GroovyRatpackMainApplicationUnderTest
import spock.lang.Shared
import spock.lang.Unroll

class BlogSpec extends GebSpec {

    @Shared
    def aut = new GroovyRatpackMainApplicationUnderTest()

    def setup() {
        URI base = aut.address
        browser.baseUrl = base.toString()
    }

    @Unroll("GET to /posts/#Old_Post_Id redirects to the static page with title: #Post_Title")
    def "Renders a redirect for existing posts"() {
        when:
        go "posts/${Old_Post_Id}"

        then:
        title == Post_Title

        where:
        Old_Post_Id | Post_Title
        "9"         | "DTOs with care"
        "8"         | "A touch of style"
        "7"         | "What to do when you have strayed from the path of the Test Driven Ninja"
        "6"         | "RIP: The Monolithic Blogpost 2012-2012"
        "5"         | "Coded in Braam: September 2012"
        "3"         | "Security: art brut"
    }
}
