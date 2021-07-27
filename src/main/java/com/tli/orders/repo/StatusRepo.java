package com.tli.orders.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tli.orders.entities.Status;

@Repository
public interface StatusRepo extends JpaRepository<Status, Integer> {

	Status findById(int i);
}
