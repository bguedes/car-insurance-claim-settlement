package com.cloudera.demo.controller;

import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cloudera.demo.model.Claim;

@RestController
//@RequestMapping("producer")
public class ImageController {
	
    @Autowired
    KafkaTemplate<String, Claim> kafkaJsontemplate;
    String TOPIC_NAME = "car_insurance_claim";

    @PostMapping(value = "/producer/postImage",produces = {"application/json"})
    @Validated
    public String postJsonMessage(@NonNull @RequestParam("file") MultipartFile multipartFile, 
    		@RequestParam("customerId") String customerId,
    		@RequestParam("customerFirstName") String customerFirstName,
    		@RequestParam("customerLastName") String customerLastName,
    		@RequestParam("customerEmail") String customerEmail) throws IOException{
    	
    	System.out.println("ImageController - postJsonMessage - sending a message	");
    	String encodedImageString = Base64.getEncoder().encodeToString(multipartFile.getBytes());
    	
    	Claim claimToSend = Claim.getInstance(UUID.randomUUID().toString(), encodedImageString, new Date());
    	
    	if(Optional.ofNullable(customerId).isPresent())
    		claimToSend.setCustomerId(customerId);
    	
    	if(Optional.ofNullable(customerId).isPresent())
    		claimToSend.setCustomerEmail(customerEmail);
    	
    	if(Optional.ofNullable(customerFirstName).isPresent())
    		claimToSend.setCustomerFirstName(customerFirstName);
    	
    	if(Optional.ofNullable(customerLastName).isPresent())
    		claimToSend.setCustomerLastName(customerLastName);   
    		
    	if(Optional.ofNullable(customerEmail).isPresent())
    		claimToSend.setCustomerEmail(customerEmail);     		
    	
    	kafkaJsontemplate.send(TOPIC_NAME, claimToSend);
        return "Message published successfully";
    }
}
