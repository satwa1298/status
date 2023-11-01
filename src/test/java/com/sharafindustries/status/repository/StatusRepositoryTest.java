package com.sharafindustries.status.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.sharafindustries.status.model.Availability;
import com.sharafindustries.status.model.Status;
import com.sharafindustries.status.model.User;

@DataJpaTest
public class StatusRepositoryTest
{
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private StatusRepository statusRepository;
	
	@Test
	public void testFindStatusByName()
	{
		String showeringMessage = "I'm showering right now";
		User user1 = new User("tester1@test.com");
		List<Status> customStatuses1 = new ArrayList<>();
		customStatuses1.add(new Status(user1, "showering", Availability.Away, showeringMessage));
		user1.setCustomStatuses(customStatuses1);
		userRepository.save(user1);
		
		User user2 = new User("tester2@test.com");
		List<Status> customStatuses2 = new ArrayList<>();
		customStatuses2.add(new Status(user2, "showering", Availability.Away, "I shouldn't show up"));
		user2.setCustomStatuses(customStatuses2);
		userRepository.save(user2);
		
		Status user1ShoweringStatus = statusRepository.getStatusByNameAndUser("showering", user1);
		assertEquals(showeringMessage, user1ShoweringStatus.getMessage());
		
	}

}
