package nl.fontys.youtubeyspringapi.services;

import nl.fontys.youtubeyspringapi.document.User;
import nl.fontys.youtubeyspringapi.exception.UserNotFoundException;
import nl.fontys.youtubeyspringapi.repositories.UserRepository;
import nl.fontys.youtubeyspringapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public User getUserByNameAndPassword(String userName, String password) throws UserNotFoundException {
        User user = userRepository.findByUserNameAndPassword(userName, password);
        if (user == null) {
            throw new UserNotFoundException("Invalid username and password");
        }
        return user;
    }
}
