package soy.frank.blog.models

import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "posts")
data class Post(
        @Id
        val id: Int = -1,
        val title: String = "",
        val renderedBody: String = "",
        val createdAt: LocalDateTime = LocalDateTime.MIN)