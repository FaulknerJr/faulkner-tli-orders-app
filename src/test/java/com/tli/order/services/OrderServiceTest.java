package com.tli.order.services;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.tli.order.TestUtils;
import com.tli.orders.DTO.LineItemDTO;
import com.tli.orders.DTO.OrderDTO;
import com.tli.orders.entities.LineItem;
import com.tli.orders.entities.Order;
import com.tli.orders.enums.Status;
import com.tli.orders.repo.LineItemRepo;
import com.tli.orders.repo.OrderRepo;
import com.tli.orders.services.LineItemService;
import com.tli.orders.services.OrderServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {

	@Mock
	private OrderRepo orderRepo;
	
	@Mock
	private LineItemRepo lineItemRepo;
	
	@Mock
	private LineItemService lineItemSerivce;
	
	@Captor
	private ArgumentCaptor<LineItem> lineItemCaptor;
	
	@Captor
	private ArgumentCaptor<Order> orderCaptor;
	
	@InjectMocks
	private OrderServiceImpl orderService;
	
	private static final int orderId = 421523;
	
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
		
		when(orderRepo.findById(12345)).thenReturn(TestUtils.mockedOrder(12345, Status.NEW));
		
		OrderDTO result = orderService.viewOrder(requestId);
		
		verify(orderRepo).findById(12345);
		
		assertEquals(12345, result.getId());
		assertEquals(Status.NEW.name(), result.getStatus());
		
		verifyNoMoreInteractionsThisTest();
	}
	
	@Test
	public void testCancelOrderSuccess() {
		
		Order mockedOrder = TestUtils.mockedOrder(orderId, Status.NEW);
		
		OrderDTO dto = new OrderDTO();
		dto.setId(orderId);
		
		when(orderRepo.findById(orderId)).thenReturn(mockedOrder).thenReturn(null);
		
		assertTrue(orderService.cancelOrder(dto));
		
		verify(orderRepo, times(2)).findById(orderId);
		verify(orderRepo).delete(mockedOrder);
		verify(orderRepo).flush();
		verify(lineItemSerivce).cancellingOrder(mockedOrder.getId());
		
		verifyNoMoreInteractionsThisTest();
	}
	
	@Test
	public void testCancelOrderFails() {
		
		Order mockedOrder = TestUtils.mockedOrder(orderId, Status.IN_TRANSIT);
		
		OrderDTO dto = new OrderDTO();
		dto.setId(orderId);
		
		when(orderRepo.findById(orderId)).thenReturn(mockedOrder).thenReturn(null);
		
		assertFalse(orderService.cancelOrder(dto));
		
		verify(orderRepo).findById(orderId);
		
		verifyNoMoreInteractionsThisTest();
	}
	
	@Test
	public void testCreateOrder() throws Exception {
		
		orderCaptor = ArgumentCaptor.forClass(Order.class);
		
		lineItemCaptor = ArgumentCaptor.forClass(LineItem.class);
		
		List<LineItemDTO> itemsDTO = new ArrayList<>();

		itemsDTO.add(new LineItemDTO("turtles", 5, 2.5));
		itemsDTO.add(new LineItemDTO("ligers", 100, 2)); //this is a good deal
		
		OrderDTO dto = new OrderDTO();
		
		dto.setLineItems(itemsDTO);
		
		when(orderRepo.save(Mockito.isA(Order.class))).then(i -> i.getArgument(0));
		when(lineItemRepo.save(Mockito.isA(LineItem.class))).then(i -> i.getArgument(0));
		
		orderService.createOrder(dto);
		
		verify(orderRepo).save(orderCaptor.capture());
		verify(lineItemRepo, times(2)).save(lineItemCaptor.capture());
		
		TestUtils.verifyNoMoreInteractionsForAllMocks(this.getClass());
	}
	
	private void verifyNoMoreInteractionsThisTest() {
		verifyNoMoreInteractions(orderRepo, lineItemRepo, lineItemSerivce);
	}
}
























