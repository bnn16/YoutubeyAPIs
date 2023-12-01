package nl.fontys.youtubeyspringapi.services;

import nl.fontys.youtubeyspringapi.document.Post;
import nl.fontys.youtubeyspringapi.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public List<Post> getAllPostsWhereStatusIsCreated() {
        return postRepository.findAllByStatus("Created");
    }
    @Override
    public void savePost(Post post) {
        postRepository.save(post);
    }


    @Override
    public List<Post> getPostByUserId(String userId) {
        return postRepository.findByUserId(userId);
    }

    @Override
    public Optional<Post> getPostById(String id) {
        Optional<Post> post = postRepository.findById(id);
        if (post.isPresent()) {
            return post;
        }
        throw new IllegalArgumentException("Post does not exist");
    }

    @Override
    public List<Post> getPostByEditorId(String editorId) {
        return postRepository.findByEditorId(editorId);
    }

    @Override
    public void deletePostById(String id) {
        if (getPostById(id).isEmpty()) {
            throw new IllegalArgumentException("Post does not exist");
        } else {
            postRepository.deleteById(id);
        }
    }

    @Override
    public void updatePostById(String id, Post updatedPost) {
        if (!postRepository.existsById(id)) {
            throw new IllegalArgumentException("Post with ID " + id + " does not exist");
        }


        Post existingPost = postRepository.findById(id).orElse(null);

        if (existingPost != null) {
            if(updatedPost.getEditedVideo() != null){
                existingPost.setEditedVideo(updatedPost.getEditedVideo());
            }
            if(updatedPost.getEditorId() != null){
                existingPost.setEditorId(updatedPost.getEditorId());
            }
            if (updatedPost.getTitle() != null) {
                existingPost.setTitle(updatedPost.getTitle());
            }
            if(updatedPost.getPublic_url() != null){
                existingPost.setPublic_url(updatedPost.getPublic_url());
            }

            if (updatedPost.getDescription() != null) {
                existingPost.setDescription(updatedPost.getDescription());
            }

            if (updatedPost.getStatus() != null) {
                existingPost.setStatus(updatedPost.getStatus());
            }

            postRepository.save(existingPost);
        }
    }
}
