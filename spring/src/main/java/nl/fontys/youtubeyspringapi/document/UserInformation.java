package nl.fontys.youtubeyspringapi.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.Binary;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "usersInfo")
public class UserInformation {
    private String id;
    private String username;
    private String userId;
    private String role;
    private String description;
    private Binary image;
    private String ytLink;
    private String location;
}
