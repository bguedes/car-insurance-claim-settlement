package com.cloudera.demo.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Response {
	
	private String carDetected;
	private String severity;
	private String localization;
	private String isDamaged;

}
