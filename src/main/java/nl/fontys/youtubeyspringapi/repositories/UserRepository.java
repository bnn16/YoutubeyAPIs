package nl.fontys.youtubeyspringapi.repositories;

import nl.fontys.youtubeyspringapi.document.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    User findByUserNameAndPassword(String userName, String password);
}
