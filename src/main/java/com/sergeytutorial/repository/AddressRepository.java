package com.sergeytutorial.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sergeytutorial.entities.AddressEntity;
import com.sergeytutorial.entities.UserEntity;
@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, Long> {
	List <AddressEntity> findAllByUserDetails(UserEntity userEntity);

}
