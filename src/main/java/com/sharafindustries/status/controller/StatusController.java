package com.sharafindustries.status.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.sharafindustries.status.model.User;
import com.sharafindustries.status.model.UserStatusInfo;
import com.sharafindustries.status.service.UserService;

@CrossOrigin(origins = "http://localhost:9090")
@RestController
public class StatusController
{
	@Autowired
	private UserService userService;
	
	private static final Logger logger = LoggerFactory.getLogger(StatusController.class);

	
	@PostMapping("/google-token-test")
	public ResponseEntity<String> testGoogleToken(HttpServletRequest request, @RequestBody String token) throws IOException
	{
		logger.info("query string is {}", request.getQueryString());
		logger.info("headers are {}", request.getHeaderNames());
//		logger.info("request body is {}", request.getReader().lines().collect(Collectors.toList()));
		logger.info("received token is {}", token);
//		boolean success = userService.verifyGoogleToken(token);
//		if (success)
			return ResponseEntity.ok().build();
//		else
//			return ResponseEntity.badRequest().build();
	}
	
	@PostMapping("/authenticate-user")
	public ResponseEntity<String> areCredentialsValid(@RequestHeader("Authorization") String authorizationHeader)
	{
		userService.authenticateUser(authorizationHeader);
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/get-status")
	public UserStatusInfo getStatus(@RequestHeader("Authorization") String authorizationHeader, @RequestParam(value = "email") String friendEmail)
	{
		logger.info("received /get-status GET request");
		User user = userService.authenticateUser(authorizationHeader);
		logger.info("user: {}", user.getEmail());
		if (userService.canUserViewStatus(user, friendEmail))
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
		logger.info("received /create-custom-status POST request");
		User user = userService.authenticateUser(authorizationHeader);
		logger.info("user: {}", user.getEmail());
		userService.addCustomStatus(user, statusName, availability, message);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@PostMapping("/add-friend")
	public ResponseEntity<String> addFriend(@RequestHeader("Authorization") String authorizationHeader, @RequestParam(value = "emailToAdd") String friendEmailToAdd)
	{
		logger.info("received /add-friend POST request");
		User user = userService.authenticateUser(authorizationHeader);
		logger.info("user: {}", user.getEmail());
		userService.addFriend(user, friendEmailToAdd);
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/delete-friend")
	public ResponseEntity<String> deleteFriend(@RequestHeader("Authorization") String authorizationHeader, @RequestParam(value = "emailToDelete") String friendEmailToDelete)
	{
		logger.info("received /delete-friend POST request");
		User user = userService.authenticateUser(authorizationHeader);
		logger.info("user: {}", user.getEmail());
		userService.deleteFriend(user, friendEmailToDelete);
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/my-friends")
	public List<String> getFriendList(@RequestHeader("Authorization") String authorizationHeader)
	{
		logger.info("received /my-friends GET request");
		User user = userService.authenticateUser(authorizationHeader);
		logger.info("user: {}", user.getEmail());
		return user.getFriendList();
	}
	
	@GetMapping("/my-statuses")
	public List<UserStatusInfo> getUserCustomStatuses(@RequestHeader("Authorization") String authorizationHeader)
	{
		logger.info("received /my-statuses GET request");
		User user = userService.authenticateUser(authorizationHeader);
		logger.info("user: {}", user.getEmail());
		return userService.getCustomStatusInfo(user);
	}
	
	@PostMapping("/set-status")
	public String setCurrentStatus(@RequestHeader("Authorization") String authorizationHeader, @RequestParam String statusName)
	{
		logger.info("received /set-status POST request");
		User user = userService.authenticateUser(authorizationHeader);
		logger.info("user: {}", user.getEmail());
		userService.setCurrentStatus(user, statusName);
		return "status set";
	}
	
	@PostMapping("/create-user")
	public ResponseEntity<String> createUser(@RequestParam(value = "email") String userEmail, @RequestParam String password)
	{
		logger.info("received /create-user POST request for email {}", userEmail);
		userService.createAndSaveNewUser(userEmail, password);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@PostMapping("/delete-custom-status")
	public ResponseEntity<String> deleteCustomStatus(@RequestHeader("Authorization") String authorizationHeader, @RequestParam String statusName)
	{
		logger.info("received /delete-custom-status POST request");
		User user = userService.authenticateUser(authorizationHeader);
		logger.info("user: {}", user.getEmail());
		userService.deleteCustomStatus(user, statusName);
		return ResponseEntity.ok().build();
	}

}
