package SOA.task3.pathProviders;

import java.util.List;
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
import jakarta.ws.rs.core.MediaType;

@Path("/owners")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class Owners {
    OwnersService ownersService = new OwnersService();

    // GET /owners - Fetch all owners
    @GET
    public List<Owner> getOwners() {
        return ownersService.getAllOwners();
    }

    // GET /owners/{ownerId} - Fetch an owner by ID
    @GET
    @Path("/{ownerId}")
    public Owner getOwner(@PathParam("ownerId") long id) {
        Owner owner = ownersService.getOwnerFromId(id);
        if (owner == null) {
            throw new IdNotFoundException("Owner ID: " + id + " not found");
        }
        return owner;
    }

    // POST /owners - Add a new owner
    @POST
    public Owner addOwner(@Valid Owner owner) {
        if (ownersService.getOwnerFromId(owner.getId()) != null) {
            throw new IdAlreadyInUseException("Owner ID: " + owner.getId() + " already in use");
        }
        return ownersService.addOwner(owner);
    }

    // PUT /owners/{ownerId} - Update an existing owner
    @PUT
    @Path("/{ownerId}")
    public Owner updateOwner(@PathParam("ownerId") long id, Owner owner) {
        Owner updatedOwner = ownersService.updateOwner(id, owner);
        if (updatedOwner == null) {
            throw new IdNotFoundException("Owner ID: " + id + " not found");
        }
        return updatedOwner;
    }

    // DELETE /owners/{ownerId} - Delete an owner by ID
    @DELETE
    @Path("/{ownerId}")
    public Owner deleteOwner(@PathParam("ownerId") long id) {
        Owner owner = ownersService.deleteOwner(id);
        if (owner == null) {
            throw new IdNotFoundException("Owner ID: " + id + " not found");
        }
        return owner;
    }
}
