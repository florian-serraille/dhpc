package com.devlabs.dhpc.gatewayservice.openapi;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DocumentDefinitionController {

	private final ServiceDefinitionsContext definitionContext;

	public DocumentDefinitionController(final ServiceDefinitionsContext definitionContext) {
		this.definitionContext = definitionContext;
	}

	@GetMapping("/service/{serviceName}")
	public String getServiceDefinition(@PathVariable String serviceName) {
		return definitionContext.getSwaggerDefinition(serviceName);
	}
}