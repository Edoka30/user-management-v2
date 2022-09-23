package com.sergeytutorial.controller;

import java.awt.PageAttributes.MediaType;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaTypeEditor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sergeytutorial.dto.UserDto;
import com.sergeytutorial.exception.UserServiceException;
import com.sergeytutorial.request.UserDetailsRequest;
import com.sergeytutorial.response.ErrorMessages;
import com.sergeytutorial.response.OperationStatusModel;
import com.sergeytutorial.response.UserRest;
import com.sergeytutorial.service.UserService;

@RestController

@RequestMapping(value = "users")
public class UserController {

	@Autowired
	UserService userService;

	@PostMapping
	public UserRest createUser(@RequestBody UserDetailsRequest userDetails) throws Exception {

		if (userDetails.getFirstName() == null)
			throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
		UserRest returnValue = new UserRest();
		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(userDetails, userDto);
		UserDto createdUser = userService.createUser(userDto);
		BeanUtils.copyProperties(createdUser, returnValue);

		return returnValue;
	}

	@GetMapping(path = "/{userid}")
	public UserRest getUserByUserid(@PathVariable String userid) throws Exception {
		UserRest returnValue = new UserRest();

		UserDto userDto = userService.getUserByUserId(userid);
		BeanUtils.copyProperties(userDto, returnValue);
		return returnValue;

	}

	@PutMapping(path = "/{userid}")
	public UserRest updateUser(@PathVariable String userid, @RequestBody UserDetailsRequest userDetails) {
		if (userid == null)
			throw new NullPointerException(" UserId is not null");
		UserRest returnValue = new UserRest();
		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(userDetails, userDto);
		UserDto updatedUser = userService.updateUser(userid, userDto);
		BeanUtils.copyProperties(updatedUser, returnValue);

		return returnValue;

	}

	@DeleteMapping(path = "/{userid}")
	public OperationStatusModel deleteUser(@PathVariable String userid) {

		OperationStatusModel returnValue = new OperationStatusModel();
		returnValue.setOperationName("Delete");

		userService.deleteUser(userid);
		returnValue.setOperationStatus("Succesful");

		return returnValue;

	}

	@GetMapping
	public List<UserRest> pageableViewallUsers(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limit", defaultValue = "1") int limit) {

		List<UserRest> returnValue = new ArrayList<>();
		List<UserDto> users = userService.getUsers(page, limit);
		for (UserDto userDto : users) {

			UserRest userModel = new UserRest();
			BeanUtils.copyProperties(userDto, userModel);

			returnValue.add(userModel);
		}
		return returnValue;
	}

}
