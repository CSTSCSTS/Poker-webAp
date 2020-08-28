package com.example.demo.domain.model;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Scope(value= "session" , proxyMode = ScopedProxyMode.TARGET_CLASS)
@Data
public class LoginSession implements Serializable {

	private Integer userId;
	private String userName;
	private boolean isOauthUser;

	public Optional<Integer> getUserId() {
			return Optional.ofNullable(userId);
	}
	public Optional<String> getUserName() {
			return Optional.ofNullable(userName);
	}

}
