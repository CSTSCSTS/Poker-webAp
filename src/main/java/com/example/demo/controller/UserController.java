package com.example.demo.controller;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.demo.domain.model.User;
import com.example.demo.dto.UserDto;
import com.example.demo.exception.UserNameDuplicateException;

@Controller
public class UserController {

		@Autowired
	 private DiscoveryClient dc;

		@Autowired
		protected MessageSource messageSource;

/**
 * ユーザー登録をする
 * @param userName ユーザー名
 * @param password パスワード
 * @return
 * @throws UserNameDuplicateException ユーザー名重複エラー
 */
 @PostMapping("/user")
	@ResponseBody
	public User add(String userName, String password) throws UserNameDuplicateException {
 		HttpHeaders headers = new HttpHeaders();
 		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

 		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
 		map.add("userName", userName);
 		map.add("password", password);

 		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

 		RestTemplate restTemplate = new RestTemplate();
 		List<ServiceInstance> userApServiceList = dc.getInstances("UserAp");
   ServiceInstance userApInstance = userApServiceList.get(0);
 		String getRankingUrl = "http://" + userApInstance.getHost() + ":" + userApInstance.getPort() + "/user";
 		UserDto userDto;
 		try {
 				userDto = restTemplate.postForEntity(getRankingUrl, request, UserDto.class).getBody();
 		} catch (HttpClientErrorException e) {
 				throw new UserNameDuplicateException(messageSource.getMessage("user.duplicate", null, Locale.JAPAN));
 		}
 		return new User(userDto.getUserId(), userDto.getUserName(), userDto.getPassword(), null);

	}

}
