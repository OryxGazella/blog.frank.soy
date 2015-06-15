package soy.frank.blog.repositories;

import org.springframework.data.repository.CrudRepository;
import soy.frank.blog.models.Post;

public interface PostRepository extends CrudRepository<Post, Long> {
}