package nl.fontys.youtubeyspringapi.repositories;

import nl.fontys.youtubeyspringapi.document.Photo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PhotoRepository extends MongoRepository<Photo, String> {
    Photo findByUserId(String userId);
}
