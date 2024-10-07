package SOA.task3;

import SOA.task3.filter.BasicAuthFilter;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.validation.ValidationFeature;

import jakarta.ws.rs.ApplicationPath;

@ApplicationPath("webapi")
public class ApplicationConfig extends ResourceConfig {
	public ApplicationConfig() {
		packages("SOA.task3");

		register(ValidationFeature.class);
		register(BasicAuthFilter.class);
	}

}
