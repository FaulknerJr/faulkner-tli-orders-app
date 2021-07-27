package com.tli.orders.DTO;

import java.util.Date;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDTO {

	@JsonProperty("id")
	private long id;

	@JsonProperty("statusId")
	private int statusId;

	@JsonProperty("createdDate")
	private Date createdDate;
	
	@JsonProperty("items")
	private List<LineItemDTO> lineItems;
	
	public OrderDTO() {
		super();
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

	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	

}
