package com.tli.orders.repo;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tli.orders.entities.LineItem;

@Repository
public interface LineItemRepo extends JpaRepository<LineItem, Long> {

	LineItem findByOrderIdAndNumber(long orderId, long number);

	@Transactional
	Integer deleteByOrderIdAndNumber(long orderId, long number);
	
	List<LineItem> findByOrderId(long orderId);

	@Transactional
	void deleteByOrderId(long orderId);
	
}
