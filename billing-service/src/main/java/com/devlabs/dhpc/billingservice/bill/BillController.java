package com.devlabs.dhpc.billingservice.bill;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/bills", produces = APPLICATION_JSON_VALUE)
public class BillController {
	
	private final BillService billService;
	
	@GetMapping("/{id}")
	public ResponseEntity<Bill> findById(@PathVariable final Long id){
		
		return ResponseEntity.ok(billService.findById(id));
	}
	
	
}
