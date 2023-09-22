package nl.fontys.youtubeyspringapi.repositories;

import nl.fontys.youtubeyspringapi.document.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PostRepository extends MongoRepository<Post, String> {
    Post findByUserId(String userId);

    List<Post> findAllByUserId(String userId);
}
