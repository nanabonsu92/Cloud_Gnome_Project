package SOA.task3.filter;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

@Provider
public class BasicAuthFilter implements ContainerRequestFilter {

    private static final String AUTHORIZATION_PREFIX = "Basic ";
    private static final String OWNER_USERNAME = "owner";
    private static final String OWNER_PASSWORD = "password";  // In production, this should be hashed

    @Override
    public void filter(ContainerRequestContext requestContext) {
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null || !authorizationHeader.startsWith(AUTHORIZATION_PREFIX)) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Missing or invalid Authorization header").build());
            return;
        }

        String base64Credentials = authorizationHeader.substring(AUTHORIZATION_PREFIX.length()).trim();
        String credentials = new String(Base64.getDecoder().decode(base64Credentials), StandardCharsets.UTF_8);
        final String[] values = credentials.split(":", 2);

        if (!authenticate(values[0], values[1])) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Invalid credentials").build());
        }
    }

    private boolean authenticate(String username, String password) {
        // Simple authentication check (should be hashed in real-world apps)
        return OWNER_USERNAME.equals(username) && OWNER_PASSWORD.equals(password);
    }
}