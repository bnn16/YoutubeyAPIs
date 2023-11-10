package nl.fontys.youtubeyspringapi.services;
import nl.fontys.youtubeyspringapi.document.Photo;
import nl.fontys.youtubeyspringapi.repositories.PhotoRepository;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PhotoServiceTest {

    @Mock
    private PhotoRepository photoRepository;

    @InjectMocks
    private PhotoService photoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddPhoto() throws IOException {
        // Arrange
        String title = "Test Title";
        String userId = "testUserId";
        MultipartFile file = new MockMultipartFile("test.jpg", "test.jpg", "image/jpeg", "test data".getBytes());

        // Act
        when(photoRepository.findByUserId(userId)).thenReturn(null);
        when(photoRepository.insert(any(Photo.class))).thenAnswer(invocation -> {
            Photo photoArg = invocation.getArgument(0);
            photoArg.setUserId(userId); // Set the user ID manually for testing purposes
            return photoArg;
        });

        String result = photoService.addPhoto(title, file);

        //Assert
        verify(photoRepository, times(1)).insert(any(Photo.class));
        assertEquals(userId, result);
    }
    @Test
    void testGetPhoto() {
        // Arrange
        String userId = "123";
        Photo photo = new Photo();
        when(photoRepository.findByUserId(userId)).thenReturn(photo);

        // Act
        Photo result = photoService.getPhoto(userId);

        // Assert
        assertEquals(photo, result);
    }
}
