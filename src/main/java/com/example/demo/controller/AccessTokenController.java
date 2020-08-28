package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.config.oauth2.OAuth2TokenService;

@Controller
public class AccessTokenController {

		@Autowired
		private OAuth2TokenService oauth2TokenService;

		@RequestMapping("/access-token")
		public String get() {
			return oauth2TokenService.getTokenValue();
		}


}
