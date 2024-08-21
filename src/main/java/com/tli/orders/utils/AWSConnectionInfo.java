package com.tli.orders.utils;

import org.springframework.beans.factory.annotation.Value;

public class AWSConnectionInfo {

	@Value("aws.queueName")
	private String queueName;
	
	
//	public void getConn() throws JMSException {
//		SQSConnectionFactory connFactory = new SQSConnectionFactory(new ProviderConfiguration(),
//			AmazonSQSClientBuilder.defaultClient());
//		
//		SQSConnection connection = connFactory.createConnection();
//		
//		AmazonSQSMessagingClientWrapper client = connection.getWrappedAmazonSQSClient();
//		
//		if(!client.queueExists(queueName)) {
//			client.createQueue(queueName);
//		}
//	}
}
