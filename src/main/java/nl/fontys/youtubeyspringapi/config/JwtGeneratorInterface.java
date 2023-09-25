package nl.fontys.youtubeyspringapi.config;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import nl.fontys.youtubeyspringapi.document.User;

import javax.naming.AuthenticationException;

public interface JwtGeneratorInterface {

    String createToken(User user);

    Claims resolveClaims(HttpServletRequest req);

    String resolveToken(HttpServletRequest request);

    boolean validateClaims(Claims claims) throws AuthenticationException;
}
