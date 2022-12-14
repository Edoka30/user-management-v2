package com.sergeytutorial.controller;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sergeytutorial.dto.AddressDto;
import com.sergeytutorial.dto.UserDto;
import com.sergeytutorial.exception.UserServiceException;
import com.sergeytutorial.request.UserDetailsRequest;
import com.sergeytutorial.response.AddressRest;
import com.sergeytutorial.response.ErrorMessages;
import com.sergeytutorial.response.OperationStatusModel;
import com.sergeytutorial.response.UserRest;
import com.sergeytutorial.service.AddressService;
import com.sergeytutorial.service.UserService;

@RestController

@RequestMapping(value = "users")
public class UserController {

	@Autowired
	UserService userService;
	
	@Autowired
	AddressService addressService;

	@PostMapping
	public UserRest createUser(@RequestBody UserDetailsRequest userDetails) throws Exception {

		if (userDetails.getFirstName() == null)
			throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
		UserRest returnValue = new UserRest();
		// UserDto userDto = new UserDto();
		// BeanUtils.copyProperties(userDetails, userDto);
		ModelMapper modelMapper = new ModelMapper();
		UserDto userDto = modelMapper.map(userDetails, UserDto.class);
		UserDto createdUser = userService.createUser(userDto);
		// BeanUtils.copyProperties(createdUser, returnValue);
		returnValue = modelMapper.map(createdUser, UserRest.class);
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

	@GetMapping(produces = { org.springframework.http.MediaType.APPLICATION_ATOM_XML_VALUE,
			org.springframework.http.MediaType.APPLICATION_JSON_VALUE })
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

	@GetMapping(path = "/{id}/addresses")
	public List<AddressRest> getAddresses(@PathVariable String id) throws Exception {
		List<AddressRest> returnValue = new ArrayList<>();

		List<AddressDto> addressDTO = addressService.getAddresses(id);

		if (addressDTO != null && !addressDTO.isEmpty()) {
			Type listType = new TypeToken<List<AddressRest>>() {
			}.getType();
			returnValue = new ModelMapper().map(addressDTO, listType);
		}
		//BeanUtils.copyProperties(addressDTO, returnValue);
		return returnValue;
	}

}
