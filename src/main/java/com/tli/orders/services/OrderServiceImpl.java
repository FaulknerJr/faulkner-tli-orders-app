package com.tli.orders.services;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tli.orders.DTO.LineItemDTO;
import com.tli.orders.DTO.OrderDTO;
import com.tli.orders.entities.LineItem;
import com.tli.orders.entities.Order;
import com.tli.orders.enums.Status;
import com.tli.orders.repo.LineItemRepo;
import com.tli.orders.repo.OrderRepo;
import com.tli.orders.utils.StatusUtils;

@Service
public class OrderServiceImpl implements OrderService {

	private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);

	@Autowired
	private OrderRepo orderRepo;
	
	@Autowired
	private LineItemRepo lineItemRepo;
	
	@Autowired
	private LineItemService lineItemService;

	/**
	 * Grabs order with the given id. Started out assuming the id would always be an
	 * long but if we want to change it to something with a UUID later it will be
	 * an easy change.
	 * 
	 * parameter {@link String} returns {@link JSONObject}
	 * 
	 * throws {@link NumberFormatException}
	 */
	@Override
	public OrderDTO viewOrder(String orderId) throws NumberFormatException {

		if (StringUtils.isNotBlank(orderId)) {
			try {
				Order matchingOrder = orderRepo.findById(Integer.parseInt(orderId));
				if(matchingOrder == null) {
					return new OrderDTO(Long.parseLong(orderId));
				}
				
				OrderDTO returnMe = new OrderDTO(matchingOrder);			
				returnMe.setLineItems(lineItemService.getLineItems(Long.parseLong(orderId)));

				return returnMe;
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
	 * return {@link OrderDTO}
	 */
	@Override
	public OrderDTO createOrder(OrderDTO saveMe) {

		LOGGER.info("Order info received. Attempting to create new order");
		
		Order newOrder = new Order();
		newOrder.setCreatedDate(new Date());
		newOrder.setStatus(Status.NEW);
		newOrder.setCreatedBy(0); 
		newOrder.setModifiedBy(0); 
		newOrder.setModifiedDate(new Date());
		
		newOrder = orderRepo.save(newOrder);
		
		saveMe.setCreatedDate(newOrder.getCreatedDate());
		
		LineItem newItem = new LineItem();

		List<LineItemDTO> ja = saveMe.getLineItems();
		
		int itemNum = 1;
		for(LineItemDTO item : ja) {
			newItem.setName(item.getName());
			newItem.setPrice(item.getPrice() < 0 ? 0 : item.getPrice());
			newItem.setQuantity(item.getQuantity() == 0 ? 1 : item.getQuantity());
			newItem.setOrderId(newOrder.getId());			
			newItem.setNumber(itemNum++);
			newItem.setModifiedBy(0);
			newItem.setCreatedBy(0);
			newItem.setCreatedDate(new Date());
			newItem.setModifiedDate(new Date());

			LineItem saved = lineItemRepo.save(newItem);
			
			item.setOrderId(saved.getOrderId());
			item.setNumber(saved.getNumber());

		}
		
		saveMe.setId(newOrder.getId());
		saveMe.setStatus(newOrder.getStatus());
		LOGGER.info("Order info saved. Returning saved data.");

		return saveMe;
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
		
		if(associatedOrder == null || !StatusUtils.isOrderAvailable(associatedOrder.getStatus())) {
			//order not cancelled if not found, is in transit or delivered.
			return false;
		}
		
		lineItemService.cancellingOrder(associatedOrder.getId());
		orderRepo.delete(associatedOrder);
		
		orderRepo.flush();
		
		return orderRepo.findById(orderDTO.getId()) == null;
	}
	
}
