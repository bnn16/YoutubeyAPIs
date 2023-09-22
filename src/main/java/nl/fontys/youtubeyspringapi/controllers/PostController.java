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
@RequestMapping("/api/requests/edits")
public class PostController {
    private PostService postService;

    @Autowired
    public PostController(PostService postService){
        this.postService=postService;
    }

    @PostMapping("/post")
    public ResponseEntity postPost(Post post){
        try{
        postService.savePost(post);
        return new ResponseEntity(post,HttpStatus.CREATED);}
        catch(Exception e){
            return new ResponseEntity(e.getMessage(),HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/posts/user/{userId}")
    public List<Post> getPostsByUserId(@PathVariable String userId) throws EmptyPostListException {
        List<Post> posts;
        try {
            posts = postService.getPostByUserId(userId);
            if (posts.isEmpty()) {
                throw new EmptyPostListException("No posts found for user with ID: " + userId);
            }
        } catch (EmptyPostListException e) {

            throw new EmptyPostListException(e.getMessage());
        }
        return posts;
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity getPostById(@PathVariable String id){
        try{
            return new ResponseEntity(postService.getPostById(id),HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity(e.getMessage(),HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity deletePostById(@PathVariable String id){
        try{
            postService.deletePostById(id);
            return new ResponseEntity(HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity(e.getMessage(),HttpStatus.CONFLICT);
        }
    }

    @PatchMapping("/posts/{id}")
    public ResponseEntity updatePostById(@PathVariable String id, @RequestBody Post post){
        try{
            postService.updatePostById(id,post);
            return new ResponseEntity(HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity(e.getMessage(),HttpStatus.CONFLICT);
        }
    }

}
