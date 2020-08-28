package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.demo.config.oauth2.OAuth2TokenService;
import com.example.demo.domain.model.LoginSession;
import com.example.demo.domain.model.Ranking;
import com.example.demo.exception.ForbiddenException;
import com.example.demo.exception.InValidAccessTokenException;

@Controller
public class RankingController {

	@Autowired
 private DiscoveryClient dc;

	@Autowired
	public LoginSession loginSession;

	@Autowired
	private OAuth2TokenService oauth2TokenService;

/**
	* ランキング情報を返す
 * @return
 * @throws InValidAccessTokenException
 * @throws ForbiddenException
 */
@GetMapping("/ranking")
	@ResponseBody
	public Ranking getRanking() throws InValidAccessTokenException, ForbiddenException {
		RestTemplate restTemplate = new RestTemplate();
		List<ServiceInstance> userApServiceList = dc.getInstances("UserAp");
  ServiceInstance userApInstance = userApServiceList.get(0);

  String getRankingUrl = "http://" + userApInstance.getHost() + ":" + userApInstance.getPort() + "/ranking";
  HttpHeaders httpHeaders = new HttpHeaders();
  httpHeaders.add("User-Agent", "eltabo");
  // OAuthユーザーならアクセストークン追加
  if(loginSession.isOauthUser()) {
  		// "Authorization: Bearer <ACCESS_TOKEN>"というヘッダーを追加
  		httpHeaders.add(HttpHeaders.AUTHORIZATION,
  						"Bearer " + oauth2TokenService.getTokenValue());
  }
  HttpEntity<Object> httpEntity = new HttpEntity<>(null, httpHeaders);

  Ranking ranking = null;
  try {
  		ranking = restTemplate.exchange(getRankingUrl, HttpMethod.GET, httpEntity, Ranking.class).getBody();
  } catch (HttpClientErrorException e) {
  		HttpStatus statusCode = e.getStatusCode();
  		if(statusCode.equals(HttpStatus.UNAUTHORIZED)) {
  				throw new InValidAccessTokenException("アクセストークンがないもしくは期限切れです。");
  		}

  		if(statusCode.equals(HttpStatus.FORBIDDEN)) {
  				throw new ForbiddenException("ランキング表示に必要な権限がありません。");
  		}
  }

		return ranking;
	}

}
