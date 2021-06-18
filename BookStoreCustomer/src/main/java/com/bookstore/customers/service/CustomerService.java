
package com.bookstore.customers.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.bookstore.customers.DTO.BookDTO;
import com.bookstore.customers.exception.CustomerServiceException;
import com.bookstore.customers.model.CustomerModel;
import com.bookstore.customers.repository.CustomerRepository;
import com.bookstore.customers.response.Response;
import com.bookstore.customers.utility.EmailUtility;
import com.bookstore.customers.utility.OTPUtility;
import com.bookstore.customers.utility.TokenUtility;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomerService implements ICustomerService{

	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private TokenUtility tokenManagaer;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private EmailUtility emailUtility;
	
	@Autowired
	private OTPUtility otpGenerator;
	
	
	@Override
	public Response addCustomer(BookDTO customerDTO) {
		log.info("Customer Creation Accessed");
		
		Optional<CustomerModel> doesCustomerExist = customerRepository.findByEmailAddress(customerDTO.getEmailAddress());
		//Checking if duplicates exist if not then proceeding
		if (doesCustomerExist.isEmpty()) {
			log.info("No duplicates found Initiating customer Creation");
			CustomerModel customer = mapper.map(customerDTO , CustomerModel.class);
			
			log.info("Customer Successfully created"+customer);
			//Generating Otp for verification
			int otp = otpGenerator.getOtp();
			//Generating a verification link
			
			//Setting the otp
			customer.setOtp(otp);
			customerRepository.save(customer);
			String verificationLink = emailUtility.setVerificationLink(tokenManagaer.createToken(customer.getId()) , otp);
			//Sending an email with verification link
			emailUtility.sendEmail(customerDTO.getEmailAddress() , "Succesfully registered with Book Store ,Please Verify" ,verificationLink);
			
			log.info("Successfully Sent Verification link to the mail with Otp :"+otp+" to mail "+customer.getEmailAddress());
			return new Response("Successfully added Customer" ,customer);
			
		}else {
			log.error("Duplicate has been found with an EmailId "+customerDTO.getEmailAddress());
			throw new CustomerServiceException(501 , "EmailId already exists");
		}
	}

	@Override
	public Response updateCustomer(BookDTO customerDTO, String token) {
		log.info("Accessed Customer Updation");
		long id = tokenManagaer.decodeToken(token);
		Optional<CustomerModel> doesCustomerExist = customerRepository.findById(id);
		
		if (doesCustomerExist.isPresent()) {
			log.info("Customer with email Id "+doesCustomerExist.get().getEmailAddress()+" has been found now initiating updation procedure");
			doesCustomerExist.get().updatecustomerDetails(customerDTO);
			customerRepository.save(doesCustomerExist.get());
			log.info("Customer Updation completed");
			return new Response("Successfully updated the customer details" ,doesCustomerExist.get());
		}else {
			log.error("Customer was not found for updation with Email "+customerDTO.getEmailAddress());
			throw new CustomerServiceException(501 , "Customer not found with given details");
		}
	}

	@Override
	public Response viewCustomers(String token) {
		log.info("Accessed all Customer retrieval");
		long id = tokenManagaer.decodeToken(token);
		Optional<CustomerModel> doesCustomerExist = customerRepository.findById(id);
		
		if (doesCustomerExist.isPresent()) {
			log.info("Customer with email Id "+doesCustomerExist.get().getEmailAddress()+" has been found now retrieving all Customers");
			return new Response("Customer retrieval complete" ,customerRepository.findAll());
		}else {
			log.error("Customer was not found for updation with token "+token);
			throw new CustomerServiceException(501 , "Customer not found with given details");
		}
	}
	
	

	@Override
	public Response viewCustomer(String token) {
		log.info("Accessed Customer retrieval");
		long id = tokenManagaer.decodeToken(token);
		Optional<CustomerModel> doesCustomerExist = customerRepository.findById(id);
		
		if (doesCustomerExist.isPresent()) {
			log.info("Customer with email Id "+doesCustomerExist.get().getEmailAddress()+" has been found now retrieving Customer");
			return new Response("Customer retirieved" ,doesCustomerExist.get());
		}else {
			log.error("Customer was not found with token "+token);
			throw new CustomerServiceException(501 , "Customer not found with given details");
		}
	}

	@Override
	public Response removeCustomer(String token) {
		log.info("Accessed Customer removal");
		long id = tokenManagaer.decodeToken(token);
		Optional<CustomerModel> doesCustomerExist = customerRepository.findById(id);
		
		if (doesCustomerExist.isPresent()) {
			log.info("Customer with email Id "+doesCustomerExist.get().getEmailAddress()+" has been found now removing customer");
			customerRepository.delete(doesCustomerExist.get());
			return new Response("Customer removed" ,HttpStatus.FOUND);
		}else {
			log.error("Customer was not found for removing with token "+token);
			throw new CustomerServiceException(501 , "Customer not found with given details");
		}
	}

	@Override
	public Response verifyCustomerWithOtp(int otp, String token) {
		log.info("Accessed Customer Updation");
		long id = tokenManagaer.decodeToken(token);
		Optional<CustomerModel> doesCustomerExist = customerRepository.findById(id);
		
		if (doesCustomerExist.isPresent()) {
			log.info("Customer with email Id "+doesCustomerExist.get().getEmailAddress()+" has been found now initiating updation procedure");
			if( doesCustomerExist.get().getOtp() == otp) {
				log.info("Credentials verified otp and token are verified");
				doesCustomerExist.get().setVerified(true);
				customerRepository.save(doesCustomerExist.get());
				log.info("Customer Verification Succesful");
				return new Response("Successfully verified the customer details" ,doesCustomerExist.get());
			}else {
				throw new CustomerServiceException(511 , "Verification Details could not be matched , Verification Failed");
			}}else {
			log.error("Customer was not found for verification with token "+token);
			throw new CustomerServiceException(501 , "Customer not found with given details");
		}
	}

	@Override
	public Response sendOtpToCustomer(String token) {
		log.info("Accessed Otp push");
		long id = tokenManagaer.decodeToken(token);
		Optional<CustomerModel> doesCustomerExist = customerRepository.findById(id);
		
		if (doesCustomerExist.isPresent()) {
			log.info("Customer with email Id "+doesCustomerExist.get().getEmailAddress()+" has been found now initiating Otp Pushing");
			
			int otp = otpGenerator.getOtp();
			doesCustomerExist.get().setOtp(otp);
			log.info("generated Otp for Customer with Email Id "+doesCustomerExist.get().getEmailAddress());
			String verificationLink = emailUtility.setVerificationLink(tokenManagaer.createToken(doesCustomerExist.get().getId()) , otp);
			//Sending an email with verification link
			emailUtility.sendEmail(doesCustomerExist.get().getEmailAddress() , "Succesfully registered with Book Store ,Please Verify" ,verificationLink);			customerRepository.save(doesCustomerExist.get());
			log.info("Customer Updation completed");
			return new Response("Successfully updated the customer details" ,doesCustomerExist.get());
		}else {
			log.error("Customer was not found with token "+token);
			throw new CustomerServiceException(501 , "Customer not found with given details");
		}
	}

	@Override
	public Response purchaseSubscriptionForCustomer(String token) {
		log.info("Accessed Customer Purchasing");
		long id = tokenManagaer.decodeToken(token);
		Optional<CustomerModel> doesCustomerExist = customerRepository.findById(id);
		
		if (doesCustomerExist.isPresent()) {
			log.info("Customer with email Id "+doesCustomerExist.get().getEmailAddress()+" has been found now initiating purchasing process for customer");
			
			doesCustomerExist.get().setPurchasedDateTime(LocalDateTime.now());
			doesCustomerExist.get().setExpiryDateTime(LocalDateTime.now().plusMonths(12));
			customerRepository.save(doesCustomerExist.get());
			log.info("Customer purchase process complete");
			return new Response("Successfully updated the customer purchase details" ,doesCustomerExist.get());
		}else {
			log.error("Customer was not found with token "+token);
			throw new CustomerServiceException(501 , "Customer not found with given details");
		}
	}

	@Override
	public Response emailCustomerSubscriptionExpiry(String token) {
		log.info("Accessed Customer Updation");
		long id = tokenManagaer.decodeToken(token);
		Optional<CustomerModel> doesCustomerExist = customerRepository.findById(id);
		
		if (doesCustomerExist.isPresent()) {
			log.info("Customer with email Id "+doesCustomerExist.get().getEmailAddress()+" has been found now initiating updation procedure");
			
			if (LocalDateTime.now().isBefore(doesCustomerExist.get().getExpiryDateTime())) {
				emailUtility.sendEmail(doesCustomerExist.get().getEmailAddress() ,"Your Subscription is about to expire" ,
						"Your Subscription wiht the book Stroe is about to expire please renew the subscription to enjoy the service \n"
						+ "The Expiry Date for Subscription is "+doesCustomerExist.get().getExpiryDateTime().getDayOfMonth()+"/"
								+doesCustomerExist.get().getExpiryDateTime().getMonthValue()+"/"+doesCustomerExist.get().getExpiryDateTime().getYear());
				log.info("Customer Updated about the expiry , mail sent to "+doesCustomerExist.get().getEmailAddress());
				return new Response("Successfully updated the customer about expiry" ,doesCustomerExist.get());
			}else {
				log.error("Subscription already expired");
				throw new CustomerServiceException(521 , "Subscription already expired");
			}}else {
			log.error("Customer was not found for email sending with token "+token);
			throw new CustomerServiceException(501 , "Customer not found with given details");
		}
	}

	@Override
	public boolean checkCustomer(String token) {
		log.info("Accessed Customer check");
		long id = tokenManagaer.decodeToken(token);
		Optional<CustomerModel> doesCustomerExist = customerRepository.findById(id);
		
		if (doesCustomerExist.isPresent()) {
			log.info("Customer was found returning true");
			return true;
		}else {
			log.error("Customer was not found with token "+token+" returning false");
			return false;
		}
	}

	@Override
	public Response loginCustomer(String emailAddress, String password) {
		log.info("Accesed the login of Book Store");
		Optional<CustomerModel> doesCustomerExist = customerRepository.findByEmailAddress(emailAddress);
		
		if (doesCustomerExist.isPresent()) {
			log.info("Customer exists with the email Address now, Checking the password");
			//Now checking the password with our Database
			if (doesCustomerExist.get().getPassword() == password) {
				//Customer has been verified now can proceed to the post login 
				log.info("Customer has been verified against the credentials provided");
				return new Response("Customer has been verified and Looged in" ,doesCustomerExist.get().getEmailAddress());
				}else {
					log.error("Customer has provided wrong password");
					return new Response("email or password provided are Incorrect " , "Please check again");
				}
		}else {
			log.error("Customer with email was not found "+doesCustomerExist.get().getEmailAddress());
			return new Response("email or password provided are Incorrect " , "Please check again");
		}
	}

	@Override
	public Response customerForgotPassword(String emailAddress) {
		log.info("Accessed forgot password with email "+emailAddress);
		
		Optional<CustomerModel> doesCustomerExist = customerRepository.findByEmailAddress(emailAddress);
		
		if (doesCustomerExist.isPresent()) {
			log.info("Customer was found with Email "+emailAddress+" now sending email to change email");
			
			String passwordUpdationLink = emailUtility.setPasswordUpdationLink(tokenManagaer.createToken(emailAddress));
			//Sending an email with verification link
			emailUtility.sendEmail(emailAddress , "Link to update our Password" ,""
					+ "Please follow the link to change the password\n"+passwordUpdationLink);
			return new Response("Emailed the updation link" , emailAddress);
			
		}else {
			log.error("Email provided for password Updation wsa Incorrect "+emailAddress);
			throw new CustomerServiceException(601 , "Email Address provide was not correct");
		}
	}

	@Override
	public Response updatePassword(String token , String password) {
		log.info("Accessed Password updation");
		
		String email = tokenManagaer.decodeTokenWithEmail(token);
		CustomerModel customer = customerRepository.findByEmailAddress(email).get();
		customer.setPassword(password);
		customerRepository.save(customer);
		log.info("Password has been Changed for Customer "+customer.getEmailAddress());
		return new Response("Password has been updated" ,customer.getEmailAddress());
		
		
	}

}
