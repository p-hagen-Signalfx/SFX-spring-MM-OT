package com.sfx.ph.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import brave.sampler.Sampler;
import brave.SpanCustomizer;


@SpringBootApplication
public class Microservice2Application {

	public static void main(String[] args) {
		SpringApplication.run(Microservice2Application.class, args);
	}
	@Bean
	public Sampler defaultSampler() {
		return Sampler.ALWAYS_SAMPLE;
	}
}
@RestController
class Microservice2Controller{
	
	@Autowired 
	SpanCustomizer span;	

	@Autowired
	RestTemplate restTemplate;
	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
	
	private String sVersion =  "1.2";   	 // example fields that will be passed as tags
    private String SCustomerType =  "Gold";  // example fields that will be passed as tags
	private  final Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	@GetMapping(value="/microservice2")
	public String method2() {
		LOG.info("Inside method2");
		span.tag("Version", sVersion);  // sending tag along in the span. usefull for development
		span.tag("CustomerType",SCustomerType);
		String baseUrl = "http://localhost:8082/microservice3";
		String response=(String) restTemplate.exchange(baseUrl,
				HttpMethod.GET, null,String.class).getBody();
		LOG.info("The response recieved by method2 is "+ response);
		span.tag ("Response",response);
		return response;
	}
}