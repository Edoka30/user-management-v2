package com.sergeytutorial.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.sergeytutorial.dto.UserDto;
import com.sergeytutorial.entities.UserEntity;
import com.sergeytutorial.exception.UserServiceException;
import com.sergeytutorial.repository.UserRepository;
import com.sergeytutorial.response.ErrorMessages;
import com.sergeytutorial.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	UserRepository userRepo;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public UserDto createUser(UserDto userdto) {

		if (userRepo.findByEmail(userdto.getEmail()) != null)
			throw new RuntimeException("User already exists");

		UserEntity userEntity = new UserEntity();
		BeanUtils.copyProperties(userdto, userEntity);
		UUID uuid = UUID.randomUUID();
		String userid = uuid.toString();
		userEntity.setUserId(userid);
		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userdto.getPassword()));
		UserEntity storedUserDetails = userRepo.save(userEntity);
		UserDto returnValue = new UserDto();
		BeanUtils.copyProperties(storedUserDetails, returnValue);
		return returnValue;
	}

	@Override
	public List<UserEntity> viewAllUsers() throws Exception {
		return userRepo.findAll();
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserEntity userEntity = userRepo.findByEmail(email);

		if (userEntity == null)
			throw new UsernameNotFoundException(email);

		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
	}

	@Override
	public UserDto getUser(String email) {
		UserEntity userEntity = userRepo.findByEmail(email);

		if (userEntity == null)
			throw new UsernameNotFoundException(email);
		UserDto returnValue = new UserDto();
		BeanUtils.copyProperties(userEntity, returnValue);

		return returnValue;
	}

	@Override
	public UserDto getUserByUserId(String userid) throws Exception {
		UserDto returnValue = new UserDto();
		UserEntity userEntity = userRepo.findByUserId(userid);
		if (userEntity == null)
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		BeanUtils.copyProperties(userEntity, returnValue);

		return returnValue;
	}

	@Override
	public UserDto updateUser(String userid, UserDto user) {
		UserDto returnValue = new UserDto();
		UserEntity userEntity = userRepo.findByUserId(userid);
		if (userEntity == null)
			throw new UsernameNotFoundException(userid);
		userEntity.setFirstName(user.getFirstName());
		userEntity.setLastName(user.getLastName());
		UserEntity updatedUser = userRepo.save(userEntity);
		BeanUtils.copyProperties(updatedUser, returnValue);
		return returnValue;

	}

	@Override
	public void deleteUser(String userid) {

		UserEntity userEntity = userRepo.findByUserId(userid);
		if (userEntity == null)
			throw new UsernameNotFoundException(userid);
		userRepo.delete(userEntity);
	}

	@Override
	public List<UserDto> getUsers(int page, int limit) {
		List<UserDto> returnValue = new ArrayList<>();
		if(page >0)  page = page-1;
		Pageable pageableRequest = PageRequest.of(page, limit);

		Page<UserEntity> usersPage = userRepo.findAll(pageableRequest);
		List<UserEntity> users = usersPage.getContent();

		for (UserEntity userEntity : users) {
			UserDto userDto = new UserDto();
			BeanUtils.copyProperties(userEntity, userDto);
			returnValue.add(userDto);
		}

		return returnValue;
	}

}
