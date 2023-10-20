package com.sharafindustries.status.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.context.annotation.Scope;

@Scope("prototype")
@Entity
@Table(name = "status_users")
public class User
{
	@Id
	private String email;
	
	private String password;
	
	@ElementCollection
	private List<String> friendList;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Status> customStatuses;
	
//	@OneToOne
//	private Status currentStatus;
	private Availability currentAvailability;
	
	private String currentMessage;
	
	public User()
	{
		friendList = new ArrayList<String>();
		customStatuses = new ArrayList<Status>();
		this.currentAvailability = Status.DEFAULT_AVAILABLE_STATUS.getAvailability();
		this.currentMessage = Status.DEFAULT_AVAILABLE_STATUS.getMessage();
	}
	
	public User(String email, String password)
	{
		this.email = email;
		this.password = password;
		friendList = new ArrayList<String>();
		customStatuses = new ArrayList<Status>();
		this.currentAvailability = Status.DEFAULT_AVAILABLE_STATUS.getAvailability();
		this.currentMessage = Status.DEFAULT_AVAILABLE_STATUS.getMessage();
	}

	public User(String email, List<String> friendList, List<Status> customStatuses, Availability currentAvailability, String currentMessage)
	{
		this.email = email;
		this.friendList = friendList;
		this.customStatuses = customStatuses;
		this.currentAvailability = currentAvailability;
		this.currentMessage = currentMessage;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}
	
	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public List<String> getFriendList()
	{
		return friendList;
	}

	public void setFriendList(List<String> friendList)
	{
		this.friendList = friendList;
	}

	public List<Status> getCustomStatuses()
	{
		return customStatuses;
	}

	public void setCustomStatuses(List<Status> customStatuses)
	{
		this.customStatuses = customStatuses;
	}

	public Availability getCurrentAvailability()
	{
		return currentAvailability;
	}

	public void setCurrentAvailability(Availability currentAvailability)
	{
		this.currentAvailability = currentAvailability;
	}

	public String getCurrentMessage()
	{
		return currentMessage;
	}

	public void setCurrentMessage(String currentMessage)
	{
		this.currentMessage = currentMessage;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((currentAvailability == null) ? 0 : currentAvailability.hashCode());
		result = prime * result + ((currentMessage == null) ? 0 : currentMessage.hashCode());
		result = prime * result + ((customStatuses == null) ? 0 : customStatuses.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((friendList == null) ? 0 : friendList.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
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
		User other = (User) obj;
		if (currentAvailability != other.currentAvailability)
			return false;
		if (currentMessage == null)
		{
			if (other.currentMessage != null)
				return false;
		}
		else if (!currentMessage.equals(other.currentMessage))
			return false;
		if (customStatuses == null)
		{
			if (other.customStatuses != null)
				return false;
		}
		else if (!customStatuses.equals(other.customStatuses))
			return false;
		if (email == null)
		{
			if (other.email != null)
				return false;
		}
		else if (!email.equals(other.email))
			return false;
		if (friendList == null)
		{
			if (other.friendList != null)
				return false;
		}
		else if (!friendList.equals(other.friendList))
			return false;
		if (password == null)
		{
			if (other.password != null)
				return false;
		}
		else if (!password.equals(other.password))
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "User [email=" + email + ", friendList=" + friendList + ", statuses=" + customStatuses
				+ ", currentAvailability=" + currentAvailability + ", currentMessage=" + currentMessage + "]";
	}
	
}
