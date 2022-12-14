package com.sergeytutorial.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.sergeytutorial.dto.UserDto;
@Entity(name="addresses")
public class AddressEntity implements Serializable {


	private static final long serialVersionUID = -5841822087628151735L;
@Id
@GeneratedValue
private long id;
@Column (nullable = false)
private String addressId;
private String city;
private String country;
private String streetName;
private String postalCode;
private String type;
//private UserDto userDetails;
@ManyToOne
@JoinColumn(name="users_id")
private UserEntity userDetails;
public long getId() {
	return id;
}
public void setId(long id) {
	this.id = id;
}
public String getAddressId() {
	return addressId;
}
public void setAddressId(String addressId) {
	this.addressId = addressId;
}
public String getCity() {
	return city;
}
public void setCity(String city) {
	this.city = city;
}
public String getCountry() {
	return country;
}
public void setCountry(String country) {
	this.country = country;
}
public String getStreetName() {
	return streetName;
}
public void setStreetName(String streetName) {
	this.streetName = streetName;
}
public String getPostalCode() {
	return postalCode;
}
public void setPostalCode(String postalCode) {
	this.postalCode = postalCode;
}
public String getType() {
	return type;
}
public void setType(String type) {
	this.type = type;
}
public UserEntity getUserDetails() {
	return userDetails;
}
public void setUserDetails(UserEntity userDetails) {
	this.userDetails = userDetails;
}
}
