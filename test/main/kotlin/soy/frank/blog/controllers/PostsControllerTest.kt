package soy.frank.blog.controllers

import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import soy.frank.blog.models.Post
import soy.frank.blog.repositories.PostRepository
import org.hamcrest.MatcherAssert.*
import org.hamcrest.Matchers.*

class PostsControllerTest {

    val postRepository = Mockito.mock(javaClass<PostRepository>())
    val postsController = PostsController(postRepository = postRepository)

    Test
    fun indexShouldReturnAllPosts() {
        val post = Post(1, "A post")
        `when`(postRepository.findAll()).thenReturn(listOf(post))
        assertThat(postsController.index(), contains(post))
    }
}