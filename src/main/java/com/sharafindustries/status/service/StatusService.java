package com.sharafindustries.status.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sharafindustries.status.model.Status;
import com.sharafindustries.status.repository.StatusRepository;


@Service
public class StatusService
{
	@Autowired
	private StatusRepository statusRepository;
	//TODO these need to be saved. maybe have these 3 accessible to everyone and field in user is only custom statuses?
	//TODO also maybe add name field
	public static final Status DEFAULT_AVAILABLE_STATUS = new Status("Available", "I'm available right now!");
	public static final Status DEFAULT_AWAY_STATUS = new Status("Away", "I'm away from my phone a while.");
	public static final Status DEFAULT_BUSY_STATUS = new Status("Busy", "Sorry, I can't talk right now.");
	
	public List<Status> getDefaultStatuses()
	{
		List<Status> defaultStatuses = new ArrayList<Status>();
		defaultStatuses.add(DEFAULT_AVAILABLE_STATUS);
		defaultStatuses.add(DEFAULT_AWAY_STATUS);
		defaultStatuses.add(DEFAULT_BUSY_STATUS);
		return defaultStatuses;
	}
	
	public Status createAndSaveCustomStatus(String availability, String message)
	{
		return statusRepository.save(new Status(availability, message));
	}
	
	public void saveStatus(Status status)
	{
		statusRepository.save(status);
	}

}
