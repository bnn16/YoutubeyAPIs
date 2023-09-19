package nl.fontys.youtubeyspringapi.repositories;

import nl.fontys.youtubeyspringapi.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    // this repository interface is empty because we are
    // relying on the default methods provided
    // by Spring Data MongoDB.
    User findByUsername(String username);
}
