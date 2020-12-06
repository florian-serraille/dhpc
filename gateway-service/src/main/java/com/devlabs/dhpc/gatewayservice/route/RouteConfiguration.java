package com.devlabs.dhpc.gatewayservice.route;

import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.cloud.gateway.discovery.DiscoveryClientRouteDefinitionLocator;
import org.springframework.cloud.gateway.discovery.DiscoveryLocatorProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfiguration {
	
	//	@Bean
	//	public RouteLocator gatewayRoutes(final RouteLocatorBuilder builder) {
	
	/// Static configuration with service discovery
	//		return builder.routes()
	//		              .route(route -> route.path("/customer/**").uri("lb://CUSTOMER-SERVICE"))
	//		              .route(route -> route.path("/products/**").uri("lb://INVENTORY-SERVICE"))
	//		              .build();
	
	/// Static configuration without service discovery
	//		return builder.routes()
	//		              .route(route -> route.path("/customer/**").uri("http://localhost:8081/"))
	//		              .route(route -> route.path("/products/**").uri("http://localhost:8082/"))
	//		              .build();
	//	}
	
	
	/// Dynamic configuration with service discovery
	@Bean
	public DiscoveryClientRouteDefinitionLocator dynamicRoutes(final ReactiveDiscoveryClient discoveryClient,
	                                                           final DiscoveryLocatorProperties locatorProperties) {
		
		return new DiscoveryClientRouteDefinitionLocator(discoveryClient, locatorProperties);
	}
	
}
