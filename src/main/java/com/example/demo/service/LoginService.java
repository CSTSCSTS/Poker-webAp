package com.example.demo.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.constants.PokerConstants;
import com.example.demo.domain.model.Money;
import com.example.demo.dto.MoneyDto;
import com.example.demo.dto.UserDto;
import com.example.demo.dto.UserForLoginDto;
import com.example.demo.exception.LoginFailureException;

@Service
public class LoginService {

	@Autowired
	protected MessageSource messageSource;

	@Autowired
 private DiscoveryClient dc;

	// ログインする
	public UserForLoginDto loginBonus(String username) throws LoginFailureException {

			RestTemplate restTemplate = new RestTemplate();
	  List<ServiceInstance> userApServiceList = dc.getInstances("UserAp");
	  ServiceInstance userApInstance = userApServiceList.get(0);
			String getloginUrl = "http://" + userApInstance.getHost() + ":" + userApInstance.getPort() + "/login" + "?userName=" + username;
			UserDto userDto = restTemplate.getForEntity(getloginUrl, UserDto.class).getBody();

   LocalDateTime now = LocalDateTime.now();
   LocalDateTime loginDate = LocalDateTime.parse(userDto.getLoginDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));

   Calendar nowTime = Calendar.getInstance();
   nowTime.set(now.getYear(), now.getMonthValue() - 1, now.getDayOfMonth());

   Calendar loginTime = Calendar.getInstance();
   loginTime.set(loginDate.getYear(), loginDate.getMonthValue() - 1, loginDate.getDayOfMonth());

   boolean isFirstLogin = false;
   // 現在日時とログイン日時を比較して、ログインがその日初めてかどうか確認
   if(nowTime.compareTo(loginTime) != 0) {
   		String getMoneyUrl = "http://" + userApInstance.getHost() + ":" + userApInstance.getPort() + "/money?userId={userId}"; // 要修正 TODO
   		// 所持金を取得
  	  MoneyDto moneyDto = restTemplate.getForEntity(getMoneyUrl, MoneyDto.class, userDto.getUserId()).getBody();
  	  Money money = Money.convertMoney(moneyDto);
   	// ログインがその日初めてならば、所持金を100円増やす。
   	money.plusMoney(PokerConstants.LOGIN_BONUS);

//   	HttpHeaders headers = new HttpHeaders();
//   	headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//   	MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
//   	map.add("email", "first.last@example.com");
//
//   	HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
//
//   	ResponseEntity<String> response = restTemplate.postForEntity( url, request , String.class );

   	// 所持金情報を更新するためのリクエスト
   	String postMoneyUrl = "http://" + userApInstance.getHost() + ":" + userApInstance.getPort() + "/money"; // 要修正 TODO
   	MoneyDto dto = MoneyDto.convertMoneyDto(money);
 	  restTemplate.postForEntity(postMoneyUrl, dto, MoneyDto.class);

   	isFirstLogin = true;
   }

   // ログイン日時を更新するためのリクエスト
	  String putUserUrl = "http://" + userApInstance.getHost() + ":" + userApInstance.getPort() + "/user"; // 要修正 TODO
	  restTemplate.put(putUserUrl, userDto);

	  return UserForLoginDto.builder()
	  		        .userId(userDto.getUserId())
	  		        .userName(userDto.getUserName())
	  		        .isFirstLogin(isFirstLogin)
	  		        .build();

	}

}
