package com.sergeytutorial.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sergeytutorial.dto.AddressDto;
import com.sergeytutorial.entities.AddressEntity;
import com.sergeytutorial.entities.UserEntity;
import com.sergeytutorial.repository.AddressRepository;
import com.sergeytutorial.repository.UserRepository;
import com.sergeytutorial.service.AddressService;

@Service
public class AddressServiceImpl implements AddressService {

	@Autowired
	AddressRepository addressRepository;
	
	@Autowired
	UserRepository userRepo;

	@Override
	public List<AddressDto> getAddresses(String userid) {
		List<AddressDto> returnValue = new ArrayList<>();
		ModelMapper modelMapper = new ModelMapper();

		UserEntity userEntity = userRepo.findByUserId(userid);
		if (userEntity == null)
			return returnValue;

		Iterable<AddressEntity> addresses = addressRepository.findAllByUserDetails(userEntity);
		for (AddressEntity addressEntity : addresses) {
			returnValue.add(modelMapper.map(addressEntity, AddressDto.class));
		}

		return returnValue;
	}

}
