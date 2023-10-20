package com.sharafindustries.status.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.sharafindustries.status.exception.InvalidAvailabilityException;
import com.sharafindustries.status.model.Status;
import com.sharafindustries.status.model.User;
import com.sharafindustries.status.model.UserStatusInfo;
import com.sharafindustries.status.service.UserService;

@RestController
public class StatusController
{
	@Autowired
	private UserService userService;
	
	@GetMapping("/get-status")
	public UserStatusInfo getStatus(@RequestHeader("Authorization") String authorizationHeader, @RequestParam(value = "email") String friendEmail)
	{
		User user = userService.authenticateUser(authorizationHeader);
		//TODO its currently unclear that whats actually being checked is whether the friend has the caller on their friend list
		if (userService.isFriend(user, friendEmail))
		{
			return userService.getUserStatusInfo(userService.getUserByEmail(friendEmail));
		}
		else
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to view this person's status");
	}
	
	@PostMapping("/create-custom-status")
	public ResponseEntity<Void> addCustomStatus(@RequestHeader("Authorization") String authorizationHeader, 
			@RequestParam(value = "statusName") String statusName, 
			@RequestParam(value = "availability") String availability, 
			@RequestParam(value = "message") String message)
	{
		User user = userService.authenticateUser(authorizationHeader);
		try
		{
			userService.addCustomStatus(user, statusName, availability, message);
		}
		catch (InvalidAvailabilityException e)
		{
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@PostMapping("/add-friend")
	public ResponseEntity<User> addFriend(@RequestHeader("Authorization") String authorizationHeader, @RequestParam(value = "emailToAdd") String friendEmailToAdd)
	{
		User user = userService.authenticateUser(authorizationHeader);
		userService.addFriend(user, friendEmailToAdd);
		return ResponseEntity.ok().build();
	}
	
	//TODO add mapping for removing permitted user
	@PostMapping("/delete-friend")
	public ResponseEntity<User> deleteFriend(@RequestHeader("Authorization") String authorizationHeader, @RequestParam(value = "emailToDelete") String friendEmailToDelete)
	{
		User user = userService.authenticateUser(authorizationHeader);
		userService.deleteFriend(user, friendEmailToDelete);
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/my-statuses")
	public List<Status> getUserCustomStatuses(@RequestHeader("Authorization") String authorizationHeader)
	{
		User user = userService.authenticateUser(authorizationHeader);
		return userService.getCustomStatuses(user);
	}
	
	@PostMapping("/set-status")
	public String setCurrentStatus(@RequestHeader("Authorization") String authorizationHeader, @RequestParam String statusName)
	{
		User user = userService.authenticateUser(authorizationHeader);
		userService.setCurrentStatus(user, statusName);
		return "status set";
	}
	
	@PostMapping("/create-user")
	public ResponseEntity<User> createUser(@RequestParam(value = "email") String userEmail, @RequestParam String password)
	{
		userService.createAndSaveNewUser(userEmail, password);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

}
