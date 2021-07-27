package com.tli.orders.entities;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "orders")
public class Order implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	@Expose
	private long id;

	@NotNull
	@Expose
	private int statusId;

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
	
	public Order() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date date) {
		this.createdDate = date;
	}
	
	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
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

	public String toString() {
		return new GsonBuilder().serializeNulls().setLenient().create().toJson(this);
	}
	
	public String getPertinentDate() {
		return new GsonBuilder().serializeNulls().excludeFieldsWithoutExposeAnnotation().setLenient().create().toJson(this);
	}
}
