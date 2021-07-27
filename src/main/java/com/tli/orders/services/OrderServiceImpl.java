package com.tli.orders.services;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tli.orders.DTO.LineItemDTO;
import com.tli.orders.DTO.OrderDTO;
import com.tli.orders.entities.LineItem;
import com.tli.orders.entities.Order;
import com.tli.orders.entities.Status;
import com.tli.orders.repo.LineItemRepo;
import com.tli.orders.repo.OrderRepo;
import com.tli.orders.repo.StatusRepo;

@Service
public class OrderServiceImpl implements OrderService {

	private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);

	@Autowired
	private OrderRepo orderRepo;
	
	@Autowired
	private LineItemRepo lineItemRepo;
	
	@Autowired
	private StatusRepo statusRepo;

	/**
	 * Grabs order with the given id. Started out assuming the id would always be an
	 * integer but if we want to change it to something with a UUID later it will be
	 * an easy change.
	 * 
	 * parameter {@link String} returns {@link JSONObject}
	 * 
	 * throws {@link NumberFormatException}
	 */
	@Override
	public String viewOrder(String orderId) throws NumberFormatException {

		if (StringUtils.isNotBlank(orderId)) {
			try {
				Order toReturn = orderRepo.findById(Integer.parseInt(orderId));
				if(toReturn == null) {
					JSONObject orderNotFound = new JSONObject();
					orderNotFound.put("orderId", orderId);
					orderNotFound.put("message", "orderNotFound");
					return orderNotFound.toString();
				}
				List<LineItem> itemsList= lineItemRepo.findByOrderId(toReturn.getId());
				
				JSONArray itemsToAdd = new JSONArray();
				for(LineItem li : itemsList) {
					itemsToAdd.put(new JSONObject(li.getPertinentData()));
				}
				
				JSONObject builtOrder = new JSONObject(toReturn.getPertinentDate());
				builtOrder.put("items", itemsToAdd);
				
				return builtOrder.toString();
			} catch (NumberFormatException e) {
				LOGGER.error("Failed to find order for {}. As this is not a number. Please try again.", orderId);
			}
		}
		return null;
	}

	/**
	 * Creates an order based on the body of request. If the order can't be created for whatever reason
	 * 	false will be returned and an internal server error will be the response.
	 * 
	 * {@link LineItem} numbers can not be duplicated. If a duplicate is found an error will be given.
	 * 	The service will return false with a message of the reason why the order wasn't saved.
	 * 
	 * 
	 * parameter {@link OrderDTO}
	 * return {@link boolean} returns false if there are duplicate line item numbers.
	 */
	@Override
	public String createOrder(OrderDTO saveMe) {

		Order newOrder = new Order();
		newOrder.setCreatedDate(new Date());
		newOrder.setStatusId(1);
		newOrder.setCreatedBy(0); //This would be the userId
		newOrder.setModifiedBy(0); //This would be the userId
		newOrder.setModifiedDate(new Date());
		
		newOrder = orderRepo.save(newOrder);
		
		LineItem newItem = new LineItem();

		List<LineItemDTO> ja = saveMe.getLineItems();
		
		JSONArray savedItems = new JSONArray();
		
		int itemNum = 1;
		for(LineItemDTO item : ja) {
			newItem.setName(item.getName());
			newItem.setPrice(item.getPrice() < 0 ? new BigDecimal(0) : new BigDecimal(item.getPrice()));
			newItem.setQuantity(item.getQuantity() == 0 ? 1 : item.getQuantity());
			newItem.setOrderId(newOrder.getId());			
			newItem.setNumber(itemNum++);
			newItem.setModifiedBy(0);
			newItem.setCreatedBy(0);
			newItem.setCreatedDate(new Date());
			newItem.setModifiedDate(new Date());
			
			//You can save all of them together as a single list so you don't have too many transaction calls.
			//I think you'd have to just create a save(List<ListItem>) in the repo class and it will do it for you.
			//Leaving like this for now.
			LineItem saved = lineItemRepo.save(newItem);
			
			
			JSONObject addToList = new JSONObject(saved.getPertinentData());
			savedItems.put(addToList);
		}
		
		JSONObject orderInfo = new JSONObject();
		orderInfo.put("id", newOrder.getId());
		orderInfo.put("status", statusRepo.findById(newOrder.getStatusId()).getName());
		orderInfo.put("lineItems", savedItems);
		
		return orderInfo.toString();
	}

	/**
	 * Removes the requested line item from an order.
	 * Line Items can not be removed if the order is In Transit or Delivered.
	 * 
	 * todo: move this over to another service revolving around lineItems
	 * 
	 * parameters {@link LineItemDTO}
	 * returns boolean true if removed false if not(for whatever reason)
	 */
	@Override
	public boolean removeItem(LineItemDTO lineItemDTO) {

		Order associatedOrder = orderRepo.findById(lineItemDTO.getOrderId());
		if(associatedOrder == null || !isOrderAvailable(associatedOrder.getStatusId())) {
			return false;
		}
		
		Integer removed = lineItemRepo.deleteByOrderIdAndNumber(lineItemDTO.getOrderId(), lineItemDTO.getNumber());
				
		return removed != null && removed.intValue() > 0;
	}

	private boolean isOrderAvailable(int statusId) {
		Status currentOrderStatus = statusRepo.findById(statusId);
		
		if(StringUtils.equalsIgnoreCase("in transit", currentOrderStatus.getName()) || StringUtils.equalsIgnoreCase("delivered", currentOrderStatus.getName())) {
			return false;
		}
		
		return true;
	}

	/**
	 * 
	 * Changes the Quantity of items to be shipped on a line item.
	 * Line Items can not be modified if the order is In Transit or Delivered.
	 * 
	 * todo: move this over to another service revolving around lineItems
	 * 
	 * parameters {@link LineItemDTO}
	 * returns {@link JSONObject} of the line item. Check to see if the quantity changed 
	 * 		update user and let them know if it has changed
	 * 
	 * Returns null if there were no lineItems with the orderId and number sent over.
	 * 
	 */
	@Override
	public JSONObject changeQuantity(LineItemDTO lineItemDTO) {

		LineItem li = lineItemRepo.findByOrderIdAndNumber(lineItemDTO.getOrderId(), lineItemDTO.getNumber());
		
		Order associatedOrder = orderRepo.findById(lineItemDTO.getOrderId());
		
		if(associatedOrder == null || !isOrderAvailable(associatedOrder.getStatusId())) {
			//if order isn't available
			return null;
		}
		
		if(li != null) {
			li.setQuantity(lineItemDTO.getQuantity());
			li = lineItemRepo.save(li);
			return new JSONObject(li.getPertinentData());
		}
		
		return null;
	}

	/**
	 * 
	 * Cancels an order if it is eligible to be cancelled.
	 * 
	 * parameters {@link OrderDTO}
	 * returns boolean true if cancelled otherwise order is either in transit, delivered, or system error.
	 */
	@Override
	public boolean cancelOrder(OrderDTO orderDTO) {

		Order associatedOrder = orderRepo.findById(orderDTO.getId());
		
		if(associatedOrder == null || !isOrderAvailable(associatedOrder.getStatusId())) {
			//order not cancelled if not found, is in transit or delivered.
			return false;
		}
				
		lineItemRepo.deleteByOrderId(associatedOrder.getId());
		orderRepo.delete(associatedOrder);
		
		orderRepo.flush();
		
		return orderRepo.findById(orderDTO.getId()) == null;
	}
}
