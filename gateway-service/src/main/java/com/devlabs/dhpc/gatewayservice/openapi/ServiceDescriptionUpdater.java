package com.devlabs.dhpc.gatewayservice.openapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Component
public class ServiceDescriptionUpdater {
	
	private static final Logger logger = LoggerFactory.getLogger(ServiceDescriptionUpdater.class);
	
	private static final String DEFAULT_SWAGGER_URL = "/v2/api-docs";
	private static final String KEY_SWAGGER_URL = "swagger_url";
	
	private final DiscoveryClient discoveryClient;
	private final ServiceDefinitionsContext definitionContext;
	private final RestTemplate template;
	
	public ServiceDescriptionUpdater(final DiscoveryClient discoveryClient,
	                                 final ServiceDefinitionsContext definitionsContext,
	                                 final RestTemplate restTemplate) {
		
		this.discoveryClient = discoveryClient;
		this.definitionContext = definitionsContext;
		this.template = restTemplate;
	}
	
	@Scheduled(fixedDelayString = "${swagger.config.refreshrate}")
	public void refreshSwaggerConfigurations() {
		
		definitionContext.clearDefinitions();
		discoveryClient.getServices()
		               .forEach(serviceId -> {
			
			               logger.debug("Attempting service definition refresh for Service : {} ", serviceId);
			               List<ServiceInstance> serviceInstances = discoveryClient.getInstances(serviceId);
			
			               final ServiceInstance instance = serviceInstances.stream()
			                                                                .findAny()
			                                                                .orElseThrow(IllegalStateException::new);
			               final Optional<String> definition = getSwaggerDefinitionForService(serviceId, instance);
			               definition.ifPresent(json -> definitionContext.addServiceDefinition(serviceId, json));
		               });
	}
	
	private String getSwaggerURL(ServiceInstance instance) {
		
		String swaggerURL = instance.getMetadata().get(KEY_SWAGGER_URL);
		return swaggerURL != null ? instance.getUri() + swaggerURL : instance.getUri() + DEFAULT_SWAGGER_URL;
	}
	
	private Optional<String> getSwaggerDefinitionForService(String serviceName, ServiceInstance instance) {
		
		String url = getSwaggerURL(instance);
		logger.debug("Accessing the SwaggerDefinition JSON for Service : {} : URL : {} ", serviceName, url);
		
		try {
			String jsonData = template.getForObject(url, String.class);
			return Optional.ofNullable(jsonData);
			
		} catch (RestClientException ex) {
			
			logger.error("Error while getting service definition for service : {} Error : {} ", serviceName,
			             ex.getMessage());
			return Optional.empty();
		}
	}
}