package com.sharafindustries.status.service;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.sharafindustries.status.exception.InvalidAvailabilityException;
import com.sharafindustries.status.model.Availability;
import com.sharafindustries.status.model.Status;
import com.sharafindustries.status.model.User;
import com.sharafindustries.status.repository.StatusRepository;

@Service
public class StatusService
{
	@Autowired
	private StatusRepository statusRepository;

	public Status createCustomStatus(User user, String name, String availability, String message) throws InvalidAvailabilityException
	{
		boolean isNameAlreadyUsed = Objects.nonNull(statusRepository.getStatusByNameAndUser(name, user));
		if (isNameAlreadyUsed)
			throw new ResponseStatusException(HttpStatus.CONFLICT, "name is already in use");
		if (Availability.isValid(availability))
			return new Status(user, name, Availability.valueOf(availability), message);
		else
		{
			throw new InvalidAvailabilityException(availability + " is not a valid Availability");
		}
	}
	
	public Status getStatusForUser(String name, User user)
	{
		Status status = statusRepository.getStatusByNameAndUser(name, user);
		if (status == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "no status with that name was found");
		return status;
	}

	public void saveStatus(Status status)
	{
		statusRepository.save(status);
	}
}
