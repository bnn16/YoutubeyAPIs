package nl.fontys.youtubeyspringapi.services;

import nl.fontys.youtubeyspringapi.document.Photo;
import nl.fontys.youtubeyspringapi.repositories.PhotoRepository;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class PhotoService {

    @Autowired
    private PhotoRepository photoRepo;

    public String addPhoto(String title, MultipartFile file) throws IOException {
        Photo photo = new Photo(title);
        photo.setImage(
                new Binary(BsonBinarySubType.BINARY, file.getBytes()));
        photo = photoRepo.insert(photo); return photo.getUserId();
    }

    public Photo getPhoto(String id) {
        return photoRepo.findByUserId(id);
    }
}