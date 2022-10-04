package com.tli.orders.enums;

import java.util.Arrays;

public enum Status {

	NEW(1),
	PROCESSING(2),
	IN_TRANSIT(3),
	DELIVERED(4),
	CANCELLED(5);

	private int id;
	
	Status(int i) {
		this.id = i;
	}
	
	public int getId() {
		return id;
	}
	
	public static Status getStatus(int id) {
		return Arrays.stream(Status.values()).filter(status -> status.id == id).findFirst().get();
	}
	
}
