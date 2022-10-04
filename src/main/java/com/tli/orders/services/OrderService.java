package com.tli.orders.services;

import org.springframework.stereotype.Service;

import com.tli.orders.DTO.OrderDTO;

@Service
public interface OrderService {

	OrderDTO viewOrder(String orderId) throws NumberFormatException;

	OrderDTO createOrder(OrderDTO order);
		
	boolean cancelOrder(OrderDTO orderDTO);
	
}
