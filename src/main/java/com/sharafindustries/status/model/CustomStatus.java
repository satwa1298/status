package com.sharafindustries.status.model;

public class CustomStatus extends Status
{
	private String message;

	public CustomStatus()
	{
		
	}
	
	public CustomStatus(String availability, String message)
	{
		super(availability);
		this.message = message;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}
	
}
