package org.jsp.banking_project.service;

import org.jsp.banking_project.repository.BankRepository;
import org.jsp.banking_project.repository.ManagementRepository;

import java.util.List;
import java.util.Optional;

import org.jsp.banking_project.dto.BankAccount;
import org.jsp.banking_project.dto.Customer;
import org.jsp.banking_project.dto.Management;
import org.jsp.banking_project.exception.MyException;
import org.jsp.banking_project.helper.ResponseStructure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ManagementService 
{
    @Autowired
	ManagementRepository repository;
    
    @Autowired
    BankRepository repository2;
	
    public ResponseStructure<Management> save(Management management)
    {
   	ResponseStructure<Management> structure=new ResponseStructure<>();
   	structure.setCode(HttpStatus.CREATED.value());
   	structure.setMessage("Account created Successfully");
   	structure.setData(repository.save(management));
   	
   	return structure;
   	 
    }

	public ResponseStructure<Management> login(Management management) throws MyException {
		
		ResponseStructure<Management> structure=new ResponseStructure<>();
		Management management1 = repository.findByEmail(management.getEmail());
		if(management1==null)
		{
			throw new MyException("Invalid management email");
		}
		else
		{
			if(management1.getPassword().equals(management.getPassword()))
			{
					structure.setCode(HttpStatus.ACCEPTED.value());
					structure.setMessage("login success");
					structure.setData(management1);
			}
			   else 
				   {
					throw new MyException("Invalid Password");
				   }
	}
		return structure;
	}

	public ResponseStructure<List<BankAccount>> fetchAllAccounts() throws MyException {
		ResponseStructure<List<BankAccount>> structure=new ResponseStructure<List<BankAccount>>();
		
		List<BankAccount> list=repository2.findAll();
		if(list.isEmpty())
		{
			throw new MyException("No Account present");
		}
		else
		{
			structure.setCode(HttpStatus.FOUND.value());
			structure.setMessage("Data Found");
			structure.setData(list);
		}
		
		return structure;
	}

	public ResponseStructure<BankAccount> changeStatus(long acno) {
		ResponseStructure<BankAccount> structure=new ResponseStructure<BankAccount>();
		Optional<BankAccount> optional=repository2.findById(acno);
		BankAccount account=optional.get();
		if(account.isStatus())
		{
			account.setStatus(false);
		}
		else
		{
			account.setStatus(true);
		}
		structure.setCode(HttpStatus.OK.value());
		structure.setMessage("Changed Status Success");
		structure.setData(repository2.save(account));
		return structure;
	}	
}
