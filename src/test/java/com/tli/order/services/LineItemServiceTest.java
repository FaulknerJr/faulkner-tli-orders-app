package com.tli.order.services;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.json.JSONException;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import com.tli.order.TestUtils;
import com.tli.orders.DTO.LineItemDTO;
import com.tli.orders.entities.LineItem;
import com.tli.orders.entities.Order;
import com.tli.orders.enums.Status;
import com.tli.orders.repo.LineItemRepo;
import com.tli.orders.repo.OrderRepo;
import com.tli.orders.services.LineItemService;


public class LineItemServiceTest {

	@Mock
	private LineItemRepo lineItemRepo;
	
	@Mock
	private OrderRepo orderRepo;
	
	
	@InjectMocks
	private LineItemService lineItemService;
//	
	@Test
	public void testChangeQuantity() throws JSONException {
		
		ArgumentCaptor<LineItem> lineItemCaptor = ArgumentCaptor.forClass(LineItem.class);

		int number = 8923;
		int quantity = 8;
		Status mockedStatus = Status.NEW;
		int orderId = 43985;
		
		Order mockedOrder = TestUtils.mockedOrder(orderId, mockedStatus);
		LineItem mockedLineItem = TestUtils.mockedLineItem(orderId, number, quantity);
		
		LineItemDTO dto = new LineItemDTO();
		
		dto.setOrderId(orderId);
		dto.setNumber(number);
		dto.setQuantiy(3);
		
		when(lineItemRepo.findByOrderIdAndNumber(orderId, number)).thenReturn(mockedLineItem);
		when(orderRepo.findById(orderId)).thenReturn(mockedOrder);
		when(lineItemRepo.save(Mockito.isA(LineItem.class))).then(i -> i.getArgument(0));
		
		assertEquals(3, lineItemService.changeQuantity(dto).getQuantity());
		
		verify(lineItemRepo).save(lineItemCaptor.capture());
		verify(orderRepo).findById(orderId);
		assertEquals(mockedStatus, mockedOrder.getStatus());
		verify(lineItemRepo).findByOrderIdAndNumber(orderId, number);
		
		assertEquals(3, lineItemCaptor.getValue().getQuantity());
		
		TestUtils.verifyNoMoreInteractionsForAllMocks(this);
		
	}
//	
//	@Test
//	public void testChangeQuantityNotAvailable() throws Exception {
//		
//		int number = 8923;
//		int quantity = 8;
//		int statusId = 4;
//		
//		Status mockedStatus = mockedStatus(statusId, "In Transit");
//		Order mockedOrder = mockedOrder(orderId, statusId);
//		LineItem mockedLineItem = mockedLineItem(orderId, number, quantity);
//		
//		LineItemDTO dto = new LineItemDTO();
//		
//		dto.setOrderId(orderId);
//		dto.setNumber(number);
//		dto.setQuantiy(3);
//		
//		when(lineItemRepo.findByOrderIdAndNumber(orderId, number)).thenReturn(mockedLineItem);
//		when(orderRepo.findById(orderId)).thenReturn(mockedOrder);
//		when(statusRepo.findById(statusId)).thenReturn(mockedStatus);
//		
//		assertNull(lineItemService.changeQuantity(dto));
//		
//		verify(lineItemRepo).findByOrderIdAndNumber(orderId, number);
//		verify(orderRepo).findById(orderId);
//		verify(statusRepo).findById(statusId);
//		
//		verifyNoMoreInteractionsThisTest();
//	}
}
