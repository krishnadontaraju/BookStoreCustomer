package com.bookstore.customers.utility;

import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;

@Component
public class TokenUtility {

	private static final String TOKEN_SECRET = "ENCRYPTED";

	public  String createToken(Long id)   {
	       try {
	        //to set algorithm
	        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
	 
	        String token = JWT.create()
	        .withClaim("user_id", id)
	        .sign(algorithm);
	        return token;
	        } catch (JWTCreationException exception) {
	        exception.printStackTrace();
	           //log Token Signing Failed
	        } catch (IllegalArgumentException e) {
	// TODO Auto-generated catch block
e.printStackTrace();
	}
	       return null;
	 }

	public Long decodeToken(String token)
	{
		Long userid;
		//for verification algorithm
		Verification verification = null;
		try {
			verification = JWT.require(Algorithm.HMAC256(TOKEN_SECRET));
		} catch (IllegalArgumentException  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JWTVerifier jwtverifier=verification.build();
		//to decode token
		DecodedJWT decodedjwt=jwtverifier.verify(token);


		Claim claim=decodedjwt.getClaim("user_id");
		userid=claim.asLong();
		return userid;

	}
	
	public  String createToken(String emailAddress)   {
	       try {
	        //to set algorithm
	        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
	 
	        String token = JWT.create()
	        .withClaim("emailAddress", emailAddress)
	        .sign(algorithm);
	        return token;
	        } catch (JWTCreationException exception) {
	        exception.printStackTrace();
	           //log Token Signing Failed
	        } catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
	        	e.printStackTrace();
			}
		       return null;
		 }
	
	public String decodeTokenWithEmail(String token)
	{
		String emailAddress;
		//for verification algorithm
		Verification verification = null;
		try {
			verification = JWT.require(Algorithm.HMAC256(TOKEN_SECRET));
		} catch (IllegalArgumentException  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JWTVerifier jwtverifier=verification.build();
		//to decode token
		DecodedJWT decodedjwt=jwtverifier.verify(token);


		Claim claim=decodedjwt.getClaim("emailAddress");
		emailAddress=claim.asString();
		return emailAddress;

	}
}
