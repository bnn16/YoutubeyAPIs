package nl.fontys.youtubeyspringapi.controllers;

import nl.fontys.youtubeyspringapi.document.Photo;
import nl.fontys.youtubeyspringapi.services.CustomUserDetailsService;
import nl.fontys.youtubeyspringapi.services.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@RestController
@RequestMapping("/rest/requests/profile/photos")
public class PhotoController {


    private final PhotoService photoService;
    private final CustomUserDetailsService userService;

    @Autowired
    public PhotoController(PhotoService photoService, CustomUserDetailsService userService) {
        this.photoService = photoService;
        this.userService = userService;
    }
    @PostMapping("/add")
    public String addPhoto(@RequestParam("title") String title,
                           @RequestParam("image") MultipartFile image, Model model)
            throws IOException {
        String id = photoService.addPhoto(title, image);

        userService.updateImageById(title, photoService.getPhoto(id));
        return "redirect:/photos/" + id;
    }
    @GetMapping("/{id}")
    public String getPhoto(@PathVariable String id, Model model) {
        Photo photo = photoService.getPhoto(id);
        model.addAttribute("image",
                Base64.getEncoder().encodeToString(photo.getImage().getData()));
        return model.toString();
    }
}
