package com.bookstore.customers.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookstore.customers.model.CustomerModel;

public interface CustomerRepository extends JpaRepository<CustomerModel , Long>{

	Optional<CustomerModel> findByEmailAddress(String emailId);

}
