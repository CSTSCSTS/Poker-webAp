package com.example.demo.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.demo.config.oauth2.OAuth2TokenService;
import com.example.demo.controller.base.SessionCheck;
import com.example.demo.domain.model.LoginSession;
import com.example.demo.domain.model.Money;
import com.example.demo.domain.model.PokerPlayingInfo;
import com.example.demo.domain.model.PokerPlayingInfo.Winner;
import com.example.demo.dto.MoneyDto;
import com.example.demo.dto.PokerPlayingInfoDto;
import com.example.demo.exception.ForbiddenException;
import com.example.demo.exception.IllegalBetException;
import com.example.demo.exception.InValidAccessTokenException;

@Controller
public class PokerController extends SessionCheck {

	@Autowired
 private DiscoveryClient dc;

	@Autowired
	public LoginSession loginSession;

	@Autowired
	private OAuth2TokenService oauth2TokenService;

/**
 * 所持金情報を返す
 * @return
 * @throws Exception
 */
 @GetMapping("/bet")
	@ResponseBody
	public Money getMoney() throws Exception {
		sessionCheck();

		RestTemplate restTemplate = new RestTemplate();
		List<ServiceInstance> userApServiceList = dc.getInstances("UserAp");
  ServiceInstance userApInstance = userApServiceList.get(0);
		String getMoneyUrl = "http://" + userApInstance.getHost() + ":" + userApInstance.getPort() + "/money?userId={userId}";
		return Money.convertMoney(restTemplate.getForEntity(getMoneyUrl, MoneyDto.class, loginSession.getUserId().get()).getBody());
	}

/**
 * ベット額が所持金を超えていないかチェックして、ポーカーの初期情報(山札・プレイヤーとCPUの手札)を返す。
 * @param betMoney ベット額
 * @param jokerIncluded ジョーカーを含んでいるかどうか
 * @return
 * @throws Exception
 */
@PostMapping("/config")
	@ResponseBody
	public PokerPlayingInfo postPokerStart(BigDecimal betMoney, boolean jokerIncluded) throws Exception {

		 sessionCheck();
		 HttpHeaders headers = new HttpHeaders();
 		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

 		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
 		map.add("userId", loginSession.getUserId().get());
 		map.add("betMoney", betMoney);
 		map.add("jokerIncluded", jokerIncluded);

 		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(map, headers);

 		RestTemplate restTemplate = new RestTemplate();
		 List<ServiceInstance> serviceList = dc.getInstances("PokerAp");
	  ServiceInstance serviceInstance = serviceList.get(0);
	  String url = "http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + "/config";
	  PokerPlayingInfoDto body;
	  try {
	  		body = restTemplate.postForEntity(url, request, PokerPlayingInfoDto.class).getBody();
	  } catch (HttpClientErrorException e) {
	  		throw new IllegalBetException(messageSource.getMessage("illegal.bet", null, Locale.JAPAN));
	  }

	  return PokerPlayingInfo.ConvertToDomainPrepare(body);

	}

/**
 * 手札交換・役判定を勝者判定を実施
 * @param jsonPlayerHands プレイヤーの手札
 * @param jsonDeck 山札
 * @param jsonComputerHands  CPUの手札
 * @return
 * @throws Exception
 */
@PostMapping("/play")
	@ResponseBody
	public PokerPlayingInfo handChange(String jsonPlayerHands, String jsonDeck, String jsonComputerHands) throws Exception {

		sessionCheck();
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("jsonPlayerHands", jsonPlayerHands);
		map.add("jsonDeck", jsonDeck);
		map.add("jsonComputerHands", jsonComputerHands);

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

		List<ServiceInstance> serviceList = dc.getInstances("PokerAp");
  ServiceInstance serviceInstance = serviceList.get(0);
  String url = "http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + "/play";
  PokerPlayingInfoDto body;
  try {
  		body = restTemplate.postForEntity(url, request, PokerPlayingInfoDto.class).getBody();
  } catch (HttpClientErrorException e) {
  		throw new Exception(e.getMessage());
  }
  return PokerPlayingInfo.ConvertToDomainHandChange(body);

	}

/**
 * 勝敗に応じて所持金を更新する
 * @param betMoney ベット額
 * @param winner 勝者
 * @return
 * @throws Exception
 */
@PostMapping("/result")
	@ResponseBody
	public Money result(BigDecimal betMoney, Winner winner) throws Exception {

	  sessionCheck();
	  RestTemplate restTemplate = new RestTemplate();
	  HttpHeaders headers = new HttpHeaders();
 		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

 		// OAuthユーザーの場合、アクセストークンを追加する。
 		if(loginSession.isOauthUser()) {
 				headers.add(HttpHeaders.AUTHORIZATION,
         "Bearer " + oauth2TokenService.getTokenValue());

 		}

 		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
 		map.add("userId", loginSession.getUserId().get());
 		map.add("betMoney", betMoney);
 		map.add("winner", winner.toString());

 		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(map, headers);

		 List<ServiceInstance> serviceList = dc.getInstances("UserAp");
	  ServiceInstance serviceInstance = serviceList.get(0);
	  String url = "http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + "/poker_money";
	  MoneyDto body = null;
	  try {
	  		body = restTemplate.exchange(url, HttpMethod.POST, request, MoneyDto.class).getBody();
	  } catch (HttpClientErrorException e) {
	  		HttpStatus statusCode = e.getStatusCode();
	  		if(statusCode.equals(HttpStatus.UNAUTHORIZED)) {
	  				throw new InValidAccessTokenException("アクセストークンがないもしくは期限切れです。");
	  		}

	  		if(statusCode.equals(HttpStatus.FORBIDDEN)) {
	  				throw new ForbiddenException("ポーカープレイに必要な権限がありません。");
	  		}
	  }
			return Money.convertMoney(body);
	}


}
