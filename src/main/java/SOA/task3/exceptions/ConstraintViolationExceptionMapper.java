package SOA.task3.exceptions;

import java.util.Set;
import java.util.stream.Collectors;

import SOA.task3.ErrorMessage;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

	@Override
	public Response toResponse(ConstraintViolationException exception) {
		Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
		String errorMessageStr = violations.stream().map(ConstraintViolation::getMessage)
				.collect(Collectors.joining(", "));

		ErrorMessage errorMessage = new ErrorMessage(errorMessageStr, 400, "http://myDocs.org");
		return Response.status(Status.NOT_FOUND).entity(errorMessage).build();
	}
}