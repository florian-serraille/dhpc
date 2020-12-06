package com.devlabs.dhpc.billingservice;

import com.devlabs.dhpc.billingservice.bill.Bill;
import com.devlabs.dhpc.billingservice.bill.BillRepository;
import com.devlabs.dhpc.billingservice.customer.Customer;
import com.devlabs.dhpc.billingservice.customer.CustomerServiceClient;
import com.devlabs.dhpc.billingservice.inventory.InventoryServiceClient;
import com.devlabs.dhpc.billingservice.inventory.Product;
import com.devlabs.dhpc.billingservice.productitem.ProductItem;
import com.devlabs.dhpc.billingservice.productitem.ProductItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.hateoas.PagedModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

@Profile("dev")
@Configuration
@Slf4j
public class DevConfiguration {
	
	@Bean
	CommandLineRunner start(final CustomerServiceClient customerService,
	                        final InventoryServiceClient inventoryService,
	                        final BillRepository billRepository,
	                        final ProductItemRepository productItemRepository) {
		
		return args -> {
			
			final Customer customer = customerService.findCustomerById(1L);
			final PagedModel<Product> products = inventoryService.findAll(0, 10);
			
			final Bill bill = billRepository.save(new Bill(null, LocalDateTime.now(), null, null, customer.getId()));
			
			products.forEach(product -> {
				final int quantity = new Random().nextInt( 10 ) + 1;
				productItemRepository.save(
						new ProductItem(null, product, product.getId(),
						                product.getPrice().multiply(BigDecimal.valueOf(quantity)), quantity, bill));
			});
		};
	}
}
