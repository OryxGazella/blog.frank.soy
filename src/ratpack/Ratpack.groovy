import static io.netty.handler.codec.http.HttpResponseStatus.MOVED_PERMANENTLY

import static ratpack.groovy.Groovy.ratpack

def old_posts = [9: "DTOs with care",
                8: "A touch of style",
                7: "What do to when you have strayed from the path of the Test Driven Ninja",
                6: "RIP: The Monolithic Blogpost 2012-2012",
                5: "Coded in Braam: September 2012",
                3: "Security: art brut"]

ratpack {
    handlers {
        get("icj2016") {
            redirect "/posts/i-code-java-2016.html"
        }
        old_posts.each {
            def slug = it.key
            get("posts/${it.key}") {
                redirect MOVED_PERMANENTLY.code(), "http://frank.soy/posts/$slug"
            }
        }
    }
}
