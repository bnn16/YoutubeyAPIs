package nl.fontys.youtubeyspringapi.services;

import nl.fontys.youtubeyspringapi.document.Post;
import nl.fontys.youtubeyspringapi.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService{

    private PostRepository postRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public void savePost(Post post) {
        if(getPostById(post.getId()).isPresent()){
            throw new IllegalArgumentException("Post already exists");
        };
        postRepository.save(post);
    }

    @Override
    public List<Post> getPostByUserId(String userId) {
        return postRepository.findAllByUserId(userId);
    }

    @Override
    public Optional<Post> getPostById(String id) {
        Optional<Post> post = postRepository.findById(id);
        if(post.isPresent()){
            return post;
        }
        throw new IllegalArgumentException("Post does not exist");
    }

    @Override
    public void deletePostById(String id) {
        if(getPostById(id).isEmpty()){
            throw new IllegalArgumentException("Post does not exist");
        }
        else {
            postRepository.deleteById(id);
        }
    }

    @Override
    public void updatePostById(String id,Post updatedPost) {
        // Check if the post with the given ID exists
        if (!postRepository.existsById(id)) {
            throw new IllegalArgumentException("Post with ID " + id + " does not exist");
        }


        Post existingPost = postRepository.findById(id).orElse(null);

        if (existingPost != null) {
            existingPost.setTitle(updatedPost.getTitle());
            existingPost.setDescription(updatedPost.getDescription());
            existingPost.setStatus(updatedPost.getStatus());
            postRepository.save(existingPost);
        }
    }
}
