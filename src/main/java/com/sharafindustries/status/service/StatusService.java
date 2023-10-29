package com.sharafindustries.status.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.sharafindustries.status.model.Availability;
import com.sharafindustries.status.model.Status;
import com.sharafindustries.status.model.User;
import com.sharafindustries.status.model.UserStatusInfo;
import com.sharafindustries.status.repository.StatusRepository;

@Service
public class StatusService
{
	@Autowired
	private StatusRepository statusRepository;

	
	/**
	 * Creates a custom status for a user. The status cannot have the name Available, Away, or Busy. Additionally, the availability of the status 
	 * must be Available, Away, or Busy.
	 * 
	 * @param user the user to create the status for
	 * @param name the name of the status
	 * @param availability the {@link Availability} of the status
	 * @param message the custom message for this status 
	 * @return the newly created Status object
	 * @throws ResponseStatusException with HttpStatus.BAD_REQUEST if the name is Available, Away, or Busy or if the {@link Availability} is invalid,
	 * @throws ResponseStatusException with HttpStatus.CONFLICT if the name is already in use
	 */
	public Status createCustomStatus(User user, String name, String availability, String message)
	{
		if (Availability.isValid(name))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name cannot be Available, Away, or Busy");
		
		boolean isNameAlreadyUsed = Objects.nonNull(statusRepository.getStatusByNameAndUser(name, user));
		if (isNameAlreadyUsed)
			throw new ResponseStatusException(HttpStatus.CONFLICT, "name is already in use");
		if (Availability.isValid(availability))
			return new Status(user, name, Availability.valueOf(availability), message);
		else
		{
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, availability + " is not a valid Availability");
		}
	}
	
	/**
	 * Gets a status for a user. Can return either the default Available, Away, or Busy statuses, or a custom one.
	 * 
	 * @param name the name of the status
	 * @param user the user to get the status for
	 * @return the requested status
	 * @throws ResponseStatusException with HttpStatus.NOT_FOUND if no status with that name is found
	 */
	public Status getStatusForUser(String name, User user)
	{
		if (Availability.isValid(name))
		{
			Availability availability = Availability.valueOf(name);
			switch (availability)
			{
				case Available:
					return Status.DEFAULT_AVAILABLE_STATUS;
				case Away:
					return Status.DEFAULT_AWAY_STATUS;
				case Busy:
					return Status.DEFAULT_BUSY_STATUS;
			}
		}
		Status status = statusRepository.getStatusByNameAndUser(name, user);
		if (status == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "no status with that name was found");
		return status;
		
	}

	/**
	 * Saves the given status.
	 * 
	 * @param status the status to save
	 */
	public void saveStatus(Status status)
	{
		statusRepository.save(status);
	}
	
	/**
	 * Deletes a custom status.
	 * 
	 * @param user the user to delete the status for
	 * @param name the name of the status to delete
	 * @throws ResponseStatusException with HttpStatus.BAD_REQUEST if the status name is Available, Away, or Busy, or if the status doesn't exist.
	 */
	public void deleteCustomStatus(User user, String name)
	{
		if (Availability.isValid(name))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot delete status " + name);
		Status status = statusRepository.getStatusByNameAndUser(name, user);
		if (status == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status with name " + name + " does not exist");
		statusRepository.delete(status);
	}
	
	/**
	 * Returns the user's custom statuses as a {@link UserStatusInfo} List.
	 * 
	 * @param user the user for whom to retrieve the custom statuses
	 * @return a {@link UserStatusInfo} List
	 */
	public List<UserStatusInfo> getCustomStatusInfo(User user)
	{
		List<Status> customStatuses = user.getCustomStatuses();
		List<UserStatusInfo> customStatusInfoList = customStatuses.stream().map(status -> toUserStatusInfo(status)).collect(Collectors.toList());
		return customStatusInfoList;
	}
	
	/**
	 * Maps a Status to a {@link UserStatusInfo} object.
	 * 
	 * @param status the staus to transform
	 * @return the {@link UserStatusInfo} representation
	 */
	private UserStatusInfo toUserStatusInfo(Status status)
	{
		return new UserStatusInfo(status.getName(), status.getAvailability().toString(), status.getMessage());
	}
}
