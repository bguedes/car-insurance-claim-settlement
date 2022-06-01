package com.cloudera.demo.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.cloudera.demo.model.Claim;

@Configuration
public class KafkaConfig {

    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Bean
    public ProducerFactory<String,Claim> producerFactory(){
        Map<String,Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"101.200.190.69:9092");
		//config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"192.168.1.10:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory(config);
    }

    @Bean
    public KafkaTemplate<String, Claim> kafkaTemplate(){
        return new KafkaTemplate<String, Claim>(producerFactory());
    }

}
