package com.bookstore.customers.DTO;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.springframework.format.annotation.DateTimeFormat;

import com.sun.istack.NotNull;

import lombok.Data;

@Data
public class BookDTO {

	@NotNull
	@NotBlank
	@Pattern(regexp = "^([A-Z]){1,}([a-zA-Z]){2,}$" , message = "Name should start with a Capital letter and should contain atleast 3 letters.")
	private String firstName;
	@NotNull
	@NotBlank
	@Pattern(regexp = "^([A-Z]){1,}([a-zA-Z]){2,}$" , message = "Name should start with a Capital letter and should contain atleast 3 letters.")
	private String lastName;
	private String kyc;
	@NotNull
	@NotBlank
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()]).{8,}$" , message = "password msut contain min. 8 digits, at least one Capital letter,at least one number,at least one special character")
	private String password;
	@DateTimeFormat(pattern = "dd MMM yyyy")
	@Past
	private LocalDate dateOfBirth;
	private String emailAddress;
	
}
