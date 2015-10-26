package soy.frank.blog.controllers

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.contains
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import soy.frank.blog.models.Post
import soy.frank.blog.repositories.PostRepository

class PostsControllerTest {

    val postRepository = Mockito.mock(PostRepository::class.java)
    val postsController = PostsController(postRepository = postRepository)

    @Test
    fun indexShouldReturnAllPosts() {
        val post = Post(1, "A post")
        `when`(postRepository.findAll()).thenReturn(listOf(post))
        assertThat(postsController.index(), contains(post))
    }
}