package tli.orders.listeners;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;

import com.tli.orders.services.OrderService;

public class OrdersListener implements MessageListener {

	@Autowired
	private OrderService orderService;
	
	@Override
	public void onMessage(Message message) {

		try {
			message.acknowledge();
			
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
