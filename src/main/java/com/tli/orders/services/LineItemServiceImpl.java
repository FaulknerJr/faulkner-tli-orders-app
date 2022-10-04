package com.tli.orders.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tli.orders.DTO.LineItemDTO;
import com.tli.orders.DTO.OrderDTO;
import com.tli.orders.entities.LineItem;
import com.tli.orders.enums.Status;
import com.tli.orders.repo.LineItemRepo;
import com.tli.orders.utils.StatusUtils;

@Service
public class LineItemServiceImpl implements LineItemService {

	@Autowired
	private LineItemRepo lineItemRepo;
	
	@Autowired
	private OrderService orderService;

	/**
	 * 
	 * Changes the Quantity of items to be shipped on a line item. Line Items can
	 * not be modified if the order is In Transit or Delivered.
	 * 
	 * 
	 * @param {@link LineItemDTO} returns {@link JSONObject} of the line item.
	 * Check to see if the quantity changed update user and let them know if it has
	 * changed
	 * 
	 * @return null if there were no lineItems with the orderId and number sent
	 * over.
	 * 
	 */
	@Override
	public LineItemDTO changeQuantity(LineItemDTO lineItemDTO) {

		LineItem li = lineItemRepo.findByOrderIdAndNumber(lineItemDTO.getOrderId(), lineItemDTO.getNumber());

		OrderDTO associatedOrder = orderService.viewOrder(lineItemDTO.getOrderId() + "");
		
		if(associatedOrder == null || !StatusUtils.isOrderAvailable(Status.valueOf(Status.class, associatedOrder.getStatus()))) {
			// if order isn't available
			return new LineItemDTO(li);
		}
		

		if (li != null) {
			li.setQuantity(lineItemDTO.getQuantity());
			li = lineItemRepo.save(li);
			return new LineItemDTO(li);
		}

		return null;

	}
	
	/**
	 * Removes the requested line item from an order.
	 * Line Items can not be removed if the order is In Transit or Delivered.
	 * 
	 * 
	 * @param {@link LineItemDTO}
	 * @return boolean true if removed false if not(for whatever reason)
	 */
	@Override
	public OrderDTO removeItem(LineItemDTO lineItemDTO) {

		OrderDTO associatedOrder = orderService.viewOrder(lineItemDTO.getOrderId() + "");
		if(associatedOrder == null || !StatusUtils.isOrderAvailable(Status.valueOf(Status.class, associatedOrder.getStatus()))) {
			return associatedOrder;
		}
		
		lineItemRepo.deleteByOrderIdAndNumber(lineItemDTO.getOrderId(), lineItemDTO.getNumber());
				
		return orderService.viewOrder(associatedOrder.getId() + "");
	}

	/**
	 * We use this only to remove from the back end. do not make available to the ui.
	 * 
	 * use to delete all {@link LineItem} based on given order ID.
	 * 
	 * todo look into using the other removeItem method for this instead. It should be possible
	 * (thinkingface)
	 * 
	 * @param orderId 
	 */
	public void cancellingOrder(long orderId) {
		lineItemRepo.deleteByOrderId(orderId);
	}
	
	@Override
	public List<LineItemDTO> getLineItems(long orderId) {
		
		List<LineItem> items = lineItemRepo.findByOrderId(orderId);
		
		return items.stream().map(a -> new LineItemDTO(a)).toList();
		
	}

}
