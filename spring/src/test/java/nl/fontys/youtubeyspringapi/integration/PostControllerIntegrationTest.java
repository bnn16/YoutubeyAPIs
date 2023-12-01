package nl.fontys.youtubeyspringapi.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import nl.fontys.youtubeyspringapi.document.Post;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


// Import necessary dependencies

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PostControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String testPostId;

    private String generateJwtToken() {
        // Mock token generation logic
        String secretKey = "8i//=BdX*:kicL!q1A+}Y?zM0DT&$EDr0wMt:0y'"; // Replace this with your secret key
        String token = Jwts.builder()
                .setSubject("user123")
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // Token expires in 1 hour
                .signWith(SignatureAlgorithm.HS512, secretKey.getBytes())
                .compact();
        return "Bearer " + token;
    }


    // Test for creating a post
    @Test
    @Order(1)
    public void testPostCreation() throws Exception {
        Post newPost = new Post();
        newPost.setTitle("Test Post Title");
        newPost.setDescription("Test Post Description");
        newPost.setUserId("user123");
        newPost.setStatus("draft");

        mockMvc.perform(post("/rest/requests/edits/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPost)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Test Post Title"))
                .andExpect(jsonPath("$.description").value("Test Post Description"))
                .andDo(result -> {
                    String contentAsString = result.getResponse().getContentAsString();
                    testPostId = objectMapper.readTree(contentAsString).get("id").asText();
                });
    }

    // Test for fetching posts by a user ID
    @Test
    @Order(2)
    public void testGetPostsByUserId() throws Exception {
        String userId = "user123";

        mockMvc.perform(get("/rest/requests/edits/posts/user/" + userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        // Add assertions to check the response body for posts by this user ID
    }

    // Test for fetching a post by ID
    @Test
    @Order(3)
    public void testGetPostById() throws Exception {
        // Use the testPostId from testPostCreation
        mockMvc.perform(get("/rest/requests/edits/posts/" + testPostId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        // Add assertions to check the response body for the specific post
    }

    // Test for updating a post by ID
    @Test
    @Order(4)
    public void testPostUpdateById() throws Exception {
        // Use the testPostId from testPostCreation
        Post updatedPost = new Post();
        updatedPost.setTitle("Updated Test Post Title");
        updatedPost.setDescription("Updated Test Post Description");
        updatedPost.setUserId("user123");
        updatedPost.setStatus("published");

        mockMvc.perform(patch("/rest/requests/edits/posts/" + testPostId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPost)))
                .andExpect(status().isOk());
        // Optionally, fetch the updated post and validate changes
    }

    // Test for deleting a post by ID
    @Test
    @Order(5)
    public void testPostDeletion() throws Exception {
        // Use the testPostId from testPostCreation
        mockMvc.perform(delete("/rest/requests/edits/posts/" + testPostId))
                .andExpect(status().isOk());
        // Optionally, check if the post with testPostId is deleted successfully
    }

    // Clean up after all tests are done
    @AfterAll
    public void cleanUp() throws Exception {
        if (testPostId != null && !testPostId.isEmpty()) {
            mockMvc.perform(delete("/rest/requests/edits/posts/" + testPostId))
                    .andExpect(status().isOk());
            // Optionally, check if the testPostId is deleted successfully after all tests
        }
    }
}
