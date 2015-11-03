package soy.frank.blog.repositories

import org.springframework.data.repository.CrudRepository
import soy.frank.blog.models.Post

interface PostRepository : CrudRepository<Post, Long>