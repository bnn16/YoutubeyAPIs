package nl.fontys.youtubeyspringapi.controllers;

import nl.fontys.youtubeyspringapi.document.Post;
import nl.fontys.youtubeyspringapi.exception.EmptyPostListException;
import nl.fontys.youtubeyspringapi.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/requests/edits/posts")
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("")
    public ResponseEntity postPost(@RequestBody Post post) {
        try {
            if (!post.getDescription().isEmpty() && !post.getTitle().isEmpty() && post.getUserId() != null && post.getStatus() != null) {
                postService.savePost(post);
                return new ResponseEntity(post, HttpStatus.CREATED);
            } else {
                return new ResponseEntity("Post field/s is empty", HttpStatus.CONFLICT);
            }
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getPostsByUserId(@PathVariable String userId) {
        List<Post> posts = postService.getPostByUserId(userId);
        if (posts.isEmpty()) {
            return new ResponseEntity<>("No posts found for user with ID: " + userId, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/editor/{editorId}")
    public ResponseEntity<?> getPostsByEditorId(@PathVariable String editorId) {
        List<Post> posts = postService.getPostByEditorId(editorId);
        if (posts.isEmpty()) {
            return new ResponseEntity<>("No posts found for editor with ID: " + editorId, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<?> getAllPosts() {
        List<Post> posts = postService.getAllPostsWhereStatusIsCreated();
        if (posts.isEmpty()) {
            return new ResponseEntity<>("No posts found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity getPostById(@PathVariable String id) {
        try {
            return new ResponseEntity(postService.getPostById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletePostById(@PathVariable String id) {
        try {
            postService.deletePostById(id);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity updatePostById(@PathVariable String id, @RequestBody Post post) {
        try {
            postService.updatePostById(id, post);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

}
