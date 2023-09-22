package nl.fontys.youtubeyspringapi.repositories;

import nl.fontys.youtubeyspringapi.document.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleRepository extends MongoRepository<Role, String> {
    Role findByRole(String role);
}
