package com.devlabs.dhpc.billingservice.bill;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.*;

@ResponseStatus(code = NOT_FOUND)
class BillNotFoundException extends RuntimeException {

}
