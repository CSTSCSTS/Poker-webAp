package com.example.demo.failurehandler;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

import com.example.demo.exception.LoginFailureException;

@Component
public class AuthenticationEventListener {

		public void handle (AuthenticationFailureBadCredentialsEvent event) throws LoginFailureException {
				System.out.println("cccccc");
				throw new BadCredentialsException("RGRGRG");
		}

}
