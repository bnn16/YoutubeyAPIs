package nl.fontys.youtubeyspringapi.document.responds;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorRes {
    HttpStatus httpStatus;
    String message;
}