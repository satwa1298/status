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
	//TODO these need to be saved. maybe have these 3 accessible to everyone and field in user is only custom statuses?
	//TODO also maybe add name field
	public static final Status DEFAULT_AVAILABLE_STATUS = new Status("Available", Availability.Free, "I'm available right now!");
	public static final Status DEFAULT_AWAY_STATUS = new Status("Away", Availability.Away, "I'm away from my phone a while.");
	public static final Status DEFAULT_BUSY_STATUS = new Status("Busy", Availability.Busy, "Sorry, I can't talk right now.");
	private static boolean areDefaultsSaved = false;
	
	public List<Status> getDefaultStatuses()
	{
		if (!areDefaultsSaved)
		{
			statusRepository.save(DEFAULT_AVAILABLE_STATUS);
			statusRepository.save(DEFAULT_AWAY_STATUS);
			statusRepository.save(DEFAULT_BUSY_STATUS);
			areDefaultsSaved = true;
		}
		List<Status> defaultStatuses = new ArrayList<Status>();
		defaultStatuses.add(DEFAULT_AVAILABLE_STATUS);
		defaultStatuses.add(DEFAULT_AWAY_STATUS);
		defaultStatuses.add(DEFAULT_BUSY_STATUS);
		return defaultStatuses;
	}
	
	public Status createAndSaveCustomStatus(String name, String availability, String message) throws InvalidAvailabilityException
	{
		//TODO change this to use context.getBean()
		if (Availability.isValid(availability))
			return statusRepository.save(new Status(name, Availability.valueOf(availability), message));
		else
		{
			throw new InvalidAvailabilityException(availability + " is not a valid Availability");
		}
	}
	
	public void saveStatus(Status status)
	{
		statusRepository.save(status);
	}

}
