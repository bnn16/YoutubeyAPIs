package nl.fontys.youtubeyspringapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class YoutubeySpringApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(YoutubeySpringApiApplication.class, args);
    }

}
