package com.tli.orders.services;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.tli.orders.DTO.LineItemDTO;
import com.tli.orders.DTO.OrderDTO;

@Service
public interface OrderService {

	String viewOrder(String orderId) throws NumberFormatException;

	String createOrder(OrderDTO order);
	
	boolean removeItem(LineItemDTO lineItemDTO);
	
	JSONObject changeQuantity(LineItemDTO lineItemDTO);
	
	boolean cancelOrder(OrderDTO orderDTO);
}
