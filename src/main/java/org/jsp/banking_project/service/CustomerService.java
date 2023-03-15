package org.jsp.banking_project.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.jsp.banking_project.dto.BankAccount;
import org.jsp.banking_project.dto.BankTransaction;
import org.jsp.banking_project.dto.Customer;
import org.jsp.banking_project.dto.Login;
import org.jsp.banking_project.exception.MyException;
import org.jsp.banking_project.helper.MialVerification;
import org.jsp.banking_project.helper.ResponseStructure;
import org.jsp.banking_project.repository.BankRepository;
import org.jsp.banking_project.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CustomerService 
{
	@Autowired
	CustomerRepository repository;
	
	@Autowired
	BankRepository bankRepository;
	
	@Autowired
	MialVerification mailVerification;
	
	@Autowired
	BankAccount account;
	
	@Autowired
	BankTransaction transaction;
	
	
public ResponseStructure<Customer> save(Customer customer)
{
	ResponseStructure<Customer> structure=new ResponseStructure<>();
	int age=Period.between(customer.getDob().toLocalDate(), LocalDate.now()).getYears();
	customer.setAge(age);
	if(age<18)
	{
		structure.setMessage("You should be 18+ to create account");
		structure.setCode(HttpStatus.CONFLICT.value());
		structure.setData(customer);
	}
	else
	{
		Random random=new Random();
		int otp=random.nextInt(100000,999999);
		customer.setOtp(otp);
		
		//mailVerification.sendMail(customer);
		
		structure.setMessage("Verification mail send");
		structure.setCode(HttpStatus.PROCESSING.value());
		structure.setData(repository.save(customer));
		
		
	}
	return structure;
}

public ResponseStructure<Customer> verify(int custid,int otp) throws MyException
{
	ResponseStructure<Customer> structure=new ResponseStructure<>();
	Optional<Customer> optional = repository.findById(custid);
	if(optional.isEmpty())
	{
		throw new MyException("Check id and try again");
	}
	else
	{
		Customer customer=optional.get();
		if(customer.getOtp()==otp)
		{
			structure.setCode(HttpStatus.CREATED.value());
			structure.setMessage("Account created successfully");
			customer.setStatus(true);
			structure.setData(repository.save(customer));
		}
		else
		{
			throw new MyException("otp mismatch");
		}
	}
	return structure;
}

public ResponseStructure<Customer> login(Login login) throws MyException {
	
	ResponseStructure<Customer> structure=new ResponseStructure<>();
	Optional<Customer> optional = repository.findById(login.getId());
	if(optional.isEmpty())
	{
		throw new MyException("Invalid customer id");
	}
	else
	{
		Customer customer=optional.get();
		if(customer.getPassword().equals(login.getPassword()))
		{
			if(customer.isStatus())
			{
				structure.setCode(HttpStatus.ACCEPTED.value());
				structure.setMessage("login success");
				structure.setData(customer);
			}
			else {
				throw new MyException("Verify your email first");
			    }
		}
			    else 
			   {
				throw new MyException("Invalid Password");
			   }
		}
	
	
	
	return structure;
	
}

public ResponseStructure<Customer> createAccount(int cust_id, String type) throws MyException {
	ResponseStructure<Customer> structure=new ResponseStructure<Customer>();
	Optional<Customer> optional = repository.findById(cust_id);
	if(optional.isEmpty())
	{
		throw new MyException("Invalid customer id");
	}
	else
	{
		Customer customer=optional.get();
	    List<BankAccount> list=customer.getAccounts();
	    
	    boolean flag=true;
	    for(BankAccount account: list)
	    {
	    	if(account.getType().equals(type))
	    	{
	    	flag=false;
	    	break;
	    	}
	    }
	    if(!flag)
	    {
	    	throw new MyException(type+" Account Already exists");
	    }
	    else {
	    account.setType(type);
	    if(type.equals("savings"))
	    {
	    	account.setBanklimit(5000);
	    }
	    else 
	    {
	    	account.setBanklimit(10000);
	    }
	   
	    list.add(account);
	    customer.setAccounts(list);
	  }
	    structure.setCode(HttpStatus.ACCEPTED.value());
	    structure.setMessage("Account created wait for management aprrove");
	    structure.setData(repository.save(customer));
	}
	return structure;
}

public ResponseStructure<List<BankAccount>> fetchAllTrue(int custid) throws MyException {
	ResponseStructure<List<BankAccount>> structure=new ResponseStructure<List<BankAccount>>();
	Optional<Customer> optional=repository.findById(custid);
	Customer customer=optional.get();
	List<BankAccount> list=customer.getAccounts();
	
	
	List<BankAccount> res=new ArrayList<BankAccount>();
	for(BankAccount account:list)
	{
		if(account.isStatus())
		{
			res.add(account);
		}
	}
	if(res.isEmpty())
	{
		throw new MyException("No Active Accounts Fouund");
	}
	else {
		structure.setCode(HttpStatus.FOUND.value());
		structure.setMessage("Accounts Found");
		structure.setData(res);
	}
	return structure;
}

public ResponseStructure<Double> checkBalance(long acno) {
	ResponseStructure<Double> structure=new ResponseStructure<Double>();
	
	Optional<BankAccount> optional=bankRepository.findById(acno);
	BankAccount account=optional.get();
	
	structure.setCode(HttpStatus.FOUND.value());
	structure.setMessage("Data Found");
	structure.setData(account.getAmount());
	
	
	
	return structure;
}

public ResponseStructure<BankAccount> deposit(long acno, double amount) {
	ResponseStructure<BankAccount> structure=new ResponseStructure<BankAccount>();
	
	BankAccount account=  bankRepository.findById(acno).get();
	account.setAmount(account.getAmount()+amount);
	
	transaction.setDateTime(LocalDateTime.now());
	transaction.setDeposit(amount);
    transaction.setBalance(account.getAmount());	
    
   List<BankTransaction> transactions= account.getBankTransaction();
   transactions.add(transaction);
   
   account.setBankTransaction(transactions);
   
   structure.setCode(HttpStatus.ACCEPTED.value());
   structure.setMessage("Amount added succsessfully");
   structure.setData(bankRepository.save(account));
	
	return structure;
}


public ResponseStructure<BankAccount> withdraw(long acno, double amount) throws MyException {
ResponseStructure<BankAccount> structure=new ResponseStructure<BankAccount>();

	BankAccount account=bankRepository.findById(acno).get();

	if(amount>account.getBanklimit())
	{
		throw new MyException("Out of Limit");
	}
	else {
	if(amount>account.getAmount())
	{
		throw new MyException("Insufficient funds");
	}
	else {
	account.setAmount(account.getAmount()-amount);

	transaction.setDateTime(LocalDateTime.now());
	transaction.setDeposit(0);
	transaction.setWithdraw(amount);
	transaction.setBalance(account.getAmount());

	List<BankTransaction> transactions=account.getBankTransaction();
	transactions.add(transaction);

	account.setBankTransaction(transactions);

	structure.setCode(HttpStatus.ACCEPTED.value());
	structure.setMessage("Amount withdrwan Successfully");
	structure.setData(bankRepository.save(account));
	}
	}
	return structure;
}


public ResponseStructure<List<BankTransaction>> viewtransaction(long acno) throws MyException {

	ResponseStructure<List<BankTransaction>> structure=new ResponseStructure<List<BankTransaction>>();

	BankAccount account=bankRepository.findById(acno).get();
	List<BankTransaction> list=account.getBankTransaction();
	if(list.isEmpty())
	{
		throw new MyException("No Transaction");
	}
	else {
		structure.setCode(HttpStatus.FOUND.value());
		structure.setMessage("Data Found");
		structure.setData(list);
	}
	return structure;
}
}
