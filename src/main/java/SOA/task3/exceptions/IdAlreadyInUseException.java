package SOA.task3.exceptions;

public class IdAlreadyInUseException extends RuntimeException {
	private static final long serialVersionUID = -1000000;

	public IdAlreadyInUseException(String message) {
		super(message);
	}
}
