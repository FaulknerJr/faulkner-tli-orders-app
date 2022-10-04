package com.tli.order;

import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.util.Date;

import org.mockito.Mock;

import com.tli.orders.entities.LineItem;
import com.tli.orders.entities.Order;

public class TestUtils {

	public static Order mockedOrder(int orderId, int statusId) {
		
		Order mockOrder = new Order();
		
		mockOrder.setId(orderId);
		mockOrder.setCreatedDate(new Date());
		mockOrder.setStatusId(statusId);
		
		return mockOrder;
	}
	
	public static LineItem mockedLineItem(int orderId, int number, int quantity) {
		
		LineItem mockLI = new LineItem();
		
		mockLI.setOrderId(orderId);
		mockLI.setNumber(number);
		mockLI.setQuantity(quantity);
		
		return mockLI;
	}
	
	public static void verifyNoMoreInteractionsForAllMocks(Object o) {
		Field[] mockedClasses = o.getClass().getDeclaredFields();
		for(Field m : mockedClasses) {
			AnnotatedType annoType = m.getAnnotatedType();
			if(annoType != null && annoType.getDeclaredAnnotation(Mock.class) != null){
				verifyNoMoreInteractions(m);
			}
		}
	}
}
