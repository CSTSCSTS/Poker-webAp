package com.example.demo.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.UserDto;

@Component
public class UserService implements UserDetailsService  {

		@Autowired
	 private DiscoveryClient dc;

		@Autowired
		protected MessageSource messageSource;

		@Override
		public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				RestTemplate restTemplate = new RestTemplate();

				Map<String, String> loginMap = new HashMap<>();
		  loginMap.put("userName", username);
//		  loginMap.put("password", password);

				List<ServiceInstance> userApServiceList = dc.getInstances("UserAp");
		  ServiceInstance userApInstance = userApServiceList.get(0);
				String getloginUrl = "http://" + userApInstance.getHost() + ":" + userApInstance.getPort() + "/login" + "?userName=" + username;
				UserDto user = restTemplate.getForEntity(getloginUrl, UserDto.class).getBody();

				if(user == null){
      throw new UsernameNotFoundException("User not found for login id: ");
    }

				return new User(user.getUserName(), "{noop}"+user.getPassword(), Collections.emptyList());
		}
}
