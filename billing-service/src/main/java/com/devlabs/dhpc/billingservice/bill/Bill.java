package com.devlabs.dhpc.billingservice.bill;

import com.devlabs.dhpc.billingservice.customer.Customer;
import com.devlabs.dhpc.billingservice.productitem.ProductItem;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;
import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Bill {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	private LocalDateTime billingDate;
	@OneToMany(mappedBy = "bill")
	private Collection<ProductItem> items;
	@Transient
	private Customer customer;
	@JsonProperty(access = WRITE_ONLY)
	private Long customerId;
}
