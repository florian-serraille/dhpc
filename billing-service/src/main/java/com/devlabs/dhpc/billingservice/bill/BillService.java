package com.devlabs.dhpc.billingservice.bill;

import com.devlabs.dhpc.billingservice.customer.CustomerServiceClient;
import com.devlabs.dhpc.billingservice.inventory.InventoryServiceClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BillService {
	
	private final BillRepository billRepository;
	private final CustomerServiceClient customerService;
	private final InventoryServiceClient inventoryService;
	
	Bill findById(final Long id) {
		
		final Bill bill = billRepository.findById(id).orElseThrow(BillNotFoundException::new);
		bill.setCustomer(customerService.findCustomerById(bill.getCustomerId()));
		bill.getItems().forEach(item -> item.setProduct(inventoryService.findProductById(item.getProductId())));
		
		return bill;
	}
}
