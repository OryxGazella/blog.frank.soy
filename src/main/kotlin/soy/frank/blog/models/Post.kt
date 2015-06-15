package soy.frank.blog.models

import javax.persistence.Entity
import javax.persistence.Id


Entity
data class Post(
        Id
        val id: Long = -1,
        val title: String = "")