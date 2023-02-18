package com.sharafindustries.status.model;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.Table;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Component
@Scope("prototype")
@Entity
@Table(appliesTo = "status_users")
public class User
{
	@Id
	private String email;
	
	private List<String> permittedUsers;
	
	@OneToOne
	private List<Status> statuses;
	
	private Status currentStatus;
	
	public User()
	{
		permittedUsers = new ArrayList<>();
		statuses = new ArrayList<>();
		this.currentStatus = null; 
	}
	
	public User(String email)
	{
		this.email = email;
		permittedUsers = new ArrayList<>();
		statuses = new ArrayList<>();
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
