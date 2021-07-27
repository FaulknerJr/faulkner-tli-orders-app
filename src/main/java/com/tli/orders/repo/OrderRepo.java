package com.tli.orders.repo;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tli.orders.entities.Order;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long>{
	
	Order findById(long id);
	
	@Transactional
	void deleteById(long id);

}
