package SOA.task3.filter;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import SOA.task3.ErrorMessage;
import SOA.task3.classes.User;
import SOA.task3.services.TokenService;
import SOA.task3.services.UserService;
import jakarta.annotation.Priority;
import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class RoleBasedAccessControlFilter implements ContainerRequestFilter {

	private static final String AUTHORIZATION_PREFIX_BEARER = "Bearer ";
	private static final String AUTHORIZATION_PREFIX_BASIC = "Basic ";

	private TokenService tokenService = new TokenService();

	private static final ErrorMessage FORBIDDEN_ErrMESSAGE = new ErrorMessage("Access blocked for all users !!!", 403,
			"http://myDocs.org");
	private static final ErrorMessage UNAUTHORIZED_ErrMESSAGE = new ErrorMessage("User cannot access the resource.",
			401, "http://myDocs.org");
	private static final ErrorMessage NO_AUTH_HEADER = new ErrorMessage("Authorization header must be provided", 401,
			"http://myDocs.org");
	private static final ErrorMessage INVALID_CREDENTIALS = new ErrorMessage("Invalid credentials", 401,
			"http://myDocs.org");
	private static final ErrorMessage INVALID_TOKEN = new ErrorMessage("Invalid token", 401, "http://myDocs.org");
	private static final ErrorMessage NO_AUTH_METHOD = new ErrorMessage("No valid authorization header type provided",
			401, "http://myDocs.org");
	private static final ErrorMessage AUTH_SYNTAX_ERROR = new ErrorMessage("Invalid authentication syntax", 400,
			"http://myDocs.org");

	@Context
	private ResourceInfo resourceInfo;

	@Override
	public void filter(ContainerRequestContext requestContext) {
		Method resMethod = resourceInfo.getResourceMethod();

		// Method
		if (resMethod.isAnnotationPresent(PermitAll.class)) {
			return;
		}
		if (resMethod.isAnnotationPresent(DenyAll.class)) {
			Response response = Response.status(Response.Status.FORBIDDEN).entity(FORBIDDEN_ErrMESSAGE)
					.type(MediaType.APPLICATION_JSON).build();
			requestContext.abortWith(response);
			return;
		}

		if (resMethod.isAnnotationPresent(RolesAllowed.class)) {
			List<String> roles = getRoles(requestContext);
			if (roles == null)
				return;
			RolesAllowed rolesAllowed = resMethod.getAnnotation(RolesAllowed.class);

			if (!UserService.checkRoles(roles, Arrays.asList(rolesAllowed.value()))) {
				Response response = Response.status(Response.Status.UNAUTHORIZED).entity(UNAUTHORIZED_ErrMESSAGE)
						.type(MediaType.APPLICATION_JSON).build();
				requestContext.abortWith(response);
			}
			return;
		}
	}

	private List<String> getRoles(ContainerRequestContext requestContext) {
		String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
		if (authorizationHeader == null) {
			Response response = Response.status(Response.Status.UNAUTHORIZED).entity(NO_AUTH_HEADER)
					.type(MediaType.APPLICATION_JSON).build();
			requestContext.abortWith(response);
			return null;
		}
		if (authorizationHeader.startsWith(AUTHORIZATION_PREFIX_BEARER)) {
			return getRolesJWT(requestContext);
		}

		if (authorizationHeader.startsWith(AUTHORIZATION_PREFIX_BASIC)) {
			return getRolesBasic(requestContext);
		}
		Response response = Response.status(Response.Status.UNAUTHORIZED).entity(NO_AUTH_METHOD)
				.type(MediaType.APPLICATION_JSON).build();
		requestContext.abortWith(response);
		return null;
	}

	private List<String> getRolesJWT(ContainerRequestContext requestContext) {
		String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

		List<String> res = null;
		try {
			String token = authorizationHeader.substring("Bearer".length()).trim();
			res = tokenService.getRolesFromToken(token);
		} catch (Exception e) {
			Response response = Response.status(Response.Status.UNAUTHORIZED).entity(INVALID_TOKEN)
					.type(MediaType.APPLICATION_JSON).build();
			requestContext.abortWith(response);
			return null;
		}

		return res;
	}

	private List<String> getRolesBasic(ContainerRequestContext requestContext) {
		String username = "";
		String password = "";
		try {
			String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

			String base64Credentials = authorizationHeader.substring(AUTHORIZATION_PREFIX_BASIC.length()).trim();
			String credentials = new String(Base64.getDecoder().decode(base64Credentials), StandardCharsets.UTF_8);
			final String[] values = credentials.split(":", 2);
			username = values[0];
			password = values[1];
		} catch (Exception e) {
			Response response = Response.status(Response.Status.BAD_REQUEST).entity(AUTH_SYNTAX_ERROR)
					.type(MediaType.APPLICATION_JSON).build();
			requestContext.abortWith(response);
			return null;
		}

		UserService userService = new UserService();
		User user = userService.getUserByUsername(username);
		if (user == null) {
			Response response = Response.status(Response.Status.UNAUTHORIZED).entity(INVALID_CREDENTIALS)
					.type(MediaType.APPLICATION_JSON).build();
			requestContext.abortWith(response);
			return null;
		}

		if (!UserService.checkPassword(password, user.getPassword())) {
			Response response = Response.status(Response.Status.UNAUTHORIZED).entity(INVALID_CREDENTIALS)
					.type(MediaType.APPLICATION_JSON).build();
			requestContext.abortWith(response);
			return null;
		}

		return user.getRoles();
	}

}
