package com.tli.orders.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;

import com.google.gson.GsonBuilder;

/**
 * This honestly might have been better as an enum with a db lookup.
 * 
 * Can't remember how to set that up at this point. Might do that later
 * 
 *
 */
@Entity
@Table(name = "order_statuses")
public class Status implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	private int id;

	@Column(name = "name")
	@NotNull
	private String name;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "created_by")
	private int created_by;
	
	@Column(name = "modified_date")
	private Date modifiedDate;
	
	@Column(name = "modified_by")
	private int modifiedBy;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public int getCreated_by() {
		return created_by;
	}

	public void setCreated_by(int created_by) {
		this.created_by = created_by;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
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
	
	public boolean equals(Object other) {
		return EqualsBuilder.reflectionEquals(this, other, false);
	}
	
	public int hashCode() {
		return ObjectUtils.hashCode(this);
	}

	// todo: add tostring equals hashcode
}
