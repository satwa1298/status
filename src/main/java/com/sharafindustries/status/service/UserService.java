package com.sharafindustries.status.service;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.sharafindustries.status.exception.InvalidAvailabilityException;
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
	
	public User createAndSaveNewUser(String email, String password)
	{
		User existingUser = userRepository.findByEmail(email);
		if (existingUser != null)
			throw new ResponseStatusException(HttpStatus.CONFLICT, "A user with that email is already registered");
		User newUser = context.getBean(User.class, email, password);
		return userRepository.save(newUser);
	}
	
	public User getUserByEmail(String email)
	{
		return userRepository.findByEmail(email);
	}
	
	public User authenticateUser(String authorizationHeader)
	{
		if (authorizationHeader == null || !authorizationHeader.startsWith("Basic"))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
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
	
	public void addFriend(User user, String friendEmailToAdd)
	{
		List<String> friendList = user.getFriendList();;
		if (!friendList.contains(friendEmailToAdd))
		{
			friendList.add(friendEmailToAdd);
			userRepository.save(user);
		}
	}
	
	public void deleteFriend(User user, String friendEmailToRemove)
	{
		user.getFriendList().remove(friendEmailToRemove);
		userRepository.save(user);
	}
	
	public void addCustomStatus(User user, String statusName, String availability, String message)
	{
		user.getCustomStatuses().add(statusService.createCustomStatus(user, statusName, availability, message));
		userRepository.save(user);
	}
	
	public boolean userIsOnOtherFriendList(User user, String maybeFriendEmail)
	{
		if (user.getEmail().equals(maybeFriendEmail))
			return true;
		User friendUser = userRepository.findByEmail(maybeFriendEmail);
		if (friendUser == null)
		{
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "that user is not registered");
		}
		return friendUser.getFriendList().contains(user.getEmail());
	}
	
	public Availability getCurrentAvailability(String userEmail)
	{
		return userRepository.findByEmail(userEmail).getCurrentAvailability();
	}
	
	public String getCurrentStatusMessage(String userEmail)
	{
		return userRepository.findByEmail(userEmail).getCurrentMessage();
	}
	
	public List<Status> getCustomStatuses(User user)
	{
		return user.getCustomStatuses();
	}
	
	public boolean setCurrentStatus(User user, String statusName)
	{
		Status status = statusService.getStatusForUser(statusName, user);
		user.setCurrentAvailability(status.getAvailability());
		user.setCurrentMessage(status.getMessage());
		userRepository.save(user);
		return true;
	}
	
	public UserStatusInfo getUserStatusInfo(User user)
	{
		return new UserStatusInfo(user.getCurrentAvailability().toString(), user.getCurrentMessage());
	}
	
	public void deleteCustomStatus(User user, String statusName)
	{
		statusService.deleteCustomStatus(user, statusName);
	}
	
}
