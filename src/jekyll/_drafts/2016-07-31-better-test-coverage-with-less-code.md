---
layout: post
title: Get better test coverage by having less code
categories: tests spock geb ratpack
date: 31 July 2016
---

## Start spreading the news

So I've been a bit hard on myself for neglecting my tests in the past so I though it was time to finally approach the
problem from two angles.

If you are reading this on or after the 31st of July, that means that you're no longer connecting to Ruby on Rails. 
The switch should have been seamless.

## Migration concerns

While the old site was mostly static content, there were a few fancy things that I wanted to keep such as:

* An asset pipeline that minifies and pre-gzips assets
* A google page speed score of 100 on mobile and desktop
* Previous links that exposed the 

![Ratpack](https://github.com/ratpack/ratpack/raw/master/ratpack-site/src/assets/images/ratpack-logo.png)


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