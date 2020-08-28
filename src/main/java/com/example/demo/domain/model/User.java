package com.example.demo.domain.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class User {

	public int userId;
	public String userName;
	public String password;
	public LocalDateTime loginDate;

}
