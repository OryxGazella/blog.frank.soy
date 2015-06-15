package soy.frank.blog.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import soy.frank.blog.models.Post
import soy.frank.blog.repositories.PostRepository

RestController
class PostsController {
    private val postRepository: PostRepository

    Autowired
    constructor(postRepository: PostRepository){
       this.postRepository = postRepository
    }

    RequestMapping("/posts")
    fun index() : Iterable<Post> {
        return postRepository.findAll()
    }
}

