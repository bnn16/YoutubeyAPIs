package nl.fontys.youtubeyspringapi.document.responds;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRes {
    private String userID;
    private String email;
    private String role;
    private String token;
}