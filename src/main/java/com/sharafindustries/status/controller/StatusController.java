package com.sharafindustries.status.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.sharafindustries.status.model.Status;
import com.sharafindustries.status.model.User;
import com.sharafindustries.status.model.UserStatusInfo;
import com.sharafindustries.status.service.UserService;

@CrossOrigin(origins = "http://localhost:9090")
@RestController
public class StatusController
{
	@Autowired
	private UserService userService;
	
	@PostMapping("/authenticate-user")
	public ResponseEntity<String> areCredentialsValid(@RequestHeader("Authorization") String authorizationHeader)
	{
		System.out.println("auth request received");
		userService.authenticateUser(authorizationHeader);
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/get-status")
	public UserStatusInfo getStatus(@RequestHeader("Authorization") String authorizationHeader, @RequestParam(value = "email") String friendEmail)
	{
		User user = userService.authenticateUser(authorizationHeader);
		//TODO its currently unclear that whats actually being checked is whether the friend has the caller on their friend list
		if (userService.userIsOnOtherFriendList(user, friendEmail))
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
		userService.addCustomStatus(user, statusName, availability, message);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@PostMapping("/add-friend")
	public ResponseEntity<String> addFriend(@RequestHeader("Authorization") String authorizationHeader, @RequestParam(value = "emailToAdd") String friendEmailToAdd)
	{
		User user = userService.authenticateUser(authorizationHeader);
		userService.addFriend(user, friendEmailToAdd);
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/delete-friend")
	public ResponseEntity<String> deleteFriend(@RequestHeader("Authorization") String authorizationHeader, @RequestParam(value = "emailToDelete") String friendEmailToDelete)
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
	public ResponseEntity<String> createUser(@RequestParam(value = "email") String userEmail, @RequestParam String password)
	{
		userService.createAndSaveNewUser(userEmail, password);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@PostMapping("/delete-custom-status")
	public ResponseEntity<String> deleteCustomStatus(@RequestHeader("Authorization") String authorizationHeader, @RequestParam String statusName)
	{
		User user = userService.authenticateUser(authorizationHeader);
		userService.deleteCustomStatus(user, statusName);
		return ResponseEntity.ok().build();
	}

}
