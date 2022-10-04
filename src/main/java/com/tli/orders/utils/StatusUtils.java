package com.tli.orders.utils;


import com.tli.orders.enums.Status;

public class StatusUtils {

	public static boolean isOrderAvailable(Status currentOrderStatus) {
		
		if(Status.IN_TRANSIT == currentOrderStatus
				|| Status.DELIVERED == currentOrderStatus) {
			return false;
		}
		
		return true;
	}
}
