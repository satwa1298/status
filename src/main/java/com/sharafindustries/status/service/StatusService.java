package com.sharafindustries.status.service;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.sharafindustries.status.model.Availability;
import com.sharafindustries.status.model.Status;
import com.sharafindustries.status.model.User;
import com.sharafindustries.status.repository.StatusRepository;

@Service
public class StatusService
{
	@Autowired
	private StatusRepository statusRepository;

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

	public void saveStatus(Status status)
	{
		statusRepository.save(status);
	}
	
	public void deleteCustomStatus(User user, String name)
	{
		if (Availability.isValid(name))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot delete status " + name);
		Status status = statusRepository.getStatusByNameAndUser(name, user);
		if (status == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status with name " + name + " does not exist");
		statusRepository.delete(status);
	}
}
