package com.bookstore.customers.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.customers.DTO.BookDTO;
import com.bookstore.customers.response.Response;
import com.bookstore.customers.service.ICustomerService;

import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/customer")
public class CustomerController {

	@Autowired
	private ICustomerService userService;
	
	
	@ApiOperation("End point to add Customer")
	@PostMapping("/addCustomer")
	public ResponseEntity<Response> addCustomer(@RequestBody BookDTO customerDTO) {
		
		Response addCustomerResponse = userService.addCustomer(customerDTO);
		return new ResponseEntity<Response> (addCustomerResponse , HttpStatus.CREATED);
		
	}
	
	//End pint to update the customer details
	
	@PutMapping("/updateCustomer")
	public ResponseEntity<Response> updateCustomer(@RequestBody BookDTO customerDTO , @RequestHeader String token) {
		
		Response updateCustomerResponse = userService.updateCustomer(customerDTO , token);
		return new ResponseEntity<Response> (updateCustomerResponse , HttpStatus.OK);
		
	}
	
	//End point to view all customers
	
	@GetMapping(value = {"" ,"/" , "/viewCustomers"})
	public ResponseEntity<Response> viewCustomers(@RequestHeader String token) {
		
		Response viewCustomersResponse = userService.viewCustomers(token);
		return new ResponseEntity<Response> (viewCustomersResponse , HttpStatus.OK);
		
	}
	
	//End point to view a particular customer
	
	@GetMapping("/viewCustomer/{token}")
	public ResponseEntity<Response> viewCustomer(@PathVariable String token) {
		
		Response viewCustomerResponse = userService.viewCustomer(token);
		return new ResponseEntity<Response> (viewCustomerResponse , HttpStatus.OK);
		
	}
	
	//End point to remove the customer
	
	@DeleteMapping("/addCustomer")
	public ResponseEntity<Response> removeCustomer(@RequestHeader String token) {
		
		Response removeCustomerResponse = userService.removeCustomer(token);
		return new ResponseEntity<Response> (removeCustomerResponse , HttpStatus.OK);
		
	}
	
	//End Point to verify Customer with Otp
	
	@RequestMapping(value = "/verify")
	public ResponseEntity<Response> verifyCustomerWithOtp(@RequestParam int otp , @RequestParam String token) {
		
		Response verifyCustomerResponse = userService.verifyCustomerWithOtp(otp , token);
		return new ResponseEntity<Response> (verifyCustomerResponse , HttpStatus.CREATED);
		
	}
	
	//End Point to send Otp to Customer
	
	@PostMapping("/sendOtp")
	public ResponseEntity<Response> sendOtpToCustomer(@RequestParam String token) {
		
		Response sendOtpToCustomerResponse = userService.sendOtpToCustomer(token);
		return new ResponseEntity<Response> (sendOtpToCustomerResponse , HttpStatus.CREATED);
		
	}
	
	//End point to Update customer's latest purchase
	
	@PostMapping("/purchase")
	public ResponseEntity<Response> purchaseSubcriptionForCustomer( @RequestParam String token) {
		
		Response subscriptionpurchaseResponse = userService.purchaseSubscriptionForCustomer(token);
		return new ResponseEntity<Response> (subscriptionpurchaseResponse , HttpStatus.CREATED);
		
	}
	
	//End point to send an email to customer about the near expire of subscription
	
	@PostMapping("/subNearExpiry")
	public ResponseEntity<Response> emailCustomerSubscriptionExpiry( @RequestParam String token) {
		
		Response emailCustomerSubscriptionExpiryResponse = userService.emailCustomerSubscriptionExpiry(token);
		return new ResponseEntity<Response> (emailCustomerSubscriptionExpiryResponse , HttpStatus.CREATED);
		
	}
	
	
	
	
}
