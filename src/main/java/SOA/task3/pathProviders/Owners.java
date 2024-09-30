package SOA.task3.pathProviders;

import java.util.List;
import java.net.URI;
import SOA.task3.classes.Owner;
import SOA.task3.exceptions.IdAlreadyInUseException;
import SOA.task3.exceptions.IdNotFoundException;
import SOA.task3.services.OwnersService;
import jakarta.validation.Valid;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.core.Link;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.core.Context;

@Path("/owners")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class Owners {
    OwnersService ownersService = new OwnersService();

    @Context
    UriInfo uriInfo;  // Injects information about the current URI

    // GET /owners - Fetch all owners
    @GET
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
    public Response getOwner(@PathParam("ownerId") long id) {
        Owner owner = ownersService.getOwnerFromId(id);
        if (owner == null) {
            throw new IdNotFoundException("Owner ID: " + id + " not found");
        }

        // Add HATEOAS links to the owner
        addLinksToOwner(owner);

        return Response.ok(owner).build();
    }

    // POST /owners - Add a new owner
    @POST
    public Response addOwner(@Valid Owner owner) {
        if (ownersService.getOwnerFromId(owner.getId()) != null) {
            throw new IdAlreadyInUseException("Owner ID: " + owner.getId() + " already in use");
        }

        Owner newOwner = ownersService.addOwner(owner);
        
        // Create URI for the newly created owner
        URI uri = UriBuilder.fromUri(uriInfo.getAbsolutePath())
                .path(String.valueOf(newOwner.getId()))
                .build();

        // Add HATEOAS links to the new owner
        addLinksToOwner(newOwner);

        return Response.created(uri).entity(newOwner).build();
    }

    // PUT /owners/{ownerId} - Update an existing owner
    @PUT
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
    @Path("/{ownerId}")
    public Response deleteOwner(@PathParam("ownerId") long id) {
        Owner owner = ownersService.deleteOwner(id);
        if (owner == null) {
            throw new IdNotFoundException("Owner ID: " + id + " not found");
        }

        // Add HATEOAS links to the deleted owner (optional, could just return a confirmation message)
        addLinksToOwner(owner);

        return Response.ok(owner).build();
    }

    // Method to add HATEOAS links to the Owner object
    private void addLinksToOwner(Owner owner) {
        // Self link (the current resource)
        URI selfUri = UriBuilder.fromUri(uriInfo.getAbsolutePath())
                .path(String.valueOf(owner.getId()))
                .build();
        Link selfLink = Link.fromUri(selfUri).rel("self").build();
        owner.addLink(selfLink);

        // Link to the owner's gnomes
        URI gnomesUri = UriBuilder.fromUri(uriInfo.getBaseUri())
                .path("owners")
                .path(String.valueOf(owner.getId()))
                .path("gnomes")  // Assuming an endpoint for gnomes exists
                .build();
        Link gnomesLink = Link.fromUri(gnomesUri).rel("gnomes").build();
        owner.addLink(gnomesLink);
    }
}
