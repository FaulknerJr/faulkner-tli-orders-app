package com.tli.order.services;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.gson.Gson;
import com.tli.orders.DTO.LineItemDTO;
import com.tli.orders.DTO.OrderDTO;
import com.tli.orders.entities.LineItem;
import com.tli.orders.entities.Order;
import com.tli.orders.entities.Status;
import com.tli.orders.repo.LineItemRepo;
import com.tli.orders.repo.OrderRepo;
import com.tli.orders.repo.StatusRepo;
import com.tli.orders.services.OrderServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {

	@Mock
	private OrderRepo orderRepo;
	
	@Mock
	private StatusRepo statusRepo;
	
	@Mock
	private LineItemRepo lineItemRepo;
	
	@Captor
	private ArgumentCaptor<LineItem> lineItemCaptor = ArgumentCaptor.forClass(LineItem.class);
	
	@Captor
	private ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
	
	@InjectMocks
	private OrderServiceImpl orderService;
	
	private static final int orderId = 421523;
	
	private static Order mockedOrder(int orderId, int statusId) {
		
		Order mockOrder = new Order();
		
		mockOrder.setId(orderId);
		mockOrder.setCreatedDate(new Date());
		mockOrder.setStatusId(statusId);
		
		return mockOrder;
	}
	
	private static Status mockedStatus(int statusId, String name) {
		Status mockStatus = new Status();
		
		mockStatus.setId(statusId);
		mockStatus.setName(name);
		
		return mockStatus;
	}
	
	private static LineItem mockedLineItem(int orderId, int number, int quantity) {
		
		LineItem mockLI = new LineItem();
		
		mockLI.setOrderId(orderId);
		mockLI.setNumber(number);
		mockLI.setQuantity(quantity);
		
		return mockLI;
	}
	@Test
	public void testFindByIdFails() throws JSONException {
		
		String requestedId = "thisIsString";
		
		try {
			orderService.viewOrder(requestedId);
		} catch (Exception e) {
			if(e instanceof NumberFormatException) {
				assertTrue(true);
			} else {
				fail();
			}
		}
	}
	
	@Test
	public void testFindById() throws Exception {
		
		String requestId = "12345";
		
		Status mockedStatus = mockedStatus(1, "New");
		
		when(orderRepo.findById(12345)).thenReturn(mockedOrder(12345, 1));
		
		JSONObject result = new JSONObject(orderService.viewOrder(requestId));
		
		verify(orderRepo).findById(12345);
		
		assertEquals(12345, result.get("id"));
		assertEquals(1, result.getInt("statusId"));
	}
	
	@Test
	public void testCancelOrderSuccess() {
		
		Order mockedOrder = mockedOrder(orderId, 1);
		Status mockedStatus = mockedStatus(1, "New");
		
		OrderDTO dto = new OrderDTO();
		dto.setId(orderId);
		
		when(orderRepo.findById(orderId)).thenReturn(mockedOrder).thenReturn(null);
		when(statusRepo.findById(1)).thenReturn(mockedStatus);
		
		assertTrue(orderService.cancelOrder(dto));
		
		verify(orderRepo, times(2)).findById(orderId);
		verify(statusRepo).findById(1);
		verify(lineItemRepo).deleteByOrderId(orderId);
		verify(orderRepo).delete(mockedOrder);
		verify(orderRepo).flush();
		
		verifyNoMoreInteractionsThisTest();
	}
	
	@Test
	public void testCancelOrderFails() {
		
		Order mockedOrder = mockedOrder(orderId, 1);
		Status mockedStatus = mockedStatus(4, "Delivered");
		
		OrderDTO dto = new OrderDTO();
		dto.setId(orderId);
		
		when(orderRepo.findById(orderId)).thenReturn(mockedOrder).thenReturn(null);
		when(statusRepo.findById(1)).thenReturn(mockedStatus);
		
		assertFalse(orderService.cancelOrder(dto));
		
		verify(orderRepo).findById(orderId);
		verify(statusRepo).findById(1);
		
		verifyNoMoreInteractionsThisTest();
	}
	
	@Test
	public void testChangeQuantity() throws JSONException {
		
		int number = 8923;
		int quantity = 8;
		int statusId = 1;
		
		Status mockedStatus = mockedStatus(statusId, "New");
		Order mockedOrder = mockedOrder(orderId, 1);
		LineItem mockedLineItem = mockedLineItem(orderId, number, quantity);
		
		LineItemDTO dto = new LineItemDTO();
		
		dto.setOrderId(orderId);
		dto.setNumber(number);
		dto.setQuantiy(3);
		
		when(lineItemRepo.findByOrderIdAndNumber(orderId, number)).thenReturn(mockedLineItem);
		when(orderRepo.findById(orderId)).thenReturn(mockedOrder);
		when(statusRepo.findById(statusId)).thenReturn(mockedStatus);
		when(lineItemRepo.save(Mockito.isA(LineItem.class))).then(i -> i.getArgument(0));
		
		assertEquals(3, orderService.changeQuantity(dto).getInt("quantity"));
		
		verify(lineItemRepo).save(lineItemCaptor.capture());
		verify(orderRepo).findById(orderId);
		verify(statusRepo).findById(statusId);
		verify(lineItemRepo).findByOrderIdAndNumber(orderId, number);
		
		assertEquals(3, lineItemCaptor.getValue().getQuantity());
		
		verifyNoMoreInteractionsThisTest();
	}
	
	@Test
	public void testChangeQuantityNotAvailable() throws Exception {
		
		int number = 8923;
		int quantity = 8;
		int statusId = 4;
		
		Status mockedStatus = mockedStatus(statusId, "In Transit");
		Order mockedOrder = mockedOrder(orderId, statusId);
		LineItem mockedLineItem = mockedLineItem(orderId, number, quantity);
		
		LineItemDTO dto = new LineItemDTO();
		
		dto.setOrderId(orderId);
		dto.setNumber(number);
		dto.setQuantiy(3);
		
		when(lineItemRepo.findByOrderIdAndNumber(orderId, number)).thenReturn(mockedLineItem);
		when(orderRepo.findById(orderId)).thenReturn(mockedOrder);
		when(statusRepo.findById(statusId)).thenReturn(mockedStatus);
		
		assertNull(orderService.changeQuantity(dto));
		
		verify(lineItemRepo).findByOrderIdAndNumber(orderId, number);
		verify(orderRepo).findById(orderId);
		verify(statusRepo).findById(statusId);
		
		verifyNoMoreInteractionsThisTest();
	}
	
	@Test
	public void testCreateOrder() throws Exception {
		
		Status mockedStatus = mockedStatus(1, "New");
		
		List<LineItemDTO> itemsDTO = new ArrayList<>();

		itemsDTO.add(new LineItemDTO("turtles", 5, 2.5));
		itemsDTO.add(new LineItemDTO("ligers", 100, 2)); //this is a good deal
		
		OrderDTO dto = new OrderDTO();
		
		dto.setLineItems(itemsDTO);
		
		when(orderRepo.save(Mockito.isA(Order.class))).then(i -> i.getArgument(0));
		when(lineItemRepo.save(Mockito.isA(LineItem.class))).then(i -> i.getArgument(0));
		when(statusRepo.findById(1)).thenReturn(mockedStatus);
		
		orderService.createOrder(dto);
		
		verify(orderRepo).save(orderCaptor.capture());
		verify(lineItemRepo, times(2)).save(lineItemCaptor.capture());
		verify(statusRepo).findById(1);
		
		verifyNoMoreInteractionsThisTest();
	}
	
	private void verifyNoMoreInteractionsThisTest() {
		verifyNoMoreInteractions(orderRepo, statusRepo, lineItemRepo);
	}
}
