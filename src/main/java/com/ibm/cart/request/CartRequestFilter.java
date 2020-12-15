package com.ibm.cart.request;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ibm.cart.service.AuthService;

@Component
public class CartRequestFilter extends OncePerRequestFilter {

	@Autowired
	AuthService authService;
	
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		boolean cc = ! request.getServletPath().contains("Cart");
		System.out.println("ProductRequestFilter :: "+ request.getServletPath() + " -->  " +  cc);
		return cc;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		if (!authService.vlidateToken(request)) {
			httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
			httpResponse.getOutputStream().flush();
			httpResponse.getOutputStream().println("Invalid token");
			throw new RuntimeException("------------ Invalid Token ---------------------");
		}

		filterChain.doFilter(httpRequest, httpResponse);
	}

}
