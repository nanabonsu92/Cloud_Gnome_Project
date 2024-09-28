package SOA.task3.exceptions;

public class IdNotFoundException extends RuntimeException{
	private static final long serialVersionUID = -100000;
	
	public IdNotFoundException(String message){
		super(message);
	}
}
