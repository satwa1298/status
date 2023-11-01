package com.sharafindustries.status.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.sharafindustries.status.model.User;
import com.sharafindustries.status.model.UserStatusInfo;
import com.sharafindustries.status.service.UserService;

@RestController
public class StatusController
{
	//TODO handle returning errors better. instead of throwing exceptions return status codes
	//TODO have a filter on all incoming requests to authenticate user
	//TODO error handling for if required attributes are missing from request body
	
	@Autowired
	private UserService userService;
	
	private static final Logger logger = LoggerFactory.getLogger(StatusController.class);

	
	@GetMapping("/does-account-exist")
	public boolean doesAccountExist(@RequestHeader("Authorization") String authorizationHeader)
	{
		try
		{
			userService.authenticateUser(authorizationHeader);
			return true;
		}
		catch (ResponseStatusException e)
		{
			if (e.getStatus() == HttpStatus.BAD_REQUEST) //token is invalid
				throw e;
			return false;
		}
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
	public ResponseEntity<Void> addCustomStatus(@RequestHeader("Authorization") String authorizationHeader, @RequestBody Map<String, String> body)
	{
		logger.info("received /create-custom-status POST request");
		User user = userService.authenticateUser(authorizationHeader);
		logger.info("user: {}", user.getEmail());
		userService.addCustomStatus(user, body.get("statusName"), body.get("availability"), body.get("message"));
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@PostMapping("/add-friend")
	public ResponseEntity<String> addFriend(@RequestHeader("Authorization") String authorizationHeader, @RequestBody Map<String, String> body)
	{
		logger.info("received /add-friend POST request");
		User user = userService.authenticateUser(authorizationHeader);
		logger.info("user: {}", user.getEmail());
		userService.addFriend(user, body.get("emailToAdd"));
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/delete-friend")
	public ResponseEntity<String> deleteFriend(@RequestHeader("Authorization") String authorizationHeader, @RequestBody Map<String, String> body)
	{
		logger.info("received /delete-friend POST request");
		User user = userService.authenticateUser(authorizationHeader);
		logger.info("user: {}", user.getEmail());
		userService.deleteFriend(user, body.get("emailToDelete"));
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
	public String setCurrentStatus(@RequestHeader("Authorization") String authorizationHeader, @RequestBody Map<String, String> body)
	{
		logger.info("received /set-status POST request");
		User user = userService.authenticateUser(authorizationHeader);
		logger.info("user: {}", user.getEmail());
		userService.setCurrentStatus(user, body.get("statusName"));
		return "status set";
	}
	
	@PostMapping("/create-user")
	public ResponseEntity<String> createUser(@RequestHeader("Authorization") String authorizationHeader)
	{
		userService.createAndSaveNewUser(authorizationHeader);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@PostMapping("/delete-custom-status")
	public ResponseEntity<String> deleteCustomStatus(@RequestHeader("Authorization") String authorizationHeader, @RequestBody Map<String, String> body)
	{
		logger.info("received /delete-custom-status POST request");
		User user = userService.authenticateUser(authorizationHeader);
		logger.info("user: {}", user.getEmail());
		userService.deleteCustomStatus(user, body.get("statusName"));
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/get-passphrase")
	public String getPassphrase(@RequestHeader("Authorization") String authorizationHeader)
	{
		User user = userService.authenticateUser(authorizationHeader);
		logger.info("passphrase is {}", user.getPassphrase());
		return user.getPassphrase();
	}
	
	@GetMapping("/brew-coffee")
	public ResponseEntity<String> brewCoffee()
	{
		return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
	}

}
