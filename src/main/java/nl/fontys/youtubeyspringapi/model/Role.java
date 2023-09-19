package nl.fontys.youtubeyspringapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@Setter
@Getter
@Document(collection="roles")
public class Role {
    @Id
    private String id;
    private ERole role;
}
