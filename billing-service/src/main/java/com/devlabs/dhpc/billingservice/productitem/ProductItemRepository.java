package com.devlabs.dhpc.billingservice.productitem;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductItemRepository extends JpaRepository<ProductItem, Long> {

	List<ProductItem> findByBillId(Long id);
}
