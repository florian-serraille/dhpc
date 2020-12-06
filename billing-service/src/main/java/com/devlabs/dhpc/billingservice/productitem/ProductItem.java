package com.devlabs.dhpc.billingservice.productitem;

import com.devlabs.dhpc.billingservice.bill.Bill;
import com.devlabs.dhpc.billingservice.inventory.Product;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;
import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ProductItem {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	@Transient
	private Product product;
	@JsonProperty(access = WRITE_ONLY)
	private Long productId;
	private BigDecimal price;
	private int quantity;
	@ManyToOne
	@JsonProperty(access = WRITE_ONLY)
	private Bill bill;
}
