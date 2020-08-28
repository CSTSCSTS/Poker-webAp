package com.example.demo.exception;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class LoginFailureException extends IOException {

	public LoginFailureException(String msg){
		super(msg);
	}

}
