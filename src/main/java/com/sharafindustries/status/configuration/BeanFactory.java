package com.sharafindustries.status.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.sharafindustries.status.model.User;
import com.sharafindustries.status.service.StatusService;

@Configuration
public class BeanFactory
{
	@Autowired
	private StatusService statusService;
	
	@Bean
	@Scope("prototype")
	public User createUser(String email)
	{
		User user = new User(email);
		user.setStatuses(statusService.getDefaultStatuses());
		user.setCurrentStatus(user.getStatuses().get(0));
		return user;
	}

}
