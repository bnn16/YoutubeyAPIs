package nl.fontys.youtubeyspringapi.repositories;

import nl.fontys.youtubeyspringapi.document.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    User findUserByEmail(String email);

    User findUserByUsername(String username);
}
