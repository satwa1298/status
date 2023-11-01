package com.sharafindustries.status.model;

import java.security.SecureRandom;
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
	
	@ElementCollection
	private List<String> friendList;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Status> customStatuses;
	
	//TODO probably refactor this to be UserStatusInfo, and make availability field in UserStatusInfo type Availability
	private String currentStatusName;
	
	private Availability currentAvailability;
	
	private String currentMessage;
	
	private String passphrase;
	
	public User()
	{
		friendList = new ArrayList<String>();
		customStatuses = new ArrayList<Status>();
		this.currentStatusName = Status.DEFAULT_AVAILABLE_STATUS.getName();
		this.currentAvailability = Status.DEFAULT_AVAILABLE_STATUS.getAvailability();
		this.currentMessage = Status.DEFAULT_AVAILABLE_STATUS.getMessage();
		this.passphrase = generatePassphrase();
	}
	
	public User(String email)
	{
		this.email = email;
		friendList = new ArrayList<String>();
		customStatuses = new ArrayList<Status>();
		this.currentStatusName = Status.DEFAULT_AVAILABLE_STATUS.getName();
		this.currentAvailability = Status.DEFAULT_AVAILABLE_STATUS.getAvailability();
		this.currentMessage = Status.DEFAULT_AVAILABLE_STATUS.getMessage();
		this.passphrase = generatePassphrase();
	}

	public User(String email, List<String> friendList, List<Status> customStatuses, String currentStatusName, Availability currentAvailability, String currentMessage)
	{
		this.email = email;
		this.friendList = friendList;
		this.customStatuses = customStatuses;
		this.currentStatusName = currentStatusName;
		this.currentAvailability = currentAvailability;
		this.currentMessage = currentMessage;
		this.passphrase = generatePassphrase();
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
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

	public String getCurrentStatusName()
	{
		return currentStatusName;
	}

	public void setCurrentStatusName(String currentStatusName)
	{
		this.currentStatusName = currentStatusName;
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
	
	public String getPassphrase()
	{
		return passphrase;
	}

	public void setPassphrase(String passphrase)
	{
		this.passphrase = passphrase;
	}

	private String generatePassphrase()
	{
		int leftLimit = 48; // numeral '0'
	    int rightLimit = 122; // letter 'z'
	    int passphraseLength = 10;
	    SecureRandom random = new SecureRandom();

	    String passphrase = random.ints(leftLimit, rightLimit + 1)
	      .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
	      .limit(passphraseLength)
	      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
	      .toString();
	    return passphrase;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((currentAvailability == null) ? 0 : currentAvailability.hashCode());
		result = prime * result + ((currentMessage == null) ? 0 : currentMessage.hashCode());
		result = prime * result + ((currentStatusName == null) ? 0 : currentStatusName.hashCode());
		result = prime * result + ((customStatuses == null) ? 0 : customStatuses.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((friendList == null) ? 0 : friendList.hashCode());
		result = prime * result + ((passphrase == null) ? 0 : passphrase.hashCode());
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
		if (currentStatusName == null)
		{
			if (other.currentStatusName != null)
				return false;
		}
		else if (!currentStatusName.equals(other.currentStatusName))
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
		if (passphrase == null)
		{
			if (other.passphrase != null)
				return false;
		}
		else if (!passphrase.equals(other.passphrase))
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "User [email=" + email + ", friendList=" + friendList + ", customStatuses=" + customStatuses
				+ ", currentStatusName=" + currentStatusName + ", currentAvailability=" + currentAvailability
				+ ", currentMessage=" + currentMessage + ", passphrase=" + passphrase + "]";
	}
	
}
