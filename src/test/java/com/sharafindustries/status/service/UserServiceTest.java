package com.sharafindustries.status.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;
import org.springframework.web.server.ResponseStatusException;

import com.sharafindustries.status.exception.InvalidAvailabilityException;
import com.sharafindustries.status.model.Availability;
import com.sharafindustries.status.model.Status;
import com.sharafindustries.status.model.User;
import com.sharafindustries.status.repository.UserRepository;

public class UserServiceTest
{

	@Mock
	private ApplicationContext context;

	@Mock
	private UserRepository userRepository;

	@Mock
	private StatusService statusService;

	@InjectMocks
	private UserService userService;

	@BeforeEach
	public void setup()
	{
		MockitoAnnotations.openMocks(this);
	}

	@Test
    public void testCreateAndSaveNewUser_CreatesAndSavesNewUser() 
	{
		User testUser = new User("email", "password");
        when(userRepository.findByEmail("email")).thenReturn(null);
        when(context.getBean(User.class, "email", "password")).thenReturn(testUser);
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        User user = userService.createAndSaveNewUser("email", "password");
        assertEquals("email", user.getEmail());
    }

	@Test
    public void testCreateAndSaveNewUser_ThrowsExceptionWhen_RegistrationIsAttemptedWithExistingEmailAddress() 
	{
        when(userRepository.findByEmail("email")).thenReturn(new User("email", "password"));
        assertThrows(ResponseStatusException.class, () -> 
        {
            userService.createAndSaveNewUser("email", "password");
        });
    }

	@Test
    public void testGetUserByEmail_ReturnsCorrectUser() 
	{
        when(userRepository.findByEmail("email")).thenReturn(new User("email", "password"));
        User user = userService.getUserByEmail("email");
        assertEquals("email", user.getEmail());
    }

	@Test
    public void testGetUserByEmail_ReturnsNull_WhenEmailIsNotFound() 
	{
        when(userRepository.findByEmail("email")).thenReturn(null);
        assertNull(userService.getUserByEmail("email"));
    }

	@Test
	public void testAuthenticateUser_SuccessfullyReturnsCorrectUser()
	{
		String header = "Basic " + Base64.getEncoder().encodeToString("email:password".getBytes());
		when(userRepository.findByEmail("email")).thenReturn(new User("email", "password"));
		User user = userService.authenticateUser(header);
		assertEquals("email", user.getEmail());
	}
	
	@Test
    public void testAuthenticateUser_ThrowsException_WhenHeaderIsInvalid() 
	{
        assertThrows(ResponseStatusException.class, () -> 
        {
            userService.authenticateUser("InvalidHeader");
        });
    }

    @Test
    public void testAuthenticateUser_ThrowsException_WhenPasswordIsIncorrect() 
    {
        String header = "Basic " + Base64.getEncoder().encodeToString("email:wrongpassword".getBytes());
        when(userRepository.findByEmail("email")).thenReturn(new User("email", "password"));
        assertThrows(ResponseStatusException.class, () -> 
        {
            userService.authenticateUser(header);
        });
    }

	@Test
	public void testAddFriend_SuccessfullyAddsEmailToFriendList()
	{
		User user = new User("email", "password");
		user.setFriendList(new ArrayList<>());
		userService.addFriend(user, "friendEmail");
		assertTrue(user.getFriendList().contains("friendEmail"));
	}
	
	@Test
	public void testAddFriend_DoesNotAddSameEmailAddressToFriendLiist_IfDuplicateEmailIsAdded()
	{
	    User user = new User("email", "password");
	    user.setFriendList(new ArrayList<>(Arrays.asList("friendEmail1")));
	    when(userRepository.findByEmail("email")).thenReturn(user);
	    userService.addFriend(user, "friendEmail1"); // Try adding the same friend email again

	    List<String> friendList = user.getFriendList();
	    int occurrences = Collections.frequency(friendList, "friendEmail1");
	    assertEquals(1, occurrences); // Should only contain one occurrence of "friendEmail1"
	}


	@Test
	public void testDeleteFriend_RemovesEMailFromFriendList()
	{
		User user = new User("email", "password");
		user.setFriendList(new ArrayList<>(Arrays.asList("friendEmail")));
		userService.deleteFriend(user, "friendEmail");
		assertFalse(user.getFriendList().contains("friendEmail"));
	}

	@Test
	public void testAddCustomStatus_AddsCustomStatus() throws InvalidAvailabilityException
	{
		User user = new User("email", "password");
		user.setCustomStatuses(new ArrayList<>());
		Status customStatus = new Status(user, "statusName", Availability.Away, "message");
		when(statusService.createCustomStatus(user, "statusName", "Away", "message")).thenReturn(customStatus);
		userService.addCustomStatus(user, "statusName", "Away", "message");
		assertTrue(user.getCustomStatuses().contains(customStatus));
	}

	@Test
	public void testAddCustomStatus_ThrowsException_WhenInvalidAvailabilityIsGiven() throws InvalidAvailabilityException
	{
		User user = new User("email", "password");
		when(statusService.createCustomStatus(user, "statusName", "invalidAvailability", "message"))
				.thenThrow(new InvalidAvailabilityException("Invalid Availability"));
		assertThrows(InvalidAvailabilityException.class, () ->
		{
			userService.addCustomStatus(user, "statusName", "invalidAvailability", "message");
		});
	}

	@Test
	public void testUserIsOnOtherFriendList_ReturnsTrue_WhenEmailIsOnFriendList()
	{
		User user = new User("email", "password");
		User friendUser = new User("friendEmail", "password");
		friendUser.setFriendList(new ArrayList<>(Arrays.asList("email")));
		when(userRepository.findByEmail("friendEmail")).thenReturn(friendUser);
		assertTrue(userService.userIsOnOtherFriendList(user, "friendEmail"));
	}

	@Test
	public void testUserIsOnOtherFriendList_ReturnsFalse_WhenEmailIsNotOnFriendList()
	{
		User user = new User("email", "password");
		User friendUser = new User("friendEmail", "password");
		friendUser.setFriendList(new ArrayList<>());
		when(userRepository.findByEmail("friendEmail")).thenReturn(friendUser);
		assertFalse(userService.userIsOnOtherFriendList(user, "friendEmail"));
	}

	@Test
	public void testUserIsOnOtherFriendList_ThrowsException_WhenFriendEmailIsNotRegistered()
	{
		User user = new User("email", "password");
		when(userRepository.findByEmail("nonexistentfriend")).thenReturn(null);
		assertThrows(ResponseStatusException.class, () ->
		{
			userService.userIsOnOtherFriendList(user, "nonexistentfriend");
		});
	}
}
