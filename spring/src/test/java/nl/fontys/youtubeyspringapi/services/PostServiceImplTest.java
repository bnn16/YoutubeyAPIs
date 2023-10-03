package nl.fontys.youtubeyspringapi.services;

import nl.fontys.youtubeyspringapi.document.Post;
import nl.fontys.youtubeyspringapi.repositories.PostRepository;
import nl.fontys.youtubeyspringapi.services.PostServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PostServiceImplTest {

    private PostServiceImpl postService;

    @Mock
    private PostRepository postRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        postService = new PostServiceImpl(postRepository);
    }

    @Test
    public void testSavePost() {
        Post post = new Post();
        post.setTitle("Test Post");
        post.setDescription("This is a test post");
        post.setStatus("draft");

        postService.savePost(post);

        verify(postRepository, times(1)).save(post);
    }

    @Test
    public void testGetPostByUserId() {
        List<Post> posts = new ArrayList<>();
        Post post1 = new Post();
        post1.setTitle("Test Post 1");
        post1.setDescription("This is a test post 1");
        post1.setStatus("draft");
        post1.setUserId("user1");
        posts.add(post1);

        Post post2 = new Post();
        post2.setTitle("Test Post 2");
        post2.setDescription("This is a test post 2");
        post2.setStatus("published");
        post2.setUserId("user1");
        posts.add(post2);

        when(postRepository.findByUserId("user1")).thenReturn(posts);

        List<Post> result = postService.getPostByUserId("user1");

        assertEquals(posts, result);
    }

    @Test
    public void testGetPostById() {
        Post post = new Post();
        post.setId("1");
        post.setTitle("Test Post");
        post.setDescription("This is a test post");
        post.setStatus("draft");

        when(postRepository.findById("1")).thenReturn(Optional.of(post));

        Optional<Post> result = postService.getPostById("1");

        assertEquals(post, result.get());
    }

    @Test
    public void testGetPostByIdNotFound() {
        when(postRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> postService.getPostById("1"));
    }

    @Test
    public void testDeletePostById() {
        Post post = new Post();
        post.setId("1");
        post.setTitle("Test Post");
        post.setDescription("This is a test post");
        post.setStatus("draft");

        when(postRepository.findById("1")).thenReturn(Optional.of(post));

        postService.deletePostById("1");

        verify(postRepository, times(1)).deleteById("1");
    }

    @Test
    public void testDeletePostByIdNotFound() {
        when(postRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> postService.deletePostById("1"));
    }

    @Test
    public void testUpdatePostById() {
        Post existingPost = new Post();
        existingPost.setId("1");
        existingPost.setTitle("Test Post");
        existingPost.setDescription("This is a test post");
        existingPost.setStatus("draft");

        Post updatedPost = new Post();
        updatedPost.setTitle("Updated Test Post");
        updatedPost.setDescription("This is an updated test post");
        updatedPost.setStatus("published");

        when(postRepository.existsById("1")).thenReturn(true);
        when(postRepository.findById("1")).thenReturn(Optional.of(existingPost));

        postService.updatePostById("1", updatedPost);

        assertEquals(updatedPost.getTitle(), existingPost.getTitle());
        assertEquals(updatedPost.getDescription(), existingPost.getDescription());
        assertEquals(updatedPost.getStatus(), existingPost.getStatus());

        verify(postRepository, times(1)).save(existingPost);
    }

    @Test
    public void testUpdatePostByIdNotFound() {
        Post updatedPost = new Post();
        updatedPost.setTitle("Updated Test Post");
        updatedPost.setDescription("This is an updated test post");
        updatedPost.setStatus("published");

        when(postRepository.existsById(anyString())).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> postService.updatePostById("1", updatedPost));
    }
}