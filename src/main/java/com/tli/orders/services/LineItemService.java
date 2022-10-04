package com.tli.orders.services;

import java.util.List;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.tli.orders.DTO.LineItemDTO;
import com.tli.orders.DTO.OrderDTO;

@Service
public interface LineItemService {

	LineItemDTO changeQuantity(LineItemDTO dto);
	OrderDTO removeItem(LineItemDTO dto);
	List<LineItemDTO> getLineItems(long orderId);
	void cancellingOrder(long id);
}
