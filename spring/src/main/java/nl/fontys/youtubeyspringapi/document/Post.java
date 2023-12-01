package nl.fontys.youtubeyspringapi.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    private String id;
    private String title;
    private String description;
    private String userId;
    private String status;
    private String link;
    private String public_url;
    private String editorId;
    private String editedVideo;
}
