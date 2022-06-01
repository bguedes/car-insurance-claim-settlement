package com.cloudera.demo.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor(staticName = "getInstance")
public class Claim {
	
    @SuppressWarnings("unused")
    @NonNull
	private String claimId;
    
    @SuppressWarnings("unused")
    @NonNull    
    private String carImageBased64encoding;
    
    
	@JsonFormat(
			shape = JsonFormat.Shape.STRING, 
			pattern = "yyyy-MM-dd HH:mm:ss")
    @NonNull	
	private transient Date claimDate;    
	
	@Setter
	private String customerId;
	
	@Setter
 	private String customerLastName;
	
	@Setter
 	private String customerFirstName;
	
	@Setter
	private String customerEmail;
	
}
