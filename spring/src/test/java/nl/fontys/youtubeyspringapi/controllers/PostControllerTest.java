package nl.fontys.youtubeyspringapi.controllers;

import nl.fontys.youtubeyspringapi.document.Post;
import nl.fontys.youtubeyspringapi.services.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class PostControllerTest {

    @Mock
    private PostService postService;

    private PostController postController;

    @BeforeEach
    void initController() {
        postController = new PostController(postService);
    }

    @Test
    void testPostPost_Success() {
        Post post = new Post("test-post", "test-post", "test-post","userid1", "open");
        ResponseEntity<?> responseEntity = postController.postPost(post);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }
    @Test
    void testPostPost_FieldEmpty() {
        Post post = new Post("", "", "test-post", "userid1", "open");
        ResponseEntity<?> responseEntity = postController.postPost(post);
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    @Test
    void testGetPostsByUserId_Success() {
        Post testPost = new Post("test-post", "test-post", "test-post", "userid1", "open");
        List<Post> posts = List.of(testPost);

        Mockito.when(postService.getPostByUserId("userid1")).thenReturn(posts);

        ResponseEntity<?> responseEntity = postController.getPostsByUserId("userid1");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(posts, responseEntity.getBody());
    }

    @Test
    void testGetPostsByUserId_NoPostsFound() {
        Mockito.when(postService.getPostByUserId("userid1")).thenReturn(List.of());

        ResponseEntity<?> responseEntity = postController.getPostsByUserId("userid1");

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("No posts found for user with ID: userid1", responseEntity.getBody());
    }

    @Test
    void testGetPostById_Success() {
        Post testPost = new Post("test-post", "test-post", "test-post", "userid1", "open");

        Mockito.when(postService.getPostById("id1")).thenReturn(Optional.of(testPost));

        ResponseEntity<?> responseEntity = postController.getPostById("id1");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(java.util.Optional.of(testPost), responseEntity.getBody());
    }
    @Test
    void testGetPostById_Exception() {
        Mockito.when(postService.getPostById("id1")).thenThrow(new IllegalArgumentException("Post does not exist"));

        ResponseEntity<?> responseEntity = postController.getPostById("id1");

        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertEquals("Post does not exist", responseEntity.getBody());
    }

    @Test
    void testDeletePostById_Success() {
        ResponseEntity<?> responseEntity = postController.deletePostById("id1");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testDeletePostById_Exception() {
        Mockito.doThrow(new IllegalArgumentException("Post does not exist")).when(postService).deletePostById("id1");

        ResponseEntity<?> responseEntity = postController.deletePostById("id1");

        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertEquals("Post does not exist", responseEntity.getBody());
    }

    @Test
    void testUpdatePostById_Success() {
        Post testPost = new Post("test-post", "test-post", "test-post", "userid1", "open");

        ResponseEntity<?> responseEntity = postController.updatePostById("id1", testPost);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testUpdatePostById_Exception() {
        Post testPost = new Post("test-post", "test-post", "test-post", "userid1", "open");
        Mockito.doThrow(new IllegalArgumentException("Post with ID id1 does not exist")).when(postService).updatePostById("id1", testPost);

        ResponseEntity<?> responseEntity = postController.updatePostById("id1", testPost);

        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertEquals("Post with ID id1 does not exist", responseEntity.getBody());
    }
}
