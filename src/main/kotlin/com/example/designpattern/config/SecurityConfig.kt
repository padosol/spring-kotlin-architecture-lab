package com.example.designpattern.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig {

	@Bean
	fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
		http.csrf { csrf -> csrf.disable() }
		http.authorizeHttpRequests { requests -> requests.anyRequest().permitAll() }
		return http.build()
	}
}
