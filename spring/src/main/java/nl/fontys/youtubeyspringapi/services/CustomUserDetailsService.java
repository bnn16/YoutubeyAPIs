package nl.fontys.youtubeyspringapi.services;

import lombok.Data;
import nl.fontys.youtubeyspringapi.document.User;
import nl.fontys.youtubeyspringapi.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Data
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository UserRepository;

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
        return UserRepository.save(user);
    }

    public User findByEmail(String email) {
        return UserRepository.findUserByEmail(email);
    }
}
