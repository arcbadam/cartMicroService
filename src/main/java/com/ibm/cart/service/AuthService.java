package com.ibm.cart.service;



import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import com.ibm.cart.client.TokenAuthorizationClient;

@Component
public class AuthService {

	@Autowired
	TokenAuthorizationClient tokenAuthClient;
	

	public boolean vlidateToken(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		boolean isValidToken = false;
		try {
			isValidToken = tokenAuthClient.validateToken(token);
		} catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
			httpClientOrServerExc.printStackTrace();
			if (HttpStatus.UNAUTHORIZED.equals(httpClientOrServerExc.getStatusCode())) {
				System.out.println("Un authorized..");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return isValidToken;
	}
	
}
