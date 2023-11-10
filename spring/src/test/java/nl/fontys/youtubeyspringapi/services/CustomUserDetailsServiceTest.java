package nl.fontys.youtubeyspringapi.services;

import nl.fontys.youtubeyspringapi.document.Photo;
import nl.fontys.youtubeyspringapi.document.User;
import nl.fontys.youtubeyspringapi.document.UserInformation;
import nl.fontys.youtubeyspringapi.repositories.UserInfoRepository;
import nl.fontys.youtubeyspringapi.repositories.UserRepository;
import org.bson.types.Binary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserInfoRepository userInfoRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUserByUsername_UserFound_ShouldReturnUserDetails() {
        // Arrange
        String userEmail = "test@example.com";
        String userPassword = "password123";
        User mockUser = new User(userEmail, userPassword); // Assuming you have a User class
        when(userRepository.findUserByEmail(userEmail)).thenReturn(mockUser);

        // Act
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userEmail);

        // Assert
        assertNotNull(userDetails);
        assertEquals(userEmail, userDetails.getUsername());
        assertEquals(userPassword, userDetails.getPassword());

        assertTrue(userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
    }


    @Test
    void loadUserByUsername_UserNotFound_ShouldThrowException() {
        // Arrange
        String userEmail = "nonexistent@example.com";
        when(userRepository.findUserByEmail(userEmail)).thenReturn(null);

        // Act and Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername(userEmail);
        });
    }



    @Test
    void saveUser_ShouldSaveUserAndUserInfo() {
        // Arrange
        User mockUser = new User("testUser", "password");
        when(userRepository.save(mockUser)).thenReturn(mockUser);

        // Act
        User savedUser = customUserDetailsService.saveUser(mockUser);

        // Assert
        assertNotNull(savedUser);
        verify(userInfoRepository, times(1)).save(any(UserInformation.class));
    }

    @Test
    void testFindUserByUsername() {
        // Arrange
        String username = "testuser";
        when(userRepository.findUserByUsername(username)).thenReturn(new User());

        // Act
        User foundUser = customUserDetailsService.findUserByUsername(username);

        // Assert
        assertNotNull(foundUser);
    }

    @Test
    void testFindByEmail() {
        // Arrange
        String email = "test@example.com";
        when(userRepository.findUserByEmail(email)).thenReturn(new User());

        // Act
        User foundUser = customUserDetailsService.findByEmail(email);

        // Assert
        assertNotNull(foundUser);
    }

    @Test
    void testFindByUserId() {
        // Arrange
        String userId = "123";
        when(userInfoRepository.findByUserId(userId)).thenReturn(new UserInformation());

        // Act
        UserInformation foundUserInfo = customUserDetailsService.findByUserId(userId);

        // Assert
        assertNotNull(foundUserInfo);
    }

    @Test
    void testFindById() {
        // Arrange
        String id = "123";
        when(userInfoRepository.findById(id)).thenReturn(Optional.of(new UserInformation()));

        // Act
        Optional<UserInformation> foundUserInfo = customUserDetailsService.findById(id);

        // Assert
        assertTrue(foundUserInfo.isPresent());
    }

    @Test
    void testSaveUser() {
        // Arrange
        User user = new User();
        user.setId("123");
        user.setUsername("testuser");
        user.setRole("USER");
        when(userRepository.save(user)).thenReturn(user);

        // Act
        User savedUser = customUserDetailsService.saveUser(user);

        // Assert
        assertNotNull(savedUser);
        assertEquals("testuser", savedUser.getUsername());
    }

    @Test
    void testSaveUserInfo() {
        // Arrange
        UserInformation userInfo = new UserInformation();

        // Act
        customUserDetailsService.saveUserInfo(userInfo);

        // Assert (verify that the save method was called on the repository)
        verify(userInfoRepository, times(1)).save(userInfo);
    }

    @Test
    void testUpdateImageById() {
        // Arrange
        String userId = "123";
        Photo image = new Photo();
        image.setImage( new Binary(new byte[1]));
        UserInformation userInfo = new UserInformation();
        userInfo.setUserId(userId);
        when(userInfoRepository.findByUserId(userId)).thenReturn(userInfo);

        // Act
        customUserDetailsService.updateImageById(userId, image);

        // Assert
        assertEquals(new Binary(new byte[1]), userInfo.getImage());
    }

    @Test
    void testEditById() {
        // Arrange
        String userId = "123";
        UserInformation existingUserInfo = new UserInformation();
        existingUserInfo.setUserId(userId);
        existingUserInfo.setUsername("oldUsername");
        existingUserInfo.setRole("OLD_ROLE");

        UserInformation updatedInfo = new UserInformation();
        updatedInfo.setId("newId");
        updatedInfo.setLocation("newLocation");
        updatedInfo.setUsername("newUsername");
        updatedInfo.setDescription("newDescription");
        updatedInfo.setYtLink("newYtLink");
        updatedInfo.setRole("NEW_ROLE");

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setUsername("oldUsername");
        existingUser.setRole("OLD_ROLE");

        when(userInfoRepository.findByUserId(userId)).thenReturn(existingUserInfo);
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        // Act
        customUserDetailsService.editById(userId, updatedInfo);

        // Assert
        assertEquals("newId", existingUserInfo.getId());
        assertEquals("newLocation", existingUserInfo.getLocation());
        assertEquals("newUsername", existingUserInfo.getUsername());
        assertEquals("newDescription", existingUserInfo.getDescription());
        assertEquals("newYtLink", existingUserInfo.getYtLink());
        assertEquals("NEW_ROLE", existingUserInfo.getRole());

        assertEquals("newUsername", existingUser.getUsername());
        assertEquals("NEW_ROLE", existingUser.getRole());
    }

    @Test
    void testEditById_UserNotFound() {
        // Arrange
        String userId = "nonexistentId";
        UserInformation updatedInfo = new UserInformation();

        when(userInfoRepository.findByUserId(userId)).thenReturn(null);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> customUserDetailsService.editById(userId, updatedInfo));
    }

}
