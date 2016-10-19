---
layout: post
title: Get better test coverage by having less code
categories: tests spock geb ratpack
date: 31 July 2016
---

So I've been a bit hard on myself for neglecting my tests in the past so I though it was time to finally approach the
problem from two angles.

If you are reading this after the 31st of July, which I'm pretty sure you are, that means that you're no longer 
connecting to Ruby on Rails. The switch should have been seamless.

## Migration concerns

While the old site was mostly static content, there were a few fancy things that I wanted to keep such as:

* An asset pipeline that minifies and pre-gzips assets
* A google page speed score of 100 on mobile and desktop
* Previous links that exposed the database id of the post

## Start spreading the news!

![Ratpack](https://github.com/ratpack/ratpack/raw/master/ratpack-site/src/assets/images/ratpack-logo.png)

I finally saw the light and decided on a static site generator to author posts. Combining a dynamic web server with some 
static content allows for the ultimate in flexibility.

~~~ groovy
import static io.netty.handler.codec.http.HttpResponseStatus.MOVED_PERMANENTLY
import static ratpack.groovy.Groovy.ratpack
import asset.pipeline.ratpack.AssetPipelineModule

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
~~~



## Testing

I was keen to try out [Geb](http://www.gebish.org/) - very groovy automation. Geb provides
a JQuery like api for driving selenium in the background. The blog previously used postgres ids for the permalink. We
would rather like readable URLs to help indexing, but many places still linked to the old urls.

~~~ groovy
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
~~~

## So what?

What this buys us is all the fun of static site generators, with a webserver for dynamic content should we so wish.
The source code can be found [here](https://github.com/OryxGazella/blog.frank.soy) and some of the other stuff that 
was used to build the new version is described [here](/about.html).

* It also allows us to roll a bespoke commenting system, should we so wish.
* It means that we can create shortened urls easily `frank.soy/whatever` can issue a redirect.