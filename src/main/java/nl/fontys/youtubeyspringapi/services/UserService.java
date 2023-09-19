package nl.fontys.youtubeyspringapi.services;

import nl.fontys.youtubeyspringapi.DTO.UserDTO;
import nl.fontys.youtubeyspringapi.model.User;
import nl.fontys.youtubeyspringapi.repositories.UserRepository;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User newUser) {
        User existingUser = userRepository.findByUsername(newUser.getUsername());
        if (existingUser != null) {
            throw new RuntimeException("Username already exists");
        }
         newUser.setPassword(newUser.getPassword());
        return userRepository.save(newUser);
    }

    public User getUserById(String id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            throw new RuntimeException("User not found");
        }

        return optionalUser.get();
    }

    public User updateUser(String id, UserDTO userDTO) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            throw new RuntimeException("User not found");
        }

        User existingUser = optionalUser.get();

        if (userDTO.getUsername() != null) {
            existingUser.setUsername(userDTO.getUsername());
        }
        if (userDTO.getEmail() != null) {
            existingUser.setEmail(userDTO.getEmail());
        }
        if (userDTO.getDescription() != null) {
            existingUser.setDescription(userDTO.getDescription());
        }

        return userRepository.save(existingUser);
    }

    public void deleteUser(String id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            throw new RuntimeException("User not found");
        }

        User existingUser = optionalUser.get();
        userRepository.delete(existingUser);
    }
}
