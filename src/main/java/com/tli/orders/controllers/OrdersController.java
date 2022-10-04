package com.tli.orders.controllers;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tli.orders.DTO.OrderDTO;
import com.tli.orders.services.OrderService;

@RestController
@RequestMapping("/order/")
public class OrdersController {

	@Autowired
	private OrderService orderService;
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public OrderDTO viewOrder(@PathVariable("id") String id) {

		return orderService.viewOrder(id);

	}

	@RequestMapping(value = "/placeOrder", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public OrderDTO placeOrder(@RequestBody OrderDTO order) {
		return orderService.createOrder(order);
	}

	@PostMapping("/cancelOrder")
	public String cancelOrder(@RequestBody OrderDTO orderDTO) {
		JSONObject orderCancelled = new JSONObject();
		
		orderCancelled.put("orderStatus", orderService.cancelOrder(orderDTO));
		
		return orderCancelled.toString();
	}
	
}
