package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
		private final UserService service;

		@Override
		protected void configure(HttpSecurity http) throws Exception {

				// CSRF無効
				http.csrf().disable();

				http.oauth2Login()
			 .loginPage("/login")
    .permitAll()
				.defaultSuccessUrl("/oauth2loginSuccess", true);
    http.authorizeRequests()
      .anyRequest().authenticated();

    http.formLogin()
				.loginPage("/login")
    .permitAll()
    .loginProcessingUrl("/login")
    .successForwardUrl("/login/success")
    //認証失敗時にforwardするURLを設定
    .failureForwardUrl("/login/error")
    .usernameParameter("userName").passwordParameter("password")
    .and();

		}

		@Autowired
		public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
	   // AuthenticationManagerBuilderにUserServiceを紐付ける
    auth.userDetailsService(service);
		}

}
