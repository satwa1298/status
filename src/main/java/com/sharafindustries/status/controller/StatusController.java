package com.sharafindustries.status.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.sharafindustries.status.model.Status;
import com.sharafindustries.status.service.UserService;

import jakarta.servlet.http.HttpServletResponse;

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
	
	@PostMapping("/add-custom-status")
	public String addCustomStatus(@RequestParam(value = "caller") String callerEmail, @RequestParam(value = "availability") String availability, @RequestParam(value = "message") String message)
	{
		userService.addCustomStatus(callerEmail, availability, message);
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
	
	@PostMapping("/set-current-status")
	public String setCurrentStatus(@RequestParam(value = "caller") String callerEmail, @RequestParam(value = "statusId") int statusId)
	{
		userService.setCurrentStatus(callerEmail, statusId);
		return "might have worked, might have not";
	}

}
