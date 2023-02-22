package com.sharafindustries.status.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.sharafindustries.status.exception.InvalidAvailabilityException;
import com.sharafindustries.status.model.Status;
import com.sharafindustries.status.service.UserService;

@RestController
public class StatusController
{
	@Autowired
	private UserService userService;
	
	@GetMapping("/get-status")
	public Status getStatus(@RequestParam(value = "caller") String callerEmail, @RequestParam(value = "email") String emailRequested, HttpServletResponse response)
	{
		
		if (userService.checkIfUserIsPermitted(callerEmail, emailRequested))
			return userService.getCurrentStatus(emailRequested);
		else
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to view this person's status");
	}
	
	@PostMapping("/add-status")
	public String addCustomStatus(@RequestParam(value = "caller") String callerEmail, 
			@RequestParam(value = "name") String name, 
			@RequestParam(value = "availability") String availability, 
			@RequestParam(value = "message") String message)
	{
		try
		{
			userService.addCustomStatus(callerEmail, name, availability, message);
		}
		catch (InvalidAvailabilityException e)
		{
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		return "status added";
	}
	
	@PostMapping("/add-permitted-user")
	public String addPermittedUser(@RequestParam(value = "caller") String callerEmail, @RequestParam(value = "emailToAdd") String emailToAdd)
	{
		userService.addPermittedUser(callerEmail, emailToAdd);
		return "user added";
	}
	
	//TODO add mapping for removing permitted user
	
	@GetMapping("/my-statuses")
	public List<Status> getAllStatuses(@RequestParam(value = "caller") String callerEmail)
	{
		return userService.getAllStatuses(callerEmail);
	}
	
	@PostMapping("/set-status")
	public String setCurrentStatus(@RequestParam(value = "caller") String callerEmail,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "availability", required = false) String availability,
			@RequestParam(value = "message", required = false) String message)
	{
		if (name == null && (availability == null || message == null))
		{
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "must include status name or availability and message in request");
		}
		if (name != null)
		{
			if (!userService.setCurrentStatus(callerEmail, name))
				try
				{
					userService.createAndSetStatus(callerEmail, name, availability, message);
				}
				catch (InvalidAvailabilityException e)
				{
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
				}
		}
			
			
		return "status set";
	}
	
	@PostMapping("/create-user")
	public String createUser(@RequestParam(value = "apiKey") String apiKey, @RequestParam(value = "email") String userEmail)
	{
		if (apiKey.equals("genericApiKey"))
		{
			userService.createAndSaveNewUser(userEmail);
			return "success";
		}
		else
		{
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "no");
		}
	}

}
