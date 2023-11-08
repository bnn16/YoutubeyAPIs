package nl.fontys.youtubeyspringapi.config;


import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import nl.fontys.youtubeyspringapi.document.User;
import nl.fontys.youtubeyspringapi.services.CustomUserDetailsService;
import org.springframework.stereotype.Component;

import javax.naming.AuthenticationException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class JwtGeneratorImpl implements JwtGeneratorInterface {

    //TODO: move to application.properties or .env, due to error with spring boot
    // ask chung or jacco if later

    private final String secret = "8i//=BdX*:kicL!q1A+}Y?zM0DT&$EDr0wMt:0y'";


    private final CustomUserDetailsService userService;

    private final long validityInMinutes = 60 * 24;
    private final JwtParser jwtParser;

    private final String TOKEN_HEADER = "Authorization";
    private final String TOKEN_PREFIX = "Bearer ";

    public JwtGeneratorImpl(CustomUserDetailsService userService) {
        this.userService = userService;
        this.jwtParser = Jwts.parser().setSigningKey(secret);
    }

    //create token, put necessary data in it, so we know who owns the jwt
    public String createToken(User user) {
        user = userService.findByEmail(user.getEmail());
        Claims claims = Jwts.claims().setSubject(user.getEmail());
        claims.put("username", user.getUsername());
        claims.put("roles", user.getRole());
        claims.put("id", user.getId());
        Date tokenCreateTime = new Date();
        Date tokenValidity = new Date(tokenCreateTime.getTime() + TimeUnit.MINUTES.toMillis(validityInMinutes));
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(tokenValidity)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    private Claims parseJwtClaims(String token) {
        return jwtParser.parseClaimsJws(token).getBody();
    }


    //get claims from token + find out if the token is expired or not
    public Claims resolveClaims(HttpServletRequest req) {
        try {
            String token = resolveToken(req);
            if (token != null) {
                return parseJwtClaims(token);
            }
            return null;
        } catch (ExpiredJwtException ex) {
            req.setAttribute("expired", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            req.setAttribute("invalid", ex.getMessage());
            throw ex;
        }
    }

    //get token from header and remove the Bearer part
    public String resolveToken(HttpServletRequest request) {

        String bearerToken = request.getHeader(TOKEN_HEADER);
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }


    public boolean validateClaims(Claims claims) throws AuthenticationException {
        try {
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            throw e;
        }
    }
}

