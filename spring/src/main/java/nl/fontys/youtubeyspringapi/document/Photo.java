package nl.fontys.youtubeyspringapi.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "photos")
public class Photo {
    @Id
    private String id;

    private String userId;

    private Binary image;

    public Photo(String userId, Binary image) {
        this.userId = userId;
        this.image = image;
    }
    public Photo(String userId){
        this.userId = userId;
    }
}
