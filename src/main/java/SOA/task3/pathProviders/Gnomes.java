package SOA.task3.pathProviders;

import java.util.List;
import java.util.Optional;

import SOA.task3.classes.Gnome;
import SOA.task3.classes.SimpleLink;
import SOA.task3.exceptions.IdNotFoundException;
import SOA.task3.services.GnomeService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Link;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriInfo;

@Path("/gnomes")
@Produces(MediaType.APPLICATION_JSON)
public class Gnomes {
	// Access the singleton instance of GnomeServic
	private GnomeService gnomeService = GnomeService.getInstance();

	@Context
	private UriInfo uriInfo;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Gnome> getGnomes() {
		List<Gnome> gnomes = gnomeService.getAllGnomes();
		for (Gnome gnome : gnomes) {
			addHateoasLinks(gnome);
		}

		return gnomes;
	}

	@GET
	@Path("/{gnomeId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Gnome getGnomeById(@PathParam("gnomeId") long gnomeId) {
		Optional<Gnome> gnome = gnomeService.getGnomeById(gnomeId);
		if (!gnome.isPresent())
			throw new IdNotFoundException("Gnome Id: " + gnomeId + " not found");
		addHateoasLinks(gnome.get());
		return gnome.get();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Gnome setGnome(
			@QueryParam("nickname") @NotNull(message = "Nickname cannot be null") @Size(min = 3, max = 50, message = "Nickname must be between 3 and 50 characters") String nickName,
			@QueryParam("creatorId") @NotNull(message = "creatorId cannot be null") @Min(value = 0, message = "Creator ID must be a positive number") long creatorId) {
		Gnome new_gnome = gnomeService.addGnome(nickName, creatorId);
		addHateoasLinks(new_gnome);

		return new_gnome;
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public Optional<Gnome> updateGnomeOwner(
			@QueryParam("gnomeId") @NotNull(message = "gnomeId cannot be null") long gnomeId,
			@QueryParam("newOwnerId") @NotNull(message = "newOwnerId cannot be null") long newOwnerId) {
		Optional<Gnome> updatedGnome = gnomeService.updateGnomeOwner(gnomeId, newOwnerId);
		updatedGnome.ifPresent(this::addHateoasLinks);
		return updatedGnome;
	}

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public boolean deleteGnome(@QueryParam("gnomeId") @NotNull(message = "gnomeId cannot be null") long gnomeId) {
		return gnomeService.deleteGnome(gnomeId);
	}

	private void addHateoasLinks(Gnome gnome) {
	    // Check if the self link is already present, and add it if not
	    boolean selfLinkExists = gnome.getLinks().stream().anyMatch(link -> "self".equals(link.getRel()));

	    if (!selfLinkExists) {
	        String selfHref = uriInfo.getBaseUriBuilder()
	                .path(Gnomes.class)
	                .path(Long.toString(gnome.getId()))
	                .build()
	                .toString();
	        gnome.addLink(new SimpleLink("self", selfHref));
	    }

	    // Check if the creator link is already present, and add it if not
	    boolean creatorLinkExists = gnome.getLinks().stream().anyMatch(link -> "creator".equals(link.getRel()));

	    if (!creatorLinkExists) {
	        String creatorHref = uriInfo.getBaseUriBuilder()
	                .path("creators")
	                .path(Long.toString(gnome.getCreatorId()))
	                .build()
	                .toString();
	        gnome.addLink(new SimpleLink("creator", creatorHref));
	    }

	    // Check if the owner link is already present, and add it if not, only if the owner exists
	    if (gnome.getOwnerId() != -1) {
	        boolean ownerLinkExists = gnome.getLinks().stream().anyMatch(link -> "owner".equals(link.getRel()));

	        if (!ownerLinkExists) {
	            String ownerHref = uriInfo.getBaseUriBuilder()
	                    .path("owners")
	                    .path(Long.toString(gnome.getOwnerId()))
	                    .build()
	                    .toString();
	            gnome.addLink(new SimpleLink("owner", ownerHref));
	        }
	    }
	}



}
