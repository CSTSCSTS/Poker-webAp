package com.example.demo.controller;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.demo.domain.model.LoginSession;
import com.example.demo.dto.UserDto;
import com.example.demo.dto.UserForLoginDto;
import com.example.demo.exception.LoginFailureException;
import com.example.demo.service.LoginService;

@Controller
public class LoginController {

	@Autowired
	public LoginService loginService;

	@Autowired
	public LoginSession loginSession;

	@Autowired
	protected MessageSource messageSource;

	@Autowired
 private DiscoveryClient dc;

	// ログイン画面を表示する
	@GetMapping("/login")
 public String loginPage() {
     return "login";
 }

	// ポーカースタ－ト画面を表示する
	@RequestMapping("/")
	public String get() {

 		//OAUTHユーザーで出ない場合、ポータルの認証情報を取得する。
//   if(!loginSession.isOauthUser()) {
//   		RestTemplate restTemplate = new RestTemplate();
//   		List<ServiceInstance> potalWebApServiceList = dc.getInstances("Potal-WebAp");
//   		ServiceInstance potalWebApInstance = potalWebApServiceList.get(0);
//   		String getAuthInfoUrl = "http://" + potalWebApInstance.getHost() + ":" + potalWebApInstance.getPort() + "/auth-info";
//   		AuthInfoDto dto = restTemplate.getForEntity(getAuthInfoUrl, AuthInfoDto.class).getBody();
//
//   		loginSession.setUserId(dto.getUserId());
//   		loginSession.setUserName(dto.getUserName());
//   }

		return "index";
	}

//認証成功時に、ここにフォワードしてくる。
@PostMapping("/login/success")
public String success(String userName, String password) throws LoginFailureException {
	UserForLoginDto dto = loginService.loginBonus(userName);
 //セッションオブジェクトにログインユーザーの情報を格納
	loginSession.setUserId(dto.getUserId());
	loginSession.setUserName(userName);
	return "redirect:/";
}

 // 認証失敗時に、ここにフォワードしてくる。
 @PostMapping("/login/error")
 public void error() throws LoginFailureException {
   throw new LoginFailureException(messageSource.getMessage("login.fali", null, Locale.JAPAN));
 }


//OAuth成功時に、ここにフォワードしてくる。
@RequestMapping("/oauth2loginSuccess")
public String success() throws LoginFailureException {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		String userName = auth.getName();


		RestTemplate restTemplate = new RestTemplate();
		List<ServiceInstance> userApServiceList = dc.getInstances("UserAp");
  ServiceInstance userApInstance = userApServiceList.get(0);
		String getOAuthUserUrl = "http://" + userApInstance.getHost() + ":" + userApInstance.getPort() + "/oauth-user?userName={userName}";
		// 所持金を取得
		UserDto dto = null;
		try {
				dto = restTemplate.getForEntity(getOAuthUserUrl, UserDto.class, userName).getBody();
		} catch(HttpClientErrorException e) {
				e.getMessage();
				System.out.println(e.getMessage());
		}


//		UserForLoginDto dto = loginService.loginBonus(userName);
  //セッションオブジェクトにログインユーザーの情報を格納
		loginSession.setUserId(dto.getUserId());
		loginSession.setUserName(userName);
		loginSession.setOauthUser(true);
		return "redirect:/";
}

}
