package com.cloudera.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class CarInsuranceClaimSettlementDemo {

	public static void main(String[] args) {
		SpringApplication.run(CarInsuranceClaimSettlementDemo.class, args);
	}
}
