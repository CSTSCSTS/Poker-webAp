package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InValidAccessTokenException extends Exception  {
		public InValidAccessTokenException(String msg){
				super(msg);
			}
}
