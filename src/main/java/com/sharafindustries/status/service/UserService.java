package com.sharafindustries.status.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.sharafindustries.status.exception.InvalidAvailabilityException;
import com.sharafindustries.status.model.Status;
import com.sharafindustries.status.model.User;
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
	
	public User createAndSaveNewUser(String email)
	{
		User newUser = context.getBean(User.class, email);
		return userRepository.save(newUser);
	}
	
	public void addPermittedUser(String userEmail, String permittedEmail)
	{
		User user = userRepository.findByEmail(userEmail);
		user.getPermittedUsers().add(permittedEmail);
		userRepository.save(user);
	}
	
	public void removePermittedUser(String userEmail, String emailToRemove)
	{
		User user = userRepository.findByEmail(userEmail);
		user.getPermittedUsers().remove(emailToRemove);
		userRepository.save(user);
	}
	
	public void addCustomStatus(String userEmail, String name, String availability, String message) throws InvalidAvailabilityException
	{
		User user = userRepository.findByEmail(userEmail);
		user.getStatuses().add(statusService.createAndSaveCustomStatus(name, availability, message));
		userRepository.save(user);
	}
	
	public boolean checkIfUserIsPermitted(String callerEmail, String email)
	{
		User userStatusInQuestion = userRepository.findByEmail(email);
		return userStatusInQuestion.getPermittedUsers().contains(callerEmail) || callerEmail.equals(email);
	}
	
	public Status getCurrentStatus(String userEmail)
	{
		return userRepository.findByEmail(userEmail).getCurrentStatus();
	}
	
	public List<Status> getAllStatuses(String userEmail)
	{
		return userRepository.findByEmail(userEmail).getStatuses();
	}
	
	public boolean setCurrentStatus(String userEmail, String statusName)
	{
		User user = userRepository.findByEmail(userEmail);
		List<Status> statuses = user.getStatuses();
		Optional<Status> desiredStatusOptional = statuses.stream().filter(status -> status.getName().equals(statusName)).findAny();
		if (desiredStatusOptional.isPresent())
		{
			user.setCurrentStatus(desiredStatusOptional.get());
			userRepository.save(user);
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public void addStatusAndUpdateUser(User user, Status status)
	{
		user.getStatuses().add(status);
		userRepository.save(user);
	}
	
	public void createAndSetStatus(String callerEmail, String name, String availability, String message) throws InvalidAvailabilityException
	{
		User user = userRepository.findByEmail(callerEmail);
		//TODO fix this, reconcile with addCustomStatus() above
		Status status = statusService.createAndSaveCustomStatus(name, availability, message);
		//TODO this is messy. status is added to statuses list and user is saved then user is saved again
		addStatusAndUpdateUser(user, status);
		user.setCurrentStatus(status);
		userRepository.save(user);
	}

}
