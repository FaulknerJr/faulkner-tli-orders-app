package com.tli.orders.utils;

import javax.jms.JMSException;

import org.springframework.beans.factory.annotation.Value;

import com.amazon.sqs.javamessaging.AmazonSQSMessagingClientWrapper;
import com.amazon.sqs.javamessaging.ProviderConfiguration;
import com.amazon.sqs.javamessaging.SQSConnection;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;

public class AWSConnectionInfo {

	@Value("aws.queueName")
	private String queueName;
	
	
	public void getConn() throws JMSException {
		SQSConnectionFactory connFactory = new SQSConnectionFactory(new ProviderConfiguration(),
			AmazonSQSClientBuilder.defaultClient());
		
		SQSConnection connection = connFactory.createConnection();
		
		AmazonSQSMessagingClientWrapper client = connection.getWrappedAmazonSQSClient();
		
		if(!client.queueExists(queueName)) {
			client.createQueue(queueName);
		}
	}
}
