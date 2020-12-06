package com.devlabs.dhpc.billingservice.inventory;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "INVENTORY-SERVICE")
public interface InventoryServiceClient {

	@GetMapping("/products/{id}?projection=full-product")
	Product findProductById(@PathVariable("id") Long id);

	@GetMapping("/products?projection=full-product")
	PagedModel<Product> findAll(@RequestParam(value = "page") int page, @RequestParam(value = "size") int size);
}
