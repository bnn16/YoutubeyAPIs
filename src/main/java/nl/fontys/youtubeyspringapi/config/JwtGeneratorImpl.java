package nl.fontys.youtubeyspringapi.config;


import io.jsonwebtoken.*;
import nl.fontys.youtubeyspringapi.document.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.util.*;

@Component
public class JwtGeneratorImpl implements JwtGeneratorInterface {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}") //24 hours
    private long validityInMilliseconds;


    //create the token and put the roles in the claims
    //then generate the validity and sign the token with the secret
    @Override
    public Map<String, String> generateToken(User user) {
        Date now = new Date();
        String jwtToken="";
        jwtToken = Jwts.builder().setSubject(user.getUserName()).setIssuedAt(now).setExpiration(new Date(now.getTime() + validityInMilliseconds)).signWith(SignatureAlgorithm.HS256, secret).compact();
        Map<String, String> jwtTokenGen = new HashMap<>();
        jwtTokenGen.put("token", jwtToken);
        return jwtTokenGen;
    }
}
