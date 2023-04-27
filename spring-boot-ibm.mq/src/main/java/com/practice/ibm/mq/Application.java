package com.practice.ibm.mq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.practice.ibm.mq.listener.DemoMessageListenerApplication;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		ConfigurableApplicationContext  applicationContect = SpringApplication.run(Application.class, args);
		
		DemoMessageListenerApplication demoMessageListenerApplication = applicationContect.getBean(DemoMessageListenerApplication.class);
		demoMessageListenerApplication.startMQListenerMainApp();
		
	}

}
