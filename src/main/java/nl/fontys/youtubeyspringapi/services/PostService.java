package nl.fontys.youtubeyspringapi.services;

import nl.fontys.youtubeyspringapi.document.Post;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public interface PostService {
    public void savePost(Post post);
    //no getAllPosts() because it's unrealistic for a user to have access to all the posts in the database
    public List<Post> getPostByUserId(String userId);
    public Optional<Post> getPostById(String id);
    public void deletePostById(String id);
    public void updatePostById(String id, Post post);
}
