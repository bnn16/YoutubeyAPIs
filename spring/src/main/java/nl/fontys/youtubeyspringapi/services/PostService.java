package nl.fontys.youtubeyspringapi.services;

import nl.fontys.youtubeyspringapi.document.Post;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface PostService {
    void savePost(Post post);

    //no getAllPosts() because it's unrealistic for a user to have access to all the posts in the database
    List<Post> getPostByUserId(String userId);

    Optional<Post> getPostById(String id);

    void deletePostById(String id);

    void updatePostById(String id, Post post);
}
