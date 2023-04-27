package com.practice.ibm.mq.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Data;

@Data
@Configuration
@PropertySource(value={"classpath:ibm-mq-config.properties"})
public class JmsConfig {
	
	@Value("${ibm.mq.hostname}")
	private String hostname;
	
	@Value("${ibm.mq.port}")
	private Integer port;
	
	@Value("${ibm.mq.channel}")
	private String channel;
	
	@Value("${ibm.mq.queueManager}")
	private String queueManager;
	
	@Value("${ibm.mq.username}")
	private String username;
	
	@Value("${ibm.mq.password}")
	private String password;
	
	@Value("${ibm.mq.queueName}")
	private String queueName;
	
}
