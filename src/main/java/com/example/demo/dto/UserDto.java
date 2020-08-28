package com.example.demo.dto;

import java.time.format.DateTimeFormatter;

import com.example.demo.domain.model.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

		public int userId;
		public String userName;
		public String password;
		public String loginDate;


		public static UserDto convertUserDto(User user) {
				return UserDto.builder()
   						.userId(user.getUserId())
   						.userName(user.getUserName())
   						.password(user.getPassword())
   						.loginDate(user.getLoginDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")))
   						.build();

		}

}
