package nl.fontys.youtubeyspringapi.controllers;

import nl.fontys.youtubeyspringapi.config.JwtGeneratorInterface;
import nl.fontys.youtubeyspringapi.document.User;
import nl.fontys.youtubeyspringapi.document.UserInformation;
import nl.fontys.youtubeyspringapi.document.requests.LoginReq;
import nl.fontys.youtubeyspringapi.document.responds.ErrorRes;
import nl.fontys.youtubeyspringapi.document.responds.LoginRes;
import nl.fontys.youtubeyspringapi.exception.UserNotFoundException;
import nl.fontys.youtubeyspringapi.services.CustomUserDetailsService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


@RestController
@RequestMapping("/rest/auth")
public class UserController {
    private final AuthenticationManager authenticationManager;
    private final JwtGeneratorInterface jwtGenerator;

    private final CustomUserDetailsService userService;

    @Autowired
    public UserController(AuthenticationManager authenticationManager, JwtGeneratorInterface jwtGenerator, CustomUserDetailsService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtGenerator = jwtGenerator;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> postUser(@RequestBody User user) {
        try {
            if (user.getUsername() == null || user.getPassword() == null) {
                throw new UserNotFoundException("UserName or Password is Empty");
            }
            if (userService.findUserByUsername(user.getUsername()) != null) {
                throw new UserNotFoundException("UserName already exists");
            }
            userService.saveUser(user);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }


    //authenticate the user and return a JWT token + email + id + role
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginReq loginReq) {

        try {
            Authentication authentication =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginReq.getEmail(), loginReq.getPassword()));
            String email = authentication.getName();
            User user = new User(email, "");
            String id = userService.findByEmail(email).getId();
            String role = userService.findByEmail(email).getRole();
            String token = jwtGenerator.createToken(user);
            LoginRes loginRes = new LoginRes(id, email, role, token);

            return ResponseEntity.ok(loginRes);

        } catch (BadCredentialsException e) {
            ErrorRes errorResponse = new ErrorRes(HttpStatus.BAD_REQUEST, "Invalid username or password");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            ErrorRes errorResponse = new ErrorRes(HttpStatus.BAD_REQUEST, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity getUserDetails(@PathVariable String id) {
        try {
            UserInformation userInformation = userService.findByUserId(id);
            if(userInformation == null){
                throw new UserNotFoundException("User not found");
            }
            else{
                return ResponseEntity.ok(userInformation);
            }
        } catch (Exception e) {
            ErrorRes errorResponse = new ErrorRes(HttpStatus.BAD_REQUEST, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
    @PostMapping("/profile/{id}")
    public ResponseEntity postUserDetails(@PathVariable String id, @RequestBody UserInformation userInformation) {
        try {
            userService.saveUserInfo(userInformation);
            return ResponseEntity.ok(userService.findById(id));
        } catch (Exception e) {
            ErrorRes errorResponse = new ErrorRes(HttpStatus.BAD_REQUEST, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
    @PatchMapping("/profile/{id}")
    public ResponseEntity editUserDetails(@PathVariable String id, @RequestBody UserInformation userInformation) {
        try {
            userService.editById(id, userInformation);
            return ResponseEntity.ok(userService.findById(id));
        } catch (Exception e) {
            ErrorRes errorResponse = new ErrorRes(HttpStatus.BAD_REQUEST, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

}