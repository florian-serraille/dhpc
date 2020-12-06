package com.devlabs.dhpc.gatewayservice.openapi;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.*;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.swagger.web.InMemorySwaggerResourcesProvider;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SwaggerUIConfiguration {

	private final ServiceDefinitionsContext definitionContext;

	public SwaggerUIConfiguration(final ServiceDefinitionsContext definitionContext) {
		this.definitionContext = definitionContext;
	}

	@Bean
	public RestTemplate configureTemptlate() {
		return new RestTemplateBuilder().build();
	}

	@Primary
	@Bean
	@Lazy
	public SwaggerResourcesProvider swaggerResourcesProvider(InMemorySwaggerResourcesProvider resourcesProvider) {
		return () -> {
			List<SwaggerResource> resources = new ArrayList<>(resourcesProvider.get());
			resources.clear();
			resources.addAll(definitionContext.getSwaggerDefinitions());
			return resources;
		};
	}
}