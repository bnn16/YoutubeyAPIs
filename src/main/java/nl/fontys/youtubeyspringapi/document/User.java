package nl.fontys.youtubeyspringapi.document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collection;
import java.util.Set;


@Data
@Document(collection= "users")
public class User implements UserDetails {

    @Id private String id;
    @NotNull
    @Indexed(unique = true)
    private String username;
    @NotNull
    @Size(max = 20)
    @JsonIgnore
    private String password;
    @NotNull
    @Indexed(unique = true)
    private String email;
    private boolean enabled;
    @DBRef
    private Set<Role> roles;


    //hash the password
    public void setPassword(String password){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.password = passwordEncoder.encode(password);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
    //TODO: find out if youtube api auth needs to be put in here.
}
