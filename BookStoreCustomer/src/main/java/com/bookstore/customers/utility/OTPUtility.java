package com.bookstore.customers.utility;

import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class OTPUtility {
	
	public int getOtp() {
	    Random random = new Random();
	    int number = random.nextInt(899999) + 100000;
	    return number;
	}

}
