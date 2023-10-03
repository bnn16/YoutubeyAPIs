package nl.fontys.youtubeyspringapi.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nl.fontys.youtubeyspringapi.config.JwtGeneratorInterface;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


//https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/filter/OncePerRequestFilter.html
//OncePerRequestFilter is a base class for filters which need to do some sort of processing a single time per request.
// I'm using this class to create a filter which will be executed once per request.
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtGeneratorInterface jwtGenerator;
    private final ObjectMapper mapper;


    public JwtFilter(JwtGeneratorInterface jwtGenerator, ObjectMapper mapper) {
        this.jwtGenerator = jwtGenerator;
        this.mapper = mapper;
    }

    //If the accessToken is null it will pass the request to next filter chain.
    // Any login request will not have jwt token in their header, therefore they will be passed to next filter chain.
    // If any acess token is present, then it will validate the token and then authenticate the request in SecurityContext.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Map<String, Object> errorDetails = new HashMap<>();

        try {
            String accessToken = jwtGenerator.resolveToken(request);
            if (accessToken == null) {
                filterChain.doFilter(request, response);
                return;
            }
            System.out.println("token : " + accessToken);
            Claims claims = jwtGenerator.resolveClaims(request);

            if (claims != null & jwtGenerator.validateClaims(claims)) {
                String email = claims.getSubject();
                System.out.println("email : " + email);
                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(email, "", new ArrayList<>());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (Exception e) {
            errorDetails.put("message", "Authentication Error");
            errorDetails.put("details", e.getMessage());
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            mapper.writeValue(response.getWriter(), errorDetails);

        }
        filterChain.doFilter(request, response);
    }
}
