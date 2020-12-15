package com.ibm.cart.client;

import org.springframework.stereotype.Component;


@Component
public class TokenAuthorizationFallback implements TokenAuthorizationClient {

	@Override
	public boolean validateToken(String token) {
		System.out.println("-----------------TokenAuthorizationFallback :: validateToken--------------");
		return false;
	}
}
