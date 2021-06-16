package com.bookstore.customers.service;

import com.bookstore.customers.DTO.BookDTO;
import com.bookstore.customers.response.Response;

public interface ICustomerService {

	Response addCustomer(BookDTO customerDTO);

	Response updateCustomer(BookDTO customerDTO, String token);

	Response viewCustomers(String token);

	Response viewCustomer(String token);

	Response removeCustomer(String token);

	Response verifyCustomerWithOtp(int otp, String token);

	Response sendOtpToCustomer(String token);

	Response purchaseSubscriptionForCustomer(String token);

	Response emailCustomerSubscriptionExpiry(String token);
	
	

}
