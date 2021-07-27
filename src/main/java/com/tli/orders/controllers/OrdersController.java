package com.tli.orders.controllers;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tli.orders.DTO.LineItemDTO;
import com.tli.orders.DTO.OrderDTO;
import com.tli.orders.services.OrderService;

@RestController
@RequestMapping("/order/")
public class OrdersController {

	@Autowired
	private OrderService orderService;

	private static final Logger LOGGER = LoggerFactory.getLogger(OrdersController.class);

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String viewOrder(@PathVariable("id") String id) {

		String toReturn = null;

		toReturn = orderService.viewOrder(id);

		if (StringUtils.isBlank(toReturn)) {
			JSONObject emptyResult = new JSONObject();
			emptyResult.put("id", id);
			emptyResult.put("message", "No order found with this ID");
			return emptyResult.toString();
		}
		//when returning JSONObject it gave me an error with 406. 
		//could not figure out why it was happening
		return toReturn.toString();
	}

	@RequestMapping(value = "/placeOrder", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String placeOrder(@RequestBody OrderDTO order) {
		return orderService.createOrder(order);
	}

	@DeleteMapping("/removeItem")
	public String removeLineItem(@RequestBody LineItemDTO lineItem) {
		return new JSONObject().put("removed", orderService.removeItem(lineItem)).toString();
	}

	@PutMapping("/changeQuantity")
	public String changeQuantity(@RequestBody LineItemDTO lineItem) {
		
		JSONObject results = orderService.changeQuantity(lineItem);
		if(results == null) {
			results = new JSONObject();
			results.put("modified", false);
			return results.toString();
		}
		return orderService.changeQuantity(lineItem).toString();
	}

	@PostMapping("/cancelOrder")
	public String cancelOrder(@RequestBody OrderDTO orderDTO) {
		JSONObject orderCancelled = new JSONObject();
		
		orderCancelled.put("orderStatus", orderService.cancelOrder(orderDTO));
		
		return orderCancelled.toString();
	}

	private ResponseEntity<JSONObject> okResponse() {
		return new ResponseEntity<JSONObject>(HttpStatus.OK);
	}

	private ResponseEntity<JSONObject> iseResponse() {
		return new ResponseEntity<JSONObject>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
