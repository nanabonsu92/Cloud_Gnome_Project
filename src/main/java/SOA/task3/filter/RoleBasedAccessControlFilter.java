package SOA.task3.filter;

import java.util.List;

import SOA.task3.services.TokenService;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.Provider;

@Provider
public class RoleBasedAccessControlFilter implements ContainerRequestFilter {

	private TokenService tokenService = new TokenService();

	@Override
	public void filter(ContainerRequestContext requestContext) {
		UriInfo uriInfo = requestContext.getUriInfo();
		String path = uriInfo.getPath();

		String method = requestContext.getMethod();

		// Exclude the /register endpoint from being blocked by this filter
		if (path.contains("/register") || path.contains("/login")) {
			return;
		}

		String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
					.entity("Authorization header must be provided").build());
			return;
		}

		String token = authorizationHeader.substring("Bearer".length()).trim();

		// Validate the token and extract roles
		List<String> roles = tokenService.getRolesFromToken(token);

		// Allow both users and admins to access gnome-related endpoints with GET method
		if (path.contains("gnomes") && method.equals("GET") && (roles.contains("user") || roles.contains("admin"))) {
			return; // Allow users and admins to make GET requests
		}

		// Restrict POST and DELETE requests to admins only
		if (path.contains("gnomes") && (method.equals("POST") || method.equals("DELETE")) && roles.contains("admin")) {
			return; // Allow only admins to make POST and DELETE requests
		}

		// owners and creators are exempt from this logic
		if (path.contains("owners") || path.contains("creators")) {
			return;
		}

		// For other restricted paths, require the admin role
		if (!roles.contains("admin")) {
			requestContext.abortWith(Response.status(Response.Status.FORBIDDEN)
					.entity("You do not have access to this resource").build());
		}
	}
}
