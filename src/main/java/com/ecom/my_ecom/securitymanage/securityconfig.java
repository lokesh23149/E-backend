package com.ecom.my_ecom.securitymanage;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.ecom.my_ecom.jwtsecurity.jwtFilter;
import com.ecom.my_ecom.services.customedetailservice;



@Configuration
@EnableWebSecurity
public class securityconfig {
	
	@Autowired
	private jwtFilter jwtfilter;
	
	@Value("${cors.allowed.origins:http://localhost:5173,http://localhost:5174,http://localhost:3000}")
	private String allowedOrigins;
	
	@Bean
public DefaultSecurityFilterChain securitylock(HttpSecurity http) throws Exception {
    http
    .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/actuator/health", "/actuator/info").permitAll()
            .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
            .requestMatchers(HttpMethod.PUT, "/auth/profile").authenticated()
            .requestMatchers(HttpMethod.POST, "/api/order").authenticated()
            .requestMatchers("/api/order/user/orders").authenticated()
            .requestMatchers("/api/order/admin/orders").authenticated()
            .requestMatchers(HttpMethod.POST, "/api/wishlist/add/**").authenticated()
            .requestMatchers(HttpMethod.DELETE, "/api/wishlist/remove/**").authenticated()
            .requestMatchers("/api/users/**").authenticated()
            .requestMatchers("/api/dashboard/**").authenticated()
            .requestMatchers(HttpMethod.POST, "/api/chat").permitAll()
            .requestMatchers("/dashboard").permitAll()
            .requestMatchers("/").permitAll()
            .anyRequest().permitAll()
        )
        .csrf(c -> c.disable())
        .sessionManagement(se -> se.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterBefore(jwtfilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
}


@Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    
    List<String> origins = Arrays.asList(allowedOrigins.split(","));
    config.setAllowedOrigins(origins);
    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
    config.setAllowedHeaders(List.of("*"));
    config.setAllowCredentials(true);
    config.setMaxAge(3600L);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);

    return source;
}



	@Bean
	public UserDetailsService userdetil() {
		return new customedetailservice();
	}

	@Bean
	public DaoAuthenticationProvider authendication() {
		DaoAuthenticationProvider dao = new DaoAuthenticationProvider();
		dao.setUserDetailsService(userdetil());
		dao.setPasswordEncoder(passEncoder());
		;

		return dao;
	}

	@Bean
	public PasswordEncoder passEncoder() {
		return new BCryptPasswordEncoder();

	}

	@Bean
	public AuthenticationManager authencationmanager() {
		return new ProviderManager(List.of(authendication()));
	}
}