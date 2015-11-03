package controllers

import soy.frank.blog.controllers.PostsController
import soy.frank.blog.models.Post
import soy.frank.blog.repositories.PostRepository
import spock.lang.Specification

import java.time.LocalDateTime

class PostsControllerSpec extends Specification {
    def postRepositoryMock = Mock(PostRepository)
    def postsController = new PostsController(postRepositoryMock)
    private post = new Post(1, "A post", "", LocalDateTime.MIN)

    def "index() returns all posts"() {
        given:
        1 * postRepositoryMock.findAll() >> [post]

        when:
        def out = postsController.index()

        then:
        out == [post]
    }
}