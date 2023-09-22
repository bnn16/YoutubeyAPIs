package nl.fontys.youtubeyspringapi.services;

import nl.fontys.youtubeyspringapi.document.User;
import nl.fontys.youtubeyspringapi.exception.UserNotFoundException;
import org.springframework.stereotype.Service;


@Service
public interface UserService {
    public void saveUser(User user);
    public User getUserByNameAndPassword(String name, String password) throws UserNotFoundException;
}