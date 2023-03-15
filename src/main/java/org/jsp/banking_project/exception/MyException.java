package org.jsp.banking_project.exception;

public class MyException extends Exception
{
String msg="Id not found";
public MyException(String msg)
{
	this.msg=msg;
}
public MyException()
{
	
}
@Override
public String toString() {
	return "MyException [msg=" + msg + "]";
}

}
