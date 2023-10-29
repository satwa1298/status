package com.sharafindustries.status.service;

import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.sharafindustries.status.model.Availability;
import com.sharafindustries.status.model.Status;
import com.sharafindustries.status.model.User;
import com.sharafindustries.status.model.UserStatusInfo;
import com.sharafindustries.status.repository.UserRepository;

@Service
public class UserService
{
	@Autowired
	private ApplicationContext context;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private StatusService statusService;
	
	/**
	 * Creates and saves a new user.
	 * 
	 * @param email the email address of the user
	 * @param password the password of the user
	 * @return the newly created {@link User}
	 * @throws ResponseStatusException with HttpStatus.CONFLICT if a user with that email already exists
	 */
	public User createAndSaveNewUser(String email, String password)
	{
		User existingUser = userRepository.findByEmail(email);
		if (existingUser != null)
			throw new ResponseStatusException(HttpStatus.CONFLICT, "A user with that email is already registered");
		User newUser = context.getBean(User.class, email, password);
		return userRepository.save(newUser);
	}
	
	/**
	 * Finds a user by their email.
	 * 
	 * @param email the email address of the user
	 * @return the user, or null if that user is not registered
	 */
	public User getUserByEmail(String email)
	{
		return userRepository.findByEmail(email);
	}
	
	/**
	 * Authenticates a user using BasicAuth. If successful the user is returned. If not, an exception is thrown. Used to authenticate any requests made to the API.
	 * 
	 * @param authorizationHeader the BasicAuth header
	 * @return the authenticated User
	 * @throws ResponseStatusException with HttpStatus.UNAUTHORIZED if the header is invalid or if authentication is not successful
	 */
	public User authenticateUser(String authorizationHeader)
	{
		if (authorizationHeader == null || !authorizationHeader.startsWith("Basic"))
		{
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}
		String base64Credentials = authorizationHeader.substring("Basic ".length());
		String decodedCredentials = new String(Base64.getDecoder().decode(base64Credentials));
		String[] values = decodedCredentials.split(":");
		String email = values[0];
		String password = values[1];
		
		User user = userRepository.findByEmail(email);
		if (user == null || !user.getPassword().equals(password))
		{
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}
		return user;
	}
	
	/**
	 * Adds a friend to a user's friend list, meaning that the added friend can check on the status of this user.
	 * 
	 * @param user the user adding a friend
	 * @param friendEmailToAdd the email of the friend to add to the user's friend list
	 */
	public void addFriend(User user, String friendEmailToAdd)
	{
		List<String> friendList = user.getFriendList();;
		if (!friendList.contains(friendEmailToAdd))
		{
			friendList.add(friendEmailToAdd);
			userRepository.save(user);
		}
	}
	
	/**
	 * Deletes a friend from a user's friend list.
	 * 
	 * @param user the user deleting the friend
	 * @param friendEmailToRemove the email of the friend to delete
	 */
	public void deleteFriend(User user, String friendEmailToRemove)
	{
		user.getFriendList().remove(friendEmailToRemove);
		userRepository.save(user);
	}
	
	/**
	 * Creates a custom status for a user.
	 * 
	 * @param user the user creating the status
	 * @param statusName the name of the status
	 * @param availability the {@link Availability} of the status
	 * @param message the message for the status
	 * @throws ResponseStatusException see {@link StatusService#createCustomStatus(User, String, String, String)}
	 */
	public void addCustomStatus(User user, String statusName, String availability, String message)
	{
		user.getCustomStatuses().add(statusService.createCustomStatus(user, statusName, availability, message));
		userRepository.save(user);
	}
	
	/**
	 * Checks whether the requesting user is on the friend list of another user in order to authorize viewing the status of the other user. 
	 * 
	 * @param requestingUser the user requesting to see another's status
	 * @param maybeFriendEmail the email of the one who's status is being requested
	 * @return true if the requesting user is on the other's friend list, false otherwise
	 * @throws ResponseStatusException with HttpStatus.BAD_REQUEST if the email whose status is requested is not registered
	 */
	public boolean canUserViewStatus(User requestingUser, String maybeFriendEmail)
	{
		if (requestingUser.getEmail().equals(maybeFriendEmail))
			return true;
		User friendUser = userRepository.findByEmail(maybeFriendEmail);
		if (friendUser == null)
		{
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "that user is not registered");
		}
		return friendUser.getFriendList().contains(requestingUser.getEmail());
	}
	
	/**
	 * Returns a List of the user's custom statuses
	 * 
	 * @param user the user
	 * @return a list of the user's custom statuses
	 */
	public List<UserStatusInfo> getCustomStatusInfo(User user)
	{
		return statusService.getCustomStatusInfo(user);
	}
	
	/**
	 * Sets the current status of the user.
	 * 
	 * @param user the user
	 * @param statusName the name of the status to set
	 * @throws ResponseStatusException see {@link StatusService#getStatusForUser(String, User)}
	 */
	public void setCurrentStatus(User user, String statusName)
	{
		Status status = statusService.getStatusForUser(statusName, user);
		user.setCurrentAvailability(status.getAvailability());
		user.setCurrentMessage(status.getMessage());
		userRepository.save(user);
	}
	
	/**
	 * Returns the current status of the user as a {@link UserStatusInfo} object.
	 * 
	 * @param user the user
	 * @return the user's current status
	 */
	public UserStatusInfo getUserStatusInfo(User user)
	{
		
		return new UserStatusInfo(user.getCurrentStatusName(), user.getCurrentAvailability().toString(), user.getCurrentMessage());
	}
	
	/**
	 * Deletes a custom status for a user.
	 * 
	 * @param user the user
	 * @param statusName the name of the status
	 * @throws ResponseStatusException see {@link StatusService#deleteCustomStatus(User, String)}
	 */
	public void deleteCustomStatus(User user, String statusName)
	{
		statusService.deleteCustomStatus(user, statusName);
	}
	
}
