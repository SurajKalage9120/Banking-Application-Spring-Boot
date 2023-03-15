package org.jsp.banking_project.repository;

import java.util.List;

import org.jsp.banking_project.dto.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankRepository extends JpaRepository<BankAccount, Long> 
{

	List<BankAccount> findAll();

}
