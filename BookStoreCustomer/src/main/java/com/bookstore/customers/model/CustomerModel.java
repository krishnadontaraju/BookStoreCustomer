package com.bookstore.customers.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.bookstore.customers.DTO.BookDTO;

import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class CustomerModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private String firstName;
	private String lastName;
	private String kyc;
	private String password;
	private LocalDate dateOfBirth;
	private String emailAddress;
	private boolean isVerified;
	private int otp;
	private LocalDateTime purchasedDateTime;
	private LocalDateTime expiryDateTime;

	private LocalDateTime updateDateTime;
	
	public void updatecustomerDetails(BookDTO customerDTO) {

		this.firstName = customerDTO.getFirstName();
		this.lastName = customerDTO.getLastName();
		this.password = customerDTO.getPassword();
		this.kyc = customerDTO.getKyc();
		this.dateOfBirth = customerDTO.getDateOfBirth();
		
		this.updateDateTime = LocalDateTime.now();
	}
	
}
