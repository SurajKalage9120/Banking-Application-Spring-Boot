package org.jsp.banking_project.dto;

import java.util.List;

import org.springframework.stereotype.Component;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;

@Entity
@Component
public class BankAccount 
{
	
  @Id
  @SequenceGenerator(name="acno",initialValue = 1002121111,allocationSize =1 )
  @GeneratedValue(generator = "acno")
	long number;
    @Override
public String toString() {
	return "Account [number=" + number + ", type=" + type + ", limit=" + banklimit + ", amount=" + amount + ", status="
			+ status + "]";
}
	public long getNumber() {
	return number;
}
public void setNumber(long number) {
	this.number = number;
}
public String getType() {
	return type;
}
public void setType(String type) {
	this.type = type;
}
public double getLimit() {
	return banklimit;
}
public void setLimit(double limit) {
	this.banklimit = limit;
}
public double getAmount() {
	return amount;
}
public void setAmount(double amount) {
	this.amount = amount;
}
public boolean isStatus() {
	return status;
}
public void setStatus(boolean status) {
	this.status = status;
}
	
    double banklimit;
    public double getBanklimit() {
		return banklimit;
	}
	public void setBanklimit(double banklimit) {
		this.banklimit = banklimit;
	}
	String type;
	double amount;
    boolean status;
	
    public List<BankTransaction> getBankTransaction() {
		return bankTransaction;
	}
	public void setBankTransaction(List<BankTransaction> bankTransaction) {
		this.bankTransaction = bankTransaction;
	}

	@OneToMany(cascade =CascadeType.ALL)
    List<BankTransaction>  bankTransaction;
	
}
