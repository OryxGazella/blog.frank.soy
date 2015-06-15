package soy.frank.blog.models

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table


Entity
Table(name = "posts")
data class Post(
        Id
        val id: Long = -1,
        val title: String = "",
        val renderedBody: String = "")