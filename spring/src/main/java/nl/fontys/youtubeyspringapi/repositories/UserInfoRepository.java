package nl.fontys.youtubeyspringapi.repositories;

import nl.fontys.youtubeyspringapi.document.UserInformation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserInfoRepository extends MongoRepository<UserInformation, String> {
    UserInformation findAllById(String id);
    UserInformation findByUserId(String userId);
}
