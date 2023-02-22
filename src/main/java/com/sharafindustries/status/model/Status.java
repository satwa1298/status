package com.sharafindustries.status.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Entity
@Scope("prototype")
public class Status
{
	@Id
	private String name;
	
	@Enumerated(EnumType.STRING)
	private Availability availability;
	
	private String message;

	public Status()
	{
		
	}
	
	public Status(String name, Availability availability, String message)
	{
		this.name = name;
		this.availability = availability;
		this.message = message;
	}
	
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;;
	}

	public Availability getAvailability()
	{
		return availability;
	}

	public void setAvailability(Availability availability)
	{
		this.availability = availability;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	@Override
	public String toString()
	{
		return "Status [name=" + name + ", availability=" + availability + ", message=" + message + "]";
	}
	
}
