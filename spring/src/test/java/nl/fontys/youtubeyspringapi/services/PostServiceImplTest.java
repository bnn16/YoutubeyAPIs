package nl.fontys.youtubeyspringapi.services;
import nl.fontys.youtubeyspringapi.document.Post;
import nl.fontys.youtubeyspringapi.repositories.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
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

    @Test
    void testGetAllPostsWhereStatusIsCreated() {
        // Mock data
        Post post1 = new Post();
        post1.setStatus("Created");
        Post post2 = new Post();
        post2.setStatus("Created");
        Post post3 = new Post();
        post3.setStatus("Published");

        List<Post> allPosts = new ArrayList<>();
        allPosts.add(post1);
        allPosts.add(post2);

        when(postRepository.findAllByStatus("Created")).thenReturn(allPosts);

        List<Post> createdPosts = postService.getAllPostsWhereStatusIsCreated();

        // Verify the result
        assertEquals(2, createdPosts.size()); // Assuming only 2 posts have 'Created' status
    }

    @Test
    void testUpdatePostById2() {
        // Mock data
        String postId = "exampleId";
        Post existingPost = new Post();
        existingPost.setId(postId);
        existingPost.setTitle("Existing Title");
        existingPost.setDescription("Existing Description");
        existingPost.setEditedVideo("Existing Edited Video");
        existingPost.setPublic_url("Existing Public URL");
        existingPost.setEditorId("Existing Editor ID");
        existingPost.setStatus("Existing Status");

        Post updatedPost = new Post();
        updatedPost.setTitle("Updated Title");
        updatedPost.setDescription("Updated Description");
        updatedPost.setEditedVideo("Updated Edited Video");
        updatedPost.setPublic_url("Updated Public URL");
        updatedPost.setEditorId("Updated Editor ID");
        updatedPost.setStatus("Updated Status");

        // Mock behavior of postRepository
        when(postRepository.existsById(postId)).thenReturn(true);
        when(postRepository.findById(postId)).thenReturn(Optional.of(existingPost));

        // Call the service method
        postService.updatePostById(postId, updatedPost);

        // Verify that postRepository.save() was called with the updated post
        verify(postRepository).save(existingPost);

        // Verify that the existing post fields were updated
        assertEquals("Updated Title", existingPost.getTitle());
        assertEquals("Updated Description", existingPost.getDescription());
        assertEquals("Updated Edited Video", existingPost.getEditedVideo());
        assertEquals("Updated Public URL", existingPost.getPublic_url());
        assertEquals("Updated Editor ID", existingPost.getEditorId());
        assertEquals("Updated Status", existingPost.getStatus());
        // ... other assertions for the fields being updated
    }
}
