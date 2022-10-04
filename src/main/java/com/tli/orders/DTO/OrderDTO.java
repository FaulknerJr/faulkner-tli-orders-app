package com.tli.orders.DTO;

import java.util.Date;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.tli.orders.entities.Order;
import com.tli.orders.enums.Status;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDTO {

	@JsonProperty("id")
	private long id;

	@JsonProperty("status")
	private String status;

	@JsonProperty("createdDate")
	private Date createdDate;
	
	@JsonProperty("items")
	private List<LineItemDTO> lineItems;
	
	private String message;
	
	

	public OrderDTO() {
		super();
	}
	
	public OrderDTO(Order order) {
		super();
		this.id = order.getId();
		this.status = Status.getStatus(order.getStatusId()).name();
		this.createdDate = order.getCreatedDate();
	}
	
	public OrderDTO(long id) {
		super();
		this.id = id;
	}
	
	public OrderDTO(long id, List<LineItemDTO> lineItems) {
		super();
		this.id = id;
		this.lineItems = lineItems;
	}

	public void setLineItems(List<LineItemDTO> lineItems) {
		this.lineItems = lineItems;
	}
	
	public List<LineItemDTO> getLineItems() {
		return lineItems;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String statusId) {
		this.status = statusId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
}
