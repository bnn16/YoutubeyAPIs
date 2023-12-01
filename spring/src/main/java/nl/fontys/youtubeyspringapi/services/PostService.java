package nl.fontys.youtubeyspringapi.services;

import nl.fontys.youtubeyspringapi.document.Post;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface PostService {
    void savePost(Post post);

    List<Post> getAllPostsWhereStatusIsCreated();
    List<Post> getPostByUserId(String userId);
    List<Post> getPostByEditorId(String editorId);

    Optional<Post> getPostById(String id);

    void deletePostById(String id);

    void updatePostById(String id, Post post);
}
