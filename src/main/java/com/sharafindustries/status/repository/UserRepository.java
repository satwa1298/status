package com.sharafindustries.status.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sharafindustries.status.model.Status;
import com.sharafindustries.status.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, String>
{
	public User findByEmail(String email);
	
//	public Status findUserStatusByName(User user, String statusName);

}
