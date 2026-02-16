package com.ecom.my_ecom.jwtsecurity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ecom.my_ecom.services.customedetailservice;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@Component
public class jwtFilter extends OncePerRequestFilter {
	@Autowired
	private jwts j;

	@Autowired
	private customedetailservice custome;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String authead = request.getHeader("Authorization");
		if (authead == null || !authead.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;

		}

		String token = authead.substring(7);
		try {
			String username=j.extractusername(token);
		
		if(username !=null && SecurityContextHolder.getContext().getAuthentication()==null) {
			UserDetails userDetails=custome.loadUserByUsername(username);
			if (j.validatetoken(token, userDetails)) {
				UsernamePasswordAuthenticationToken authenticationtoken=
						new UsernamePasswordAuthenticationToken(userDetails,
								userDetails.getPassword(),userDetails.getAuthorities());
				authenticationtoken.setDetails(new WebAuthenticationDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authenticationtoken);
				
			}
			
		}
			
		}catch (Exception e) {
			Map<String, String> responesemap=new HashMap<String, String>();
			responesemap.put("error", "invalid token");
			ObjectMapper objmap= new  ObjectMapper();
			String jsonString =objmap.writeValueAsString(responesemap);
			response.getWriter().write(jsonString);
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		return;
		}
		filterChain.doFilter(request, response);
				
		
		}
}
