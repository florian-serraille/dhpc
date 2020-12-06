package com.devlabs.dhpc.billingservice.customer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "CUSTOMER-SERVICE")
public interface CustomerServiceClient {
	
	@GetMapping("/customers/{id}?projection=full-customer")
	Customer findCustomerById(@PathVariable("id") Long id);
}
