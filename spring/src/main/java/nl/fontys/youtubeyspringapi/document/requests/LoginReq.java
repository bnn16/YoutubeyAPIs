package nl.fontys.youtubeyspringapi.document.requests;

import lombok.Data;

@Data
public class LoginReq {
    private String email;
    private String password;
}