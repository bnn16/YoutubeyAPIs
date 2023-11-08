package nl.fontys.youtubeyspringapi.services;

import lombok.Data;
import nl.fontys.youtubeyspringapi.document.Photo;
import nl.fontys.youtubeyspringapi.document.User;
import nl.fontys.youtubeyspringapi.document.UserInformation;
import nl.fontys.youtubeyspringapi.repositories.UserInfoRepository;
import nl.fontys.youtubeyspringapi.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository UserRepository;
    private final UserInfoRepository UserInfoRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = UserRepository.findUserByEmail(email);
        List<String> roles = new ArrayList<>();
        roles.add("USER");
        UserDetails userDetails =
                org.springframework.security.core.userdetails.User.builder()
                        .username(user.getEmail())
                        .password(user.getPassword())
                        .roles(roles.toArray(new String[0]))
                        .build();
        return userDetails;
    }

    public User findUserByUsername(String username) {
        return UserRepository.findUserByUsername(username);
    }


    public User saveUser(User user) {
        User saved = UserRepository.save(user);
        UserInformation userInfo = new UserInformation();
        userInfo.setUserId(user.getId());
        userInfo.setUsername(user.getUsername());
        userInfo.setRole(user.getRole());
        UserInfoRepository.save(userInfo);
        return saved;
    }

    public User findByEmail(String email) {
        return UserRepository.findUserByEmail(email);
    }

    public UserInformation findByUserId(String id) {
            return UserInfoRepository.findByUserId(id);
    }

    public Optional<UserInformation> findById(String id) {
        return UserInfoRepository.findById(id);
    }

    public void saveUserInfo(UserInformation userInfo) {
        UserInfoRepository.save(userInfo);
    }

    public void updateImageById(String id, Photo image) {
        UserInformation userInfo = UserInfoRepository.findByUserId(id);
        if (userInfo != null) {
            userInfo.setImage(image.getImage());
            UserInfoRepository.save(userInfo);
        }
    }
    public void editById(String id, UserInformation updatedInfo) {
        UserInformation userInfo = UserInfoRepository.findByUserId(id);
        User user = UserRepository.findById(id).orElse(null);

        if (userInfo != null && user != null){
            if(updatedInfo.getId() != null){
                userInfo.setId(updatedInfo.getId());
            }
            if(updatedInfo.getLocation() != null){
                userInfo.setLocation(updatedInfo.getLocation());
            }
            if (updatedInfo.getUsername() != null) {
                userInfo.setUsername(updatedInfo.getUsername());
                user.setUsername(updatedInfo.getUsername());
            }

            if (updatedInfo.getDescription() != null) {
                userInfo.setDescription(updatedInfo.getDescription());
            }
            if(updatedInfo.getYtLink() != null){
                userInfo.setYtLink(updatedInfo.getYtLink());
            }
            if(updatedInfo.getRole() != null) {
                userInfo.setRole(updatedInfo.getRole());
                user.setRole(updatedInfo.getRole());
            }
            UserInfoRepository.save(userInfo);
            UserRepository.save(user);
        }
       else{
            throw new IllegalArgumentException("User does not exist");
        }
    }
}
