package com.tli.orders.DTO;

import com.google.gson.GsonBuilder;
import com.tli.orders.entities.LineItem;

public class LineItemDTO {

	private String name;

	private long number;

	private long orderId;

	private int quantity;

	private double price;

	public LineItemDTO(LineItem item) {
		if (item != null) {
			this.name = item.getName();
			this.number = item.getNumber();
			this.orderId = item.getOrderId();
			this.quantity = item.getQuantity();
			this.price = item.getPrice();
		}
	}

	public LineItemDTO(String name, int quantity, double price) {
		this.name = name;
		this.quantity = quantity;
		this.price = price;
	}

	public LineItemDTO() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getNumber() {
		return number;
	}

	public void setNumber(long number) {
		this.number = number;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantiy(int quantity) {
		this.quantity = quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String toString() {
		return new GsonBuilder().serializeNulls().setLenient().create().toJson(this);
	}
}
