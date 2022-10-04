package com.tli.orders.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.EqualsBuilder;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;


@Entity
@Table(name = "order_line_items")
public class LineItem implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "number")
	@Expose
	private long number;
	
	@NotNull
	@Expose
	private long orderId;
	
	@NotNull
	@Column(name = "name")
	@Expose
	private String name;
	
	@NotNull
	@Column(name = "price")
	@Expose
	private double price;
	
	@NotNull
	@Column(name = "quantity")
	@Expose
	private int quantity;

	@NotNull
	@Column(name = "created_date")
	private Date createdDate;
	
	@NotNull
	@Column(name = "modified_date")
	private Date modifiedDate;
	
	@NotNull
	@Column(name = "created_by")
	private int createdBy;
	
	@NotNull
	@Column(name = "modified_by")
	private int modifiedBy;
	
	
	public long getNumber() {
		return number;
	}

	public void setNumber(long number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}
	
	public long getOrderId() {
		return orderId;
	}
	
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public int getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(int modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public boolean equals(Object other) {
		return EqualsBuilder.reflectionEquals(this, other, false);	
	}
	
	public String toString() {
		return new GsonBuilder().serializeNulls().setLenient().create().toJson(this);
	}
	
	public String getPertinentData() {
		return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().setLenient().create().toJson(this);
	}
}
