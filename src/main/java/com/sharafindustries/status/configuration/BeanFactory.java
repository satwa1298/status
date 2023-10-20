package com.sharafindustries.status.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.sharafindustries.status.model.User;

@Configuration
public class BeanFactory
{
	
	@Bean
	@Scope("prototype")
	public User createUser(String email, String password)
	{
		User user = new User(email, password);
		return user;
	}

}
