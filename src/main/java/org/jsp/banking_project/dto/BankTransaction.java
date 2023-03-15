package org.jsp.banking_project.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
@Component

public class BankTransaction 
{
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
int id;
LocalDateTime dateTime;
double deposit;
double withdraw;
double balance;
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public LocalDateTime getDateTime() {
	return dateTime;
}
public void setDateTime(LocalDateTime dateTime) {
	this.dateTime = dateTime;
}
public double getDeposit() {
	return deposit;
}
public void setDeposit(double deposit) {
	this.deposit = deposit;
}
public double getWithdraw() {
	return withdraw;
}
public void setWithdraw(double withdraw) {
	this.withdraw = withdraw;
}
public double getBalance() {
	return balance;
}
public void setBalance(double balance) {
	this.balance = balance;
}


}



