package SOA.task3.pathProviders;

import java.net.URI;
import java.util.List;

import SOA.task3.classes.Creator;
import SOA.task3.classes.Gnome;
import SOA.task3.classes.Owner;
import SOA.task3.classes.SimpleLink;
import SOA.task3.exceptions.IdNotFoundException;
import SOA.task3.services.CreatorsService;
import SOA.task3.services.GnomeService;
import SOA.task3.services.OwnersService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;

@Path("/owners")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class Owners {
	OwnersService ownersService = new OwnersService();
	GnomeService gnomeService = GnomeService.getInstance();

	@Context
	UriInfo uriInfo; // Injects information about the current URI

	// GET /owners - Fetch all owners
	@GET
	@RolesAllowed({ "admin", "user" })
	public Response getOwners() {
		List<Owner> owners = ownersService.getAllOwners();

		// Add HATEOAS links to each owner
		for (Owner owner : owners) {
			addLinksToOwner(owner);
		}

		return Response.ok(owners).build();
	}

	// GET /owners/{ownerId} - Fetch an owner by ID
	@GET
	@Path("/{ownerId}")
	@RolesAllowed({ "admin", "user" })
	public Response getOwner(@PathParam("ownerId") long id) {
		Owner owner = ownersService.getOwnerFromId(id);
		if (owner == null) {
			throw new IdNotFoundException("Owner ID: " + id + " not found");
		}

		// Add HATEOAS links to the owner
		addLinksToOwner(owner);

		return Response.ok(owner).build();
	}

	// Nested Resource: Get all gnomes for an owner
	@GET
	@Path("/{ownerId}/gnomes")
	@RolesAllowed({ "admin", "user" })
	public Response getGnomesForOwner(@PathParam("ownerId") long ownerId) {
		Owner owner = ownersService.getOwnerFromId(ownerId);
		List<Gnome> gnomes = owner.getGnomes();
		return Response.ok(gnomes).build();
	}

	// Nested Resource: Get creator of a specific gnome for an owner
	@GET
	@Path("/{ownerId}/gnomes/{gnomeId}/creator")
	@RolesAllowed({ "admin", "user" })
	public Response getCreatorForOwnerGnome(@PathParam("ownerId") long ownerId, @PathParam("gnomeId") long gnomeId) {
		Gnome gnome = gnomeService.getGnomeById(gnomeId)
				.orElseThrow(() -> new IdNotFoundException("Gnome Id: " + gnomeId + " not found"));

		if (gnome.getOwnerId() != ownerId) {
			throw new IdNotFoundException("Gnome does not belong to this owner.");
		}

		Creator creator = new CreatorsService().getCreatorFromId(gnome.getCreatorId(), true);
		return Response.ok(creator).build();
	}

	// POST /owners - Add a new owner
	@POST
	@RolesAllowed("admin")
	public Response addOwner(@Valid Owner owner) {

		Owner newOwner = ownersService.addOwner(owner);

		// Create URI for the newly created owner
		URI uri = UriBuilder.fromUri(uriInfo.getAbsolutePath()).path(String.valueOf(newOwner.getId())).build();

		// Add HATEOAS links to the new owner
		addLinksToOwner(newOwner);

		return Response.created(uri).entity(newOwner).build();
	}

	// PUT /owners/{ownerId} - Update an existing owner
	@PUT
	@RolesAllowed("admin")
	@Path("/{ownerId}")
	public Response updateOwner(@PathParam("ownerId") long id, Owner owner) {
		Owner updatedOwner = ownersService.updateOwner(id, owner);
		if (updatedOwner == null) {
			throw new IdNotFoundException("Owner ID: " + id + " not found");
		}

		// Add HATEOAS links to the updated owner
		addLinksToOwner(updatedOwner);

		return Response.ok(updatedOwner).build();
	}

	// DELETE /owners/{ownerId} - Delete an owner by ID
	@DELETE
	@RolesAllowed("admin")
	@Path("/{ownerId}")
	public Response deleteOwner(@PathParam("ownerId") long id) {
		Owner owner = ownersService.deleteOwner(id);
		if (owner == null) {
			throw new IdNotFoundException("Owner ID: " + id + " not found");
		}

		// Add HATEOAS links to the deleted owner (optional, could just return a
		// confirmation message)
		addLinksToOwner(owner);

		return Response.ok(owner).build();
	}

	// Method to add HATEOAS links to the Owner object
	private void addLinksToOwner(Owner owner) {
		// Self link (the current resource)
		URI selfUri = UriBuilder.fromUri(uriInfo.getAbsolutePath()).path(String.valueOf(owner.getId())).build();
		SimpleLink selfLink = new SimpleLink("self", selfUri.toString());
		owner.addLink(selfLink);

		// Link to the owner's gnomes
		URI gnomesUri = UriBuilder.fromUri(uriInfo.getBaseUri()).path("owners").path(String.valueOf(owner.getId()))
				.path("gnomes") // Assuming an endpoint for gnomes exists
				.build();
		SimpleLink gnomesLink = new SimpleLink("gnomes", gnomesUri.toString());
		owner.addLink(gnomesLink);
	}
}
