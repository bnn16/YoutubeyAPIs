package nl.fontys.youtubeyspringapi.controllers;

import nl.fontys.youtubeyspringapi.auth.JwtGeneratorInterface;
import nl.fontys.youtubeyspringapi.document.User;
import nl.fontys.youtubeyspringapi.document.UserInformation;
import nl.fontys.youtubeyspringapi.document.requests.LoginReq;
import nl.fontys.youtubeyspringapi.services.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private CustomUserDetailsService userService;

    @Mock
    private JwtGeneratorInterface jwtGenerator;

    private UserController userController;

    @BeforeEach
    void initController() {
        userController = new UserController(authenticationManager, jwtGenerator, userService);
    }

    @Test
    void testPostUser_Success() {
        User user = new User("testemail@gmail.com", "password");
        user.setRole("user");
        user.setUsername("testemail123");
        user.setFirstName("test-firstname");
        ResponseEntity<?> responseEntity = userController.postUser(user);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    void testPostUser_PasswordEmpty() {
        User user = new User("123@gmail.com", "");
        ResponseEntity<?> responseEntity = userController.postUser(user);
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }
    @Test
    void testPostUser_UsernameEmpty() {
        User user = new User("", "testing123");
        ResponseEntity<?> responseEntity = userController.postUser(user);
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    @Test
    void testPostUser_UsernameExists() {
        User existingUser = new User("bbb@gmail.com", "existingpassword");

        User newUser = new User("bbb@gmail.com", "newpassword");
        ResponseEntity<?> responseEntity = userController.postUser(newUser);
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }


    @Test
    void testLogin_Success() {
        LoginReq loginReq = new LoginReq("testemail", "testpassword");
        Authentication authentication = new UsernamePasswordAuthenticationToken(loginReq.getEmail(), loginReq.getPassword());
        Mockito.when(authenticationManager.authenticate(authentication)).thenReturn(authentication);

        User user = new User(loginReq.getEmail(), "");
        Mockito.when(userService.findByEmail(loginReq.getEmail())).thenReturn(user);

        String token = "testtoken";
        Mockito.when(jwtGenerator.createToken(user)).thenReturn(token);

        ResponseEntity responseEntity = userController.login(loginReq);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testLogin_BadCredentials() {
        LoginReq loginReq = new LoginReq("testemail", "testpassword");
        Mockito.when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid username or password"));

        ResponseEntity responseEntity = userController.login(loginReq);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void testLogin_Exception() {
        LoginReq loginReq = new LoginReq("testemail", "testpassword");
        Mockito.when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Test exception"));

        ResponseEntity responseEntity = userController.login(loginReq);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void testGetUserDetails_Success() {
        String id = "testid";
        UserInformation userInformation = new UserInformation();
        Mockito.when(userService.findByUserId(id)).thenReturn(userInformation);

        ResponseEntity responseEntity = userController.getUserDetails(id);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testGetUserDetails_UserNotFound() {
        String id = "testid";
        Mockito.when(userService.findByUserId(id)).thenReturn(null);

        ResponseEntity responseEntity = userController.getUserDetails(id);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void testGetUserDetails_Exception() {
        String id = "testid";
        Mockito.when(userService.findByUserId(id)).thenThrow(new RuntimeException("Test exception"));

        ResponseEntity responseEntity = userController.getUserDetails(id);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void testPostUserDetails_Success() {
        String id = "testid";
        UserInformation userInformation = new UserInformation();
        userInformation.setYtLink("testlink");

        ResponseEntity responseEntity = userController.postUserDetails(id, userInformation);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
    @Test
    void testEditUserDetails_Success() {
        String id = "testid";
        UserInformation userInformation = new UserInformation();
        userInformation.setYtLink("testlink");

        ResponseEntity responseEntity = userController.editUserDetails(id, userInformation);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testEditUserDetails_Exception() {
        String id = "testid";
        UserInformation userInformation = new UserInformation();
        userInformation.setYtLink("testlink");
        Mockito.doThrow(new RuntimeException("Test exception")).when(userService).editById(id, userInformation);

        ResponseEntity responseEntity = userController.editUserDetails(id, userInformation);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
}
