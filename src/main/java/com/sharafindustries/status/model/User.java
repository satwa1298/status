package com.sharafindustries.status.model;

import java.util.ArrayList;
import java.util.List;

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
	private List<String> permittedUsers;
	
	//@ElementCollection(targetClass = Status.class)
	@OneToMany
	private List<Status> statuses;
	
	@OneToOne
	private Status currentStatus;
	
	public User()
	{
		permittedUsers = new ArrayList<String>();
		statuses = new ArrayList<Status>();
		this.currentStatus = null; 
	}
	
	public User(String email)
	{
		this.email = email;
		permittedUsers = new ArrayList<String>();
		statuses = new ArrayList<Status>();
		this.currentStatus = null;
	}

	public User(String email, List<String> permittedUsers, List<Status> statuses, Status currentStatus)
	{
		this.email = email;
		this.permittedUsers = permittedUsers;
		this.statuses = statuses;
		this.currentStatus = currentStatus;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public List<String> getPermittedUsers()
	{
		return permittedUsers;
	}

	public void setPermittedUsers(List<String> permittedUsers)
	{
		this.permittedUsers = permittedUsers;
	}

	public List<Status> getStatuses()
	{
		return statuses;
	}

	public void setStatuses(List<Status> statuses)
	{
		this.statuses = statuses;
	}
	
	

	public Status getCurrentStatus()
	{
		return currentStatus;
	}

	public void setCurrentStatus(Status currentStatus)
	{
		this.currentStatus = currentStatus;
	}

	@Override
	public String toString()
	{
		return "User [email=" + email + ", permittedUsers=" + permittedUsers + ", statuses=" + statuses
				+ ", currentStatus=" + currentStatus + "]";
	}
	
}
