package com.tli.orders.controllers;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tli.orders.DTO.LineItemDTO;
import com.tli.orders.services.LineItemService;

@RestController
@RequestMapping("/lineItem/")
public class LineItemsController {


	@Autowired
	private LineItemService lineItemService;

	@DeleteMapping("/removeItem")
	public String removeLineItem(@RequestBody LineItemDTO lineItem) {
		return new JSONObject().put("removed", lineItemService.removeItem(lineItem)).toString();
	}

	@PutMapping("/changeQuantity")
	public LineItemDTO changeQuantity(@RequestBody LineItemDTO lineItem) {
			
		return lineItemService.changeQuantity(lineItem);
		
	}

}
