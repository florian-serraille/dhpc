package com.devlabs.dhpc.customerservice;

import com.devlabs.dhpc.customerservice.customer.Customer;
import com.devlabs.dhpc.customerservice.customer.CustomerRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;

@Profile("dev")
@Configuration
public class DevConfiguration {
	
	@Bean
	CommandLineRunner start(final CustomerRepository customerRepository) {
		
		return args -> customerRepository.saveAll(
				Arrays.asList(new Customer(1L, "James Bond", "james.bond@mi6.uk"),
				              new Customer(2L, "Major Gerald", "major.gerald@@legion-etrangere.com")));
	}
}
