package SOA.task3.pathProviders;

import SOA.task3.classes.User;
import SOA.task3.services.TokenService;
import SOA.task3.services.UserService;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserEndpoint {
    private UserService userService = new UserService();
    private TokenService tokenService = new TokenService();  // Inject the TokenService

    @POST
    @Path("/register")
    public Response registerUser(User user) {
        try {
            User newUser = userService.registerUser(user.getUsername(), user.getPassword(), user.getEmail());
            return Response.ok(newUser).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }


    @POST
    @Path("/login")
    public Response loginUser(User user) {
        User existingUser = userService.getUserByUsername(user.getUsername());

        if (existingUser == null || !userService.checkPassword(user.getPassword(), existingUser.getPassword())) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid credentials").build();
        }

        //Generate JWT token for the user
        String token = tokenService.generateTokenForUser(existingUser.getUsername());

        return Response.ok(token).build();
    }
    	
}
