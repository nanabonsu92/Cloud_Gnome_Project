package SOA.task3.pathProviders;

import java.util.List;
import java.util.Optional;

import SOA.task3.classes.Gnome;
import SOA.task3.exceptions.IdNotFoundException;
import SOA.task3.services.GnomeService;
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
	public Gnome setGnome(@QueryParam("nickname") String nickName, @QueryParam("creatorId") long creatorId) {
		Gnome new_gnome = gnomeService.addGnome(nickName, creatorId);
		addHateoasLinks(new_gnome);

		return new_gnome;
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public Optional<Gnome> updateGnomeOwner(@QueryParam("gnomeId") long gnomeId,
			@QueryParam("newOwnerId") long newOwnerId) {
		Optional<Gnome> updatedGnome = gnomeService.updateGnomeOwner(gnomeId, newOwnerId);
		updatedGnome.ifPresent(this::addHateoasLinks);
		return updatedGnome;
	}

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public boolean deleteGnome(@QueryParam("gnomeId") long gnomeId) {
		return gnomeService.deleteGnome(gnomeId);
	}

	// Add HATEOAS links for creator and owner
	private void addHateoasLinks(Gnome gnome) {
		// Check if the self link is already present, and add it if not
		boolean selfLinkExists = gnome.getLinks().stream().anyMatch(link -> "self".equals(link.getRel()));

		if (!selfLinkExists) {
			Link selfLink = Link
					.fromUri(uriInfo.getBaseUriBuilder().path(Gnomes.class).path(Long.toString(gnome.getId())).build())
					.rel("self").build();
			gnome.addLink(selfLink);
		}

		// Check if the creator link is already present, and add it if not
		boolean creatorLinkExists = "creator"
				.equals(gnome.getCreator_link() != null ? gnome.getCreator_link().getRel() : null);

		if (!creatorLinkExists) {
			Link creatorLink = Link.fromUri(
					uriInfo.getBaseUriBuilder().path("/creator").path(Long.toString(gnome.getCreatorId())).build())
					.rel("creator").build();
			gnome.setCreator_link(creatorLink);
		}

		// Check if the owner link is already present, and add it if not, only if the
		// owner exists
		if (gnome.getOwnerId() != -1) {
			boolean ownerLinkExists = "owner"
					.equals(gnome.getOwner_link() != null ? gnome.getOwner_link().getRel() : null);

			if (!ownerLinkExists) {
				Link ownerLink = Link.fromUri(
						uriInfo.getBaseUriBuilder().path("/owners").path(Long.toString(gnome.getOwnerId())).build())
						.rel("owner").build();
				gnome.setOwner_link(ownerLink);
			}
		}
	}

}
