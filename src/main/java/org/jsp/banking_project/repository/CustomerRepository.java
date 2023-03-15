package org.jsp.banking_project.repository;

import org.jsp.banking_project.dto.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer>
{

	
}
