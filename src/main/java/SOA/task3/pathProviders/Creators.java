package SOA.task3.pathProviders;

import java.util.ArrayList;
import java.util.List;

import SOA.task3.classes.Creator;
import SOA.task3.classes.SimpleLink;
import SOA.task3.exceptions.IdAlreadyInUseException;
import SOA.task3.services.CreatorsService;
import jakarta.validation.Valid;
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
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.UriInfo;

@Path("/creators")
@Produces(MediaType.APPLICATION_JSON)
public class Creators {
	CreatorsService creatorsService = new CreatorsService();

	@Context
	private UriInfo uriInfo;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Creator> getPublications() {
		List<Creator> list = creatorsService.getAllCreators();
		for (Creator c : list) {
			setHATEOASLinks(c);
		}
		return list;
	}

	@GET
	@Path("/{creatorId}")
	public Creator getCreator(@PathParam("creatorId") long id) {
		Creator creator = creatorsService.getCreatorFromId(id, true);

		setHATEOASLinks(creator);
		return creator;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response addCreator(@Valid Creator ceator) {
		// TODO: Check if gnome ids are valid
		if (creatorsService.getCreatorFromId(ceator.getId(), false) != null)
			throw new IdAlreadyInUseException("Creators Id: " + ceator.getId() + " already in use");

		Creator c = creatorsService.addCreator(ceator);
		setHATEOASLinks(c);
		String loc = uriInfo.getBaseUriBuilder().path("creators").path(Long.toString(c.getId())).toString();
		return Response.status(Status.CREATED).header("Location", loc).entity(c).build();
	}

	@PUT
	@Path("/{creatorId}")
	public Creator updateCreator(@PathParam("creatorId") long id, @Valid Creator creator) {
		Creator c = creatorsService.updateCreator(id, creator);
		setHATEOASLinks(c);
		return c;
	}

	@DELETE
	@Path("/{creatorId}")
	public Creator deleteCreator(@PathParam("creatorId") long id) {
		Creator c = creatorsService.deleteCreator(id);
		return c;
	}

	private void setHATEOASLinks(Creator creator) {
	    List<SimpleLink> links = new ArrayList<>();

	    // Create the self link
	    String selfHref = uriInfo.getBaseUriBuilder()
	            .path("creators")
	            .path(Long.toString(creator.getId()))
	            .build()
	            .toString();
	    SimpleLink selfLink = new SimpleLink("self", selfHref);
	    links.add(selfLink);

	    // Create the links for each gnome associated with the creator
	    for (Long gnomeId : creator.getGnomesIds()) {
	        String gnomeHref = uriInfo.getBaseUriBuilder()
	                .path("gnomes")
	                .path(gnomeId.toString())
	                .build()
	                .toString();
	        SimpleLink gnomeLink = new SimpleLink("gnome_" + gnomeId, gnomeHref);
	        links.add(gnomeLink);
	    }

	    // Set the links in the creator object
	    creator.setLinks(links);
	}

}
