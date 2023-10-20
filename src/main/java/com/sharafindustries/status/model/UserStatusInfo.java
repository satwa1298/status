package com.sharafindustries.status.model;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class UserStatusInfo
{
	private String availability;
	private String message;
	
	public UserStatusInfo()
	{
		
	}
	
	public UserStatusInfo(String availability, String message)
	{
		super();
		this.availability = availability;
		this.message = message;
	}

	public String getAvailability()
	{
		return availability;
	}

	public void setAvailability(String availability)
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
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((availability == null) ? 0 : availability.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserStatusInfo other = (UserStatusInfo) obj;
		if (availability == null)
		{
			if (other.availability != null)
				return false;
		}
		else if (!availability.equals(other.availability))
			return false;
		if (message == null)
		{
			if (other.message != null)
				return false;
		}
		else if (!message.equals(other.message))
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "UserStatusInfo [availability=" + availability + ", message=" + message + "]";
	}
}
