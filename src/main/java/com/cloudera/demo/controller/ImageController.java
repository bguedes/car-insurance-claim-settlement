package com.cloudera.demo.controller;

import java.io.ByteArrayInputStream;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.cloudera.demo.model.Claim;
import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Metadata;

@RestController
//@RequestMapping("producer")
public class ImageController {
	
    @Autowired
    KafkaTemplate<String, Claim> kafkaJsontemplate;
    String TOPIC_NAME = "car_insurance_claim";

    @PostMapping(value = "/producer/postImage",produces = {"application/json"})
    @Validated
    public RedirectView postJsonMessage(@NonNull @RequestParam("file") MultipartFile multipartFile, 
    		@RequestParam("customerId") String customerId,
    		@RequestParam("customerFirstName") String customerFirstName,
    		@RequestParam("customerLastName") String customerLastName,
    		@RequestParam("customerEmail") String customerEmail,
    		RedirectAttributes redirectAttrs) throws IOException, JpegProcessingException{
    	
    	System.out.println("ImageController - postJsonMessage - sending a message	");
    	String encodedImageString = Base64.getEncoder().encodeToString(multipartFile.getBytes());
    	
    	Claim claimToSend = Claim.getInstance(UUID.randomUUID().toString(), encodedImageString, new Date());
    	
    	Metadata imageMetadata = JpegMetadataReader.readMetadata(new ByteArrayInputStream(multipartFile.getBytes()));
    	claimToSend.setLocation(imageMetadata);
    	
    	
    	
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
    	
   	
        RedirectView rv = new RedirectView("/", true);
        redirectAttrs.addFlashAttribute("message", "The claim informations has been sent successfully.");
         
        return rv;
    	
        //return new ResponseEntity<>("Image sent.", HttpStatus.OK);
    }
}
