package com.sharafindustries.status.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

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
		newUser.setStatuses(statusService.getDefaultStatuses());
		newUser.setCurrentStatus(StatusService.DEFAULT_AVAILABLE_STATUS);
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
	
	public void addCustomStatus(String userEmail, String availability, String message)
	{
		User user = userRepository.findByEmail(userEmail);
		user.getStatuses().add(statusService.createCustomStatus(availability, message));
		userRepository.save(user);
	}
	
	public boolean checkIfUserIsPermitted(String callerEmail, String email)
	{
		User userStatusInQuestion = userRepository.findByEmail(email);
		return userStatusInQuestion.getPermittedUsers().contains(callerEmail);
	}
	
	public Status getCurrentStatus(String userEmail)
	{
		return userRepository.findByEmail(userEmail).getCurrentStatus();
	}
	
	public List<Status> getAllStatuses(String userEmail)
	{
		return userRepository.findByEmail(userEmail).getStatuses();
	}
	
	public void setCurrentStatus(String userEmail, int statusId)
	{
		User user = userRepository.findByEmail(userEmail);
		List<Status> statuses = user.getStatuses();
		Optional<Status> desiredStatusOptional = statuses.stream().filter(status -> status.getId() == statusId).findAny();
		if (desiredStatusOptional.isPresent())
		{
			user.setCurrentStatus(desiredStatusOptional.get());
			userRepository.save(user);
		}
		else
		{
			//TODO throw exception here or something
		}
	}

}
