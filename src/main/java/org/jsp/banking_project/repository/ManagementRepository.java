package org.jsp.banking_project.repository;

import org.jsp.banking_project.dto.Management;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ManagementRepository extends JpaRepository<Management, Integer>
{
 Management findByEmail(String email);
}
