package tli.orders.listeners;

import antlr.debug.MessageEvent;
import antlr.debug.MessageListener;
import antlr.debug.TraceEvent;

public class OrdersListener implements MessageListener {

	@Override
	public void doneParsing(TraceEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reportError(MessageEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reportWarning(MessageEvent arg0) {
		// TODO Auto-generated method stub
		
	}

//	@Autowired
//	private OrderService orderService;
//	
//	@Override
//	public void onMessage(Message message) {
//
//		try {
//			message.acknowledge();
//			
//		} catch (JMSException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		
//	}

}
