package com.sharafindustries.status.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sharafindustries.status.model.Status;
import com.sharafindustries.status.model.User;

public interface StatusRepository extends JpaRepository<Status, Integer>
{
	
	public Status getStatusByNameAndUser(String name, User user);

}
