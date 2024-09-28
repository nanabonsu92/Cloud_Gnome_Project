package SOA.task3;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/creators")
public class Creators {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getAllCreators() {
        return "Creators pls!";
    }
}
