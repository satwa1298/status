package com.sharafindustries.status.service;

import java.util.ArrayList;
import java.util.List;

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
	// TODO these need to be saved. maybe have these 3 accessible to everyone and
	// field in user is only custom statuses?
	// TODO also maybe add name field
	public static final String DEFAULT_AVAILABLE_MESSAGE = "I'm available right now!";
	public static final String DEFAULT_AWAY_MESSAGE = "I'm away right now.";
	public static final String DEFAULT_BUSY_MESSAGE = "Sorry, I'm busy right now.";

	public List<Status> getDefaultStatuses(String email)
	{
		Status defaultAvailableStatus = new Status(null, "AVAILABLE", Availability.Available, DEFAULT_AVAILABLE_MESSAGE);
		Status defaultAwayStatus = new Status(null, "AWAY", Availability.Away, DEFAULT_AWAY_MESSAGE);
		Status defaultBusyStatus = new Status(null, "BUSY", Availability.Busy, DEFAULT_BUSY_MESSAGE);
		statusRepository.save(defaultAvailableStatus);
		statusRepository.save(defaultAwayStatus);
		statusRepository.save(defaultBusyStatus);
		List<Status> defaultStatuses = new ArrayList<Status>();
		defaultStatuses.add(defaultAvailableStatus);
		defaultStatuses.add(defaultAwayStatus);
		defaultStatuses.add(defaultBusyStatus);
		return defaultStatuses;
	}

	public Status createCustomStatus(User user, String name, String availability, String message) throws InvalidAvailabilityException
	{
		if (Availability.isValid(availability))
			return new Status(user, name, Availability.valueOf(availability), message);
//			return statusRepository.save(new Status(prependEmail(email, name), Availability.valueOf(availability), message));
		// return statusRepository.save(new Status(name,
		// Availability.valueOf(availability), message));
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
