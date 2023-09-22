package nl.fontys.youtubeyspringapi.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Document(collection= "users")
public class User {

    @Id
    private String id;
    @Indexed(unique = true)
    private String userName;
    private String password;
    private String role;
    private String email;
}