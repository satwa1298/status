package com.sharafindustries.status.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Base64;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.sharafindustries.status.model.User;
import com.sharafindustries.status.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
public class StatusControllerTest
{
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private UserService userService;
    
    private String authorizationHeader;
    
    private static boolean isInitialized = false;
    
    private String defaultEmail = "email@example.com";
    private String password = "password";
    private String friendEmail = "friend@example.com";
    private String friendToDeleteEmail = "friendToDelete@example.com";
    private String duplicateEmail = "duplicateEmail@example.com";
    
    @BeforeEach
    public void setup()
    {
    	if (!isInitialized)
    	{
	    	User user = userService.createAndSaveNewUser(defaultEmail, password);
	    	userService.addFriend(user, friendToDeleteEmail);
	    	
	    	User friendUser = userService.createAndSaveNewUser(friendEmail, password);
	    	userService.addFriend(friendUser, defaultEmail);
	    	
	    	userService.createAndSaveNewUser(duplicateEmail, password);
	    	isInitialized = true;
    	}
    	String base64EncodedCredentials = Base64.getEncoder().encodeToString("email@example.com:password".getBytes());
    	authorizationHeader = "Basic " + base64EncodedCredentials;
    }
    
    //TODO better test names
    //TODO more comprehensive tests
    @Test
    public void testGetStatus_ValidRequest() throws Exception
    {
        mockMvc.perform(get("/get-status")
            .header("Authorization", authorizationHeader)
            .param("email", friendEmail))
            .andExpect(status().isOk());
    }

    @Test
    public void testGetStatus_InvalidAuthorization() throws Exception
    {
        mockMvc.perform(get("/get-status")
            .header("Authorization", "invalidAuthorizationHeader")
            .param("email", friendEmail))
            .andExpect(status().isUnauthorized());
    }

    @Test
    public void testCreateCustomStatus_ValidRequest() throws Exception
    {
        mockMvc.perform(post("/create-custom-status")
            .header("Authorization", authorizationHeader)
            .param("statusName", "My Available")
            .param("availability", "Available")
            .param("message", "I am available"))
            .andExpect(status().isCreated());
    }

    @Test
    public void testCreateCustomStatus_InvalidAvailability() throws Exception
    {
        mockMvc.perform(post("/create-custom-status")
            .header("Authorization", authorizationHeader)
            .param("statusName", "Available")
            .param("availability", "Invalid")
            .param("message", "I am available"))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void testAddFriend_ValidRequest() throws Exception
    {
        mockMvc.perform(post("/add-friend")
            .header("Authorization", authorizationHeader)
            .param("emailToAdd", "friendToAdd@example.com"))
            .andExpect(status().isOk());
    }

    @Test
    public void testDeleteFriend_ValidRequest() throws Exception
    {
        mockMvc.perform(post("/delete-friend")
            .header("Authorization", authorizationHeader)
            .param("emailToDelete", friendToDeleteEmail))
            .andExpect(status().isOk());
    }

    @Test
    public void testGetUserCustomStatuses() throws Exception
    {
        mockMvc.perform(get("/my-statuses")
            .header("Authorization", authorizationHeader))
            .andExpect(status().isOk());
    }

    @Test
    public void testSetCurrentStatus_ValidRequest() throws Exception
    {
        mockMvc.perform(post("/set-status")
            .header("Authorization", authorizationHeader)
            .param("statusName", "Available"))
            .andExpect(status().isOk())
            .andExpect(content().string("status set"));
    }

    @Test
    public void testCreateUser_ValidRequest() throws Exception
    {
        mockMvc.perform(post("/create-user")
            .param("email", "newUser@example.com")
            .param("password", password))
            .andExpect(status().isCreated());
    }

    @Test
    public void testCreateUser_DuplicateEmail() throws Exception
    {
        mockMvc.perform(post("/create-user")
            .param("email", duplicateEmail)
            .param("password", password))
            .andExpect(status().isConflict());
    }
}
