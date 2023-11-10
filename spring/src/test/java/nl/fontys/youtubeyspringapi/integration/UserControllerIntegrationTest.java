package nl.fontys.youtubeyspringapi.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.fontys.youtubeyspringapi.document.User;
import nl.fontys.youtubeyspringapi.document.UserInformation;
import nl.fontys.youtubeyspringapi.document.requests.LoginReq;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    private static String loginToken;

    @Test
    @Order(1)
    public void testUserRegistration() throws Exception {
        User newUser = new User();
        newUser.setEmail("uniqueTestUser@123.com");
        newUser.setPassword("password");
        newUser.setUsername("integrationTest2");
        newUser.setId("12345");

        mockMvc.perform(post("/rest/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value("uniqueTestUser@123.com")) // Assert the username
                .andExpect(jsonPath("$.password").value("password")).andExpect(jsonPath("$.username").value("integrationTest2"));
    }

    @Test
    @Order(2)
    public void testUserLogin() throws Exception {
        LoginReq loginRequest = new LoginReq();
        loginRequest.setEmail("uniqueTestUser@123.com");
        loginRequest.setPassword("password");

        MvcResult result = mockMvc.perform(post("/rest/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        System.out.println("Response Content: " + content);

        JsonNode jsonResponse = objectMapper.readTree(content);
        JsonNode tokenNode = jsonResponse.get("token");
        if (tokenNode != null) {
            loginToken = tokenNode.asText();
            System.out.println("Token: " + loginToken);
        } else {
            System.out.println("Token not found in the response.");
        }

    }

    @Test
    @Order(3)
    public void testUpdateUserDetails() throws Exception {
        UserInformation updatedDetails = new UserInformation();
        updatedDetails.setYtLink("https://www.youtube.com/channel/UCX6OQ3DkcsbYNE6H8uQQuVA");

        String userIdToUpdate = "12345";

        mockMvc.perform(MockMvcRequestBuilders.post("/rest/auth/profile/" + userIdToUpdate)
                        .header("Authorization", "Bearer " + loginToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDetails)))
                .andExpect(status().isOk());

    }

    @Test
    @Order(4)
    public void testGetUserDetails() throws Exception {
        String userIdToRetrieve = "12345";

        mockMvc.perform(MockMvcRequestBuilders.get("/rest/auth/profile/" + userIdToRetrieve)
                        .header("Authorization", "Bearer " + loginToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userIdToRetrieve)).andExpect(jsonPath("$.username").value("integrationTest2"));
    }

    @Test
    @Order(5)
    public void testUserDeletion() throws Exception {
        String userIdToDelete = "12345";

        mockMvc.perform(MockMvcRequestBuilders.delete("/rest/auth/profile/" + userIdToDelete)
                        .header("Authorization", "Bearer " + loginToken))
                .andExpect(status().isOk());
    }
}
