package com.sharafindustries.status.configuration;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.annotation.Value;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.sharafindustries.status.model.User;

@Configuration
public class BeanFactory
{
	
	@Value("${front-end-client-id}")
	private String clientId;
	
	@Bean
	@Scope("prototype")
	public User createUser(String email, String password)
	{
		User user = new User(email, password);
		return user;
	}
	
	@Bean
	public GoogleIdTokenVerifier googleIdTokenVerifier()
	{
		return new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
				.setAudience(Collections.singletonList(clientId))
				.build();
	}

}
