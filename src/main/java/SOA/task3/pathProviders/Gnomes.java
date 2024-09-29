package SOA.task3.pathProviders;


import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import SOA.task3.classes.Gnome;
import SOA.task3.exceptions.IdAlreadyInUseException;
import SOA.task3.exceptions.IdNotFoundException;
import SOA.task3.services.GnomeService;
import jakarta.validation.Valid;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.QueryParam;

@Path("/gnomes")
@Produces(MediaType.APPLICATION_JSON)
public class Gnomes {
	// Access the singleton instance of GnomeService
    private GnomeService gnomeService = GnomeService.getInstance();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Gnome> getGnomes() {
		return gnomeService.getAllGnomes();
	}
	
	@GET
	@Path("/{gnomeId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Gnome getGnomeById(@PathParam("gnomeId") long gnomeId) {
		Optional<Gnome> gnome =  gnomeService.getGnomeById(gnomeId);
		if (!gnome.isPresent())
			throw new IdNotFoundException("Gnome Id: " + gnomeId + " not found");

		return gnome.get();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Gnome setGnome(@QueryParam("nickname") String nickName, @QueryParam("creatorId") long creatorId) {
		return gnomeService.addGnome(nickName, creatorId);
	}
	
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public Optional<Gnome> updateGnomeOwner(@QueryParam("oldOwnerId") long oldOwnerId, @QueryParam("newOwnerId") long newOwnerId) {
		return gnomeService.updateGnomeOwner(oldOwnerId, newOwnerId);
	}
	
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public boolean deleteGnome(@QueryParam("gnomeId") long gnomeId) {
		return gnomeService.deleteGnome(gnomeId);
	}

}
