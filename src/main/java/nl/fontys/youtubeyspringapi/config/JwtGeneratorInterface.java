package nl.fontys.youtubeyspringapi.config;

import nl.fontys.youtubeyspringapi.document.User;

import java.util.Map;

public interface JwtGeneratorInterface {

    Map<String, String> generateToken(User user);
}
