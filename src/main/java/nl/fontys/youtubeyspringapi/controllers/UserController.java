package nl.fontys.youtubeyspringapi.controllers;

import nl.fontys.youtubeyspringapi.DTO.UserDTO;
import nl.fontys.youtubeyspringapi.document.User;
import nl.fontys.youtubeyspringapi.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //create a new user
    @PostMapping("")
    public ResponseEntity createUser(@RequestBody User newUser) {
        try {
            User createdUser = userService.createUser(newUser);
            return ResponseEntity.ok().body("Successfully created a new account!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //get a user by id
    @GetMapping("{id}")
    public ResponseEntity getUserById(@PathVariable("id") String id) {
        try {
            User user = userService.getUserById(id);
            UserDTO userDTO = new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getDescription());
            return ResponseEntity.ok().body(userDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("{id}")
    public ResponseEntity updateUser(@PathVariable("id") String id, @RequestBody UserDTO userDTO) {
        try {
            User updatedUser = userService.updateUser(id, userDTO);
            return ResponseEntity.ok().body("User updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteUser(@PathVariable("id") String id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok().body("User deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}