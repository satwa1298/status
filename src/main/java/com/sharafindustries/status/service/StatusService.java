package com.sharafindustries.status.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sharafindustries.status.exception.InvalidAvailabilityException;
import com.sharafindustries.status.model.Availability;
import com.sharafindustries.status.model.Status;
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
	public static final String DEFAULT_AWAY_MESSAGE = "I'm away from my phone a while.";
	public static final String DEFAULT_BUSY_MESSAGE = "Sorry, I can't talk right now.";

	public List<Status> getDefaultStatuses(String email)
	{
		Status defaultAvailableStatus = new Status(prependEmail(email, "Available"), Availability.Free, DEFAULT_AVAILABLE_MESSAGE);
		Status defaultAwayStatus = new Status(prependEmail(email, "Away"), Availability.Away, DEFAULT_AWAY_MESSAGE);
		Status defaultBusyStatus = new Status(prependEmail(email, "Busy"), Availability.Busy, DEFAULT_BUSY_MESSAGE);
		statusRepository.save(defaultAvailableStatus);
		statusRepository.save(defaultAwayStatus);
		statusRepository.save(defaultBusyStatus);
		List<Status> defaultStatuses = new ArrayList<Status>();
		defaultStatuses.add(defaultAvailableStatus);
		defaultStatuses.add(defaultAwayStatus);
		defaultStatuses.add(defaultBusyStatus);
		return defaultStatuses;
	}

	public Status createAndSaveCustomStatus(String email, String name, String availability, String message)
			throws InvalidAvailabilityException
	{
		// TODO change this to use context.getBean()
		if (Availability.isValid(availability))
			return new Status(prependEmail(email, name), Availability.valueOf(availability), message);
		// return statusRepository.save(new Status(name,
		// Availability.valueOf(availability), message));
		else
		{
			throw new InvalidAvailabilityException(availability + " is not a valid Availability");
		}
	}

	public void saveStatus(Status status)
	{
		statusRepository.save(status);
	}
	
	public String prependEmail(String email, String statusName)
	{
		return email + "_" + statusName;
	}
	
}
