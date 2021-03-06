import asset.pipeline.ratpack.AssetPipelineModule

import static io.netty.handler.codec.http.HttpResponseStatus.MOVED_PERMANENTLY
import static ratpack.groovy.Groovy.ratpack

def old_posts = [9: "dtos-with-care.html",
                 8: "a-touch-of-style.html",
                 7: "tdd-ninja.html",
                 6: "rip-the-monolithic-blogpost.html",
                 5: "coded-in-braam.html",
                 3: "security-art-brut.html"]

ratpack {
    bindings {
        module(AssetPipelineModule) { cfg ->
            cfg.sourcePath("../../../src/assets")
        }
    }
    handlers {
        all {
            if ((request.headers['X-Forwarded-Proto']?.toUpperCase()) == "HTTP") {
                redirect("https://${request.headers.host.replaceAll(/:\d+/, '')}${request.rawUri}")
            } else {
                context.next()
            }
        }
        get("icj2016") {
            redirect "/posts/i-code-java-2016.html"
        }
        old_posts.each {
            def slug = it.value
            get("posts/${it.key}") {
                redirect MOVED_PERMANENTLY.code(), "/posts/$slug"
            }
        }
    }
}
