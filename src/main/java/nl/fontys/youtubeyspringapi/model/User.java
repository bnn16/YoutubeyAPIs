package nl.fontys.youtubeyspringapi.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Data
@AllArgsConstructor
@Document(collection= "users")
public class User {

    @Id private String id;
    @NotBlank
    @Size(max = 20)
    private String username;

    private String password;
    private String email;
    private String description;


    //hash the password
    public void setPassword(String password){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.password = passwordEncoder.encode(password);
    }
    //TODO: find out if youtube api auth needs to be put in here.
}
