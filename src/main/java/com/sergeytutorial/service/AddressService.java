package com.sergeytutorial.service;

import java.util.List;

import com.sergeytutorial.dto.AddressDto;

public interface AddressService {
	
List <AddressDto> getAddresses(String usersid);
	
	
}
