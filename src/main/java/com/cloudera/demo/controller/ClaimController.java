package com.cloudera.demo.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.cloudera.demo.model.Claim;
import com.cloudera.demo.model.Response;
import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Metadata;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author bguedes
 *
 *         https://dzone.com/articles/synchronous-kafka-using-spring-request-reply-1
 */
@Controller
public class ClaimController {

	@Autowired
	RestTemplate restTemplate;
	
    @Value("${cdp.rest.url}")
    private String restUrl;

    private String result;
    
    private Response response;
    
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getIndex(Model model) throws JsonMappingException, JsonProcessingException {
    	
    	result = null;
    	response = null;
    	
		return ("redirect:/claims/");
    }      
    
    @RequestMapping(value = "/claims/", method = RequestMethod.POST)
	public String postJsonMessage(
			@NonNull @RequestParam("file") MultipartFile multipartFile,
			Model model
	) throws IOException, JpegProcessingException, InterruptedException, ExecutionException, TimeoutException {
		
    	result = null;
    	
		System.out.println("ImageController - postJsonMessage - sending a message	");
		String encodedImageString = Base64.getEncoder().encodeToString(multipartFile.getBytes());

		Claim claimToSend = new Claim(UUID.randomUUID().toString(), encodedImageString, new Date(), "customer0001",
		    null, null);

		Metadata imageMetadata = JpegMetadataReader.readMetadata(new ByteArrayInputStream(multipartFile.getBytes()));
		claimToSend.setLocation(imageMetadata);		
		
	    HttpHeaders headers = new HttpHeaders();
	    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	    HttpEntity<Claim> entity = new HttpEntity<Claim>(claimToSend,headers);		

		ResponseEntity<String> response = restTemplate.postForEntity(restUrl, entity, String.class);
		result = response.getBody();	
		
		return ("redirect:/claims/");
	}
    
    @RequestMapping(value = "/claims/", method = RequestMethod.GET)
    public String getUsers(Model model) throws JsonMappingException, JsonProcessingException {
    	
    	if(result != null) 
    	{
        	ObjectMapper objectMapper = new ObjectMapper();
        	response = objectMapper.readValue(result, Response.class);
        	model.addAttribute("result", response);
    	}
    	
      return "claims";
    }    
}
