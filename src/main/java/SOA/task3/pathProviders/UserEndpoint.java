package SOA.task3.pathProviders;

import java.util.Arrays;
import java.util.List;

import SOA.task3.classes.User;
import SOA.task3.services.TokenService;
import SOA.task3.services.UserService;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserEndpoint {
	private UserService userService = new UserService();
	private TokenService tokenService = new TokenService(); // Inject the TokenService

	@POST
	@Path("/register")
	@PermitAll
	@Produces(MediaType.APPLICATION_JSON)
	public Response registerUser(@Valid User user) {
		try {
			// If roles are specified in the request body, use them. Otherwise, assign
			// default role "user".
			List<String> roles;
			if (user.getRoles() == null || user.getRoles().isEmpty()) {
				roles = Arrays.asList("user"); // Assign default role "user" if no roles are provided
			} else {
				roles = user.getRoles(); // Use the roles provided in the request body
			}

			// Register the user with the default role
			User newUser = userService.registerUser(user.getUsername(), user.getPassword(), user.getEmail(), roles);
			return Response.ok(newUser).build();
		} catch (RuntimeException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path("/login")
	@PermitAll
	// No Security Annotation, everybody can access it
	public Response loginUser(@Valid User user) {
		User existingUser = userService.getUserByUsername(user.getUsername());

		if (existingUser == null || !UserService.checkPassword(user.getPassword(), existingUser.getPassword())) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid credentials").build();
		}

		// Get user roles from the User object
		List<String> roles = existingUser.getRoles();

		// Generate JWT token for the user
		String token = tokenService.generateTokenForUser(existingUser.getUsername(), roles);
		return Response.ok(token).build();
	}

}
