package nl.fontys.youtubeyspringapi.controllers;

import nl.fontys.youtubeyspringapi.document.Photo;
import nl.fontys.youtubeyspringapi.services.CustomUserDetailsService;
import nl.fontys.youtubeyspringapi.services.PhotoService;
import org.bson.types.Binary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PhotoControllerTest {
    @Mock
    private PhotoService photoService;
    @Mock
    private CustomUserDetailsService userService;

    @Mock
    private Model model;
    @Mock
    private MultipartFile image;

    private PhotoController photoController;

    @BeforeEach
    void initController() {
        //have to do this because it won't work with @InjectMocks/@Mock/@AutoWired
        photoService = Mockito.mock(PhotoService.class);
        userService = Mockito.mock(CustomUserDetailsService.class);
        photoController = new PhotoController(photoService, userService);
    }

    @Test
    void testAddPhoto() throws IOException {
        String title = "Test Title";
        Mockito.when(photoService.addPhoto(title, image)).thenReturn("photoId");

        String result = photoController.addPhoto(title, image, model);

        verify(userService).updateImageById(title, photoService.getPhoto(result));
        assertEquals("redirect:/photos/photoId", result);
    }
    //TODO: Fix this test, because for some reason model.addAttribute() doesn't work and model is null???
//    @Test
//    void testGetPhoto() {
//        String photoId = "photoId";
//        Photo testPhoto = new Photo("123", new Binary(new byte[1]));
//        Model newModel = Mockito.mock(Model.class);
//        newModel.addAttribute("image",
//                Base64.getEncoder().encodeToString(testPhoto.getImage().getData()));
//
//        Mockito.when(photoService.getPhoto(photoId)).thenReturn(testPhoto);
//        Mockito.when(photoController.getPhoto(photoId, newModel)).thenReturn(newModel.toString());
//
//        String result = photoController.getPhoto(photoId, newModel);
//
//        verify(newModel).addAttribute(eq("image"), anyString());
//        assertEquals(newModel.toString(), result);
//    }
}
