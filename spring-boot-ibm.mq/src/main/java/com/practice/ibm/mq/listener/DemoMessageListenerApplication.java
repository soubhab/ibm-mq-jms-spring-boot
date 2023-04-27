/*
* (c) Copyright IBM Corporation 2018
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

// This application shows an example of using a listener to receive messages from a queue.

package com.practice.ibm.mq.listener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;

import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsFactoryFactory;
import com.ibm.msg.client.wmq.WMQConstants;
import com.practice.ibm.mq.configuration.JmsConfig;

@Component("demoMessageListenerApplication")
public class DemoMessageListenerApplication {

	// Create variables for the connection to MQ
	/*
	 * private final String HOST = "localhost"; // Host name or IP address private
	 * final int PORT = 1414; // Listener port for your queue manager private final
	 * String CHANNEL = "DEV.APP.SVRCONN"; // Channel name private final String QMGR
	 * = "QM1"; // Queue manager name private final String APP_USER = "app"; // User
	 * name that application uses to connect to MQ private final String APP_PASSWORD
	 * = "passw0rd"; // Password that the application uses to connect to MQ private
	 * final String QUEUE_NAME = "DEV.QUEUE.1"; // Queue that the application uses
	 * to put and get messages to and from
	 */
	@Autowired
	private JmsConfig jmsConfig;

	public void startMQListenerMainApp() {
		System.out.println("++++++++++ Inside startMQListenerMainApp() ++++++++");
		// Declare JMS 2.0 objects
		JMSContext context;
		Destination destination; // The destination will be a queue, but could also be a topic
		JMSConsumer consumer;

		if (jmsConfig == null) {
			System.out.println("jmsConfig is null");
		} else {
			System.out.println("jmsConfig is not null");
		}

		System.out.println("MQ Test: Connecting to " + jmsConfig.getHostname() + ", Port " + jmsConfig.getPort()
				+ ", Channel " + jmsConfig.getChannel() + ", Connecting to " + jmsConfig.getQueueName());

		JmsConnectionFactory connectionFactory = createJMSConnectionFactory();

		setJMSProperties(connectionFactory);

		System.out.println("MQ Test: Connecting to " + jmsConfig.getHostname() + ", Port " + jmsConfig.getPort()
				+ ", Channel " + jmsConfig.getChannel() + ", Connecting to " + jmsConfig.getQueueName());

		try {
			context = connectionFactory.createContext(); // This is connection + session. The connection is started by
															// default
			destination = context.createQueue("queue:///" + jmsConfig.getQueueName()); // Set the producer and consumer
																						// destination to be the same...
																						// not true in general
			consumer = context.createConsumer(destination); // associate consumer with the queue we put messages onto

			/************ IMPORTANT PART ******************************/
			MessageListener ml = new DemoMessageListener(); // Creates a listener object
			consumer.setMessageListener(ml); // Associates listener object with the consumer

			// The message listener will now listen for messages in a separate thread (see
			// MyMessageListener.java file)
			System.out.println("The message listener is running."); // (Because the connection is started by default)
			// The messaging system is now set up
		} catch (Exception e) {
			// if there is an associated linked exception, print it. Otherwise print the
			// stack trace
			if (e instanceof JMSException) {
				JMSException jmse = (JMSException) e;
				if (jmse.getLinkedException() != null) {
					System.out.println("!! JMS exception thrown in application main method !!");
					System.out.println(jmse.getLinkedException());
				} else {
					jmse.printStackTrace();
				}
			} else {
				System.out.println("!! Failure in application main method !!");
				e.printStackTrace();
			}
		}
	}

	private JmsConnectionFactory createJMSConnectionFactory() {
		JmsFactoryFactory ff;
		JmsConnectionFactory cf;
		try {
			ff = JmsFactoryFactory.getInstance(WMQConstants.WMQ_PROVIDER);
			cf = ff.createConnectionFactory();
		} catch (JMSException jmse) {
			System.out.println("JMS Exception when trying to create connection factory!");
			if (jmse.getLinkedException() != null) { // if there is an associated linked exception, print it. Otherwise
														// print the stack trace
				System.out.println(((JMSException) jmse).getLinkedException());
			} else {
				jmse.printStackTrace();
			}
			cf = null;
		}
		return cf;
	}

	private void setJMSProperties(JmsConnectionFactory cf) {
		try {
			cf.setStringProperty(WMQConstants.WMQ_HOST_NAME, jmsConfig.getHostname());
			cf.setIntProperty(WMQConstants.WMQ_PORT, jmsConfig.getPort());
			cf.setStringProperty(WMQConstants.WMQ_CHANNEL, jmsConfig.getChannel());
			cf.setIntProperty(WMQConstants.WMQ_CONNECTION_MODE, WMQConstants.WMQ_CM_CLIENT);
			cf.setStringProperty(WMQConstants.WMQ_QUEUE_MANAGER, jmsConfig.getQueueManager());
			// cf.setStringProperty(WMQConstants.WMQ_APPLICATIONNAME, "JmsPutGet (JMS)");
			cf.setBooleanProperty(WMQConstants.USER_AUTHENTICATION_MQCSP, true);
			cf.setStringProperty(WMQConstants.USERID, jmsConfig.getUsername());
			cf.setStringProperty(WMQConstants.PASSWORD, jmsConfig.getPassword());
		} catch (JMSException jmse) {
			System.out.println("JMS Exception when trying to set JMS properties!");
			if (jmse.getLinkedException() != null) { // if there is an associated linked exception, print it. Otherwise
														// print the stack trace
				System.out.println(((JMSException) jmse).getLinkedException());
			} else {
				jmse.printStackTrace();
			}
		}
		return;
	}
}
