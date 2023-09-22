package nl.fontys.youtubeyspringapi.repositories;

import nl.fontys.youtubeyspringapi.document.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    // this repository interface is empty because we are
    // relying on the default methods provided
    // by Spring Data MongoDB.
    User findByEmail(String email);
}
