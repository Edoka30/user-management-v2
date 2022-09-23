package com.sergeytutorial.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.sergeytutorial.dto.UserDto;
import com.sergeytutorial.entities.UserEntity;

public interface UserService extends UserDetailsService {

	UserDto createUser(UserDto userDto);

	public List<UserEntity> viewAllUsers() throws Exception;

	UserDto getUser(String email);
	
	UserDto getUserByUserId(String userid) throws Exception;
	UserDto updateUser(String userid, UserDto user);
	void deleteUser(String userid);

	List<UserDto> getUsers(int page, int limit);
}
