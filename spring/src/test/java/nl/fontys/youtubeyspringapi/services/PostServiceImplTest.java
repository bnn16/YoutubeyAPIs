package nl.fontys.youtubeyspringapi.services;
import nl.fontys.youtubeyspringapi.document.Post;
import nl.fontys.youtubeyspringapi.repositories.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PostServiceImplTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostServiceImpl postService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSavePost() {
        // Arrange
        Post post = new Post();

        // Act
        postService.savePost(post);

        // Assert
        verify(postRepository, times(1)).save(post);
    }

    @Test
    void testGetPostByUserId() {
        // Arrange
        String userId = "123";
        List<Post> posts = new ArrayList<>();
        when(postRepository.findByUserId(userId)).thenReturn(posts);

        // Act
        List<Post> result = postService.getPostByUserId(userId);

        // Assert
        assertNotNull(result);
        assertEquals(posts, result);
    }

    @Test
    void testGetPostById() {
        // Arrange
        String postId = "456";
        Post post = new Post();
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        // Act
        Optional<Post> result = postService.getPostById(postId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(post, result.get());
    }

    @Test
    void testGetPostById_PostNotFound() {
        // Arrange
        String postId = "nonexistentId";
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> postService.getPostById(postId));
    }

    @Test
    void testDeletePostById() {
        // Arrange
        String postId = "789";
        when(postRepository.findById(postId)).thenReturn(Optional.of(new Post()));

        // Act
        postService.deletePostById(postId);

        // Assert
        verify(postRepository, times(1)).deleteById(postId);
    }

    @Test
    void testDeletePostById_PostNotFound() {
        // Arrange
        String postId = "nonexistentId";
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> postService.deletePostById(postId));
    }

    @Test
    void testUpdatePostById() {
        // Arrange
        String postId = "101";
        Post existingPost = new Post();
        existingPost.setId(postId);
        existingPost.setTitle("Old Title");
        existingPost.setDescription("Old Description");
        existingPost.setStatus("Old Status");

        Post updatedPost = new Post();
        updatedPost.setTitle("New Title");
        updatedPost.setDescription("New Description");
        updatedPost.setStatus("New Status");

        when(postRepository.existsById(postId)).thenReturn(true);
        when(postRepository.findById(postId)).thenReturn(Optional.of(existingPost));

        // Act
        postService.updatePostById(postId, updatedPost);

        // Assert
        assertEquals("New Title", existingPost.getTitle());
        assertEquals("New Description", existingPost.getDescription());
        assertEquals("New Status", existingPost.getStatus());
        verify(postRepository, times(1)).save(existingPost);
    }

    @Test
    void testUpdatePostById_PostNotFound() {
        // Arrange
        String postId = "nonexistentId";
        Post updatedPost = new Post();

        when(postRepository.existsById(postId)).thenReturn(false);

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> postService.updatePostById(postId, updatedPost));
    }

    @Test
    void testUpdatePostById_NullUpdatedFields() {
        // Arrange
        String postId = "202";
        Post existingPost = new Post();
        existingPost.setId(postId);
        existingPost.setTitle("Old Title");
        existingPost.setDescription("Old Description");
        existingPost.setStatus("Old Status");

        Post updatedPost = new Post(); // no fields set

        when(postRepository.existsById(postId)).thenReturn(true);
        when(postRepository.findById(postId)).thenReturn(Optional.of(existingPost));

        // Act
        postService.updatePostById(postId, updatedPost);

        // Assert
        assertEquals("Old Title", existingPost.getTitle());
        assertEquals("Old Description", existingPost.getDescription());
        assertEquals("Old Status", existingPost.getStatus());
        verify(postRepository, times(1)).save(existingPost);
    }
}
