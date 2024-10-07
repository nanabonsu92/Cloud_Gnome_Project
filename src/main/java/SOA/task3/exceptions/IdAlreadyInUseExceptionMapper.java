package SOA.task3.exceptions;

import SOA.task3.ErrorMessage;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider // the annotation preregisters our Mapper for JAX-RS to be used
public class IdAlreadyInUseExceptionMapper implements ExceptionMapper<IdAlreadyInUseException> {

	@Override
	public Response toResponse(IdAlreadyInUseException ex) {
		ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), 400, "http://myDocs.org");
		return Response.status(Status.NOT_FOUND).entity(errorMessage).type(MediaType.APPLICATION_JSON).build();
	}
}
