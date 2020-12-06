package com.devlabs.dhpc.inventoryservice.inventory;

import org.springframework.data.rest.core.config.Projection;

import java.math.BigDecimal;

@Projection(name = "full-product", types = Product.class)
public interface ProductProjection {
	
	Long getId();
	String getName();
	BigDecimal getPrice();
}
