package com.sharafindustries.status.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Entity
@Scope("prototype")
public class Status
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	private String name;
	
	@Enumerated(EnumType.STRING)
	private Availability availability;
	
	private String message;
	
	public static final Status DEFAULT_AVAILABLE_STATUS = new Status(null, "Default Available Status", Availability.Available, "I'm available right now!");
	public static final Status DEFAULT_AWAY_STATUS = new Status(null, "Default Away Status", Availability.Away, "I'm away right now.");
	public static final Status DEFAULT_BUSY_STATUS = new Status(null, "Default Busy Status", Availability.Busy, "Sorry, I'm busy right now.");
	

	public Status()
	{
		
	}
	
	public Status(User user, String name, Availability availability, String message)
	{
		this.user = user;
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
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((availability == null) ? 0 : availability.hashCode());
		result = prime * result + id;
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
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
		Status other = (Status) obj;
		if (availability != other.availability)
			return false;
		if (id != other.id)
			return false;
		if (message == null)
		{
			if (other.message != null)
				return false;
		}
		else if (!message.equals(other.message))
			return false;
		if (name == null)
		{
			if (other.name != null)
				return false;
		}
		else if (!name.equals(other.name))
			return false;
		if (user == null)
		{
			if (other.user != null)
				return false;
		}
		else if (!user.equals(other.user))
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "Status [id=" + id + ", user=" + user + ", name=" + name + ", availability=" + availability
				+ ", message=" + message + "]";
	}
	
}
