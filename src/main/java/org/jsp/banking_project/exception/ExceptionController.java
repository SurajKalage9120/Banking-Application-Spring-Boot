package org.jsp.banking_project.exception;

import org.jsp.banking_project.helper.ResponseStructure;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController 
{
 @ExceptionHandler(value = MyException.class)
 public ResponseEntity<ResponseStructure<String>> idNotFound(MyException ie)
 {
	 ResponseStructure<String> responseStructure=new ResponseStructure<>();
	 responseStructure.setCode(HttpStatus.NOT_FOUND.value());
	 responseStructure.setMessage("Request failed");
	 responseStructure.setData(ie.toString());
	 return new ResponseEntity<ResponseStructure<String>>(responseStructure, HttpStatus.NOT_ACCEPTABLE);
	 
 }
}
