package SOA.task3.pathProviders;

import java.util.List;

import SOA.task3.classes.Creator;
import SOA.task3.exceptions.IdAlreadyInUseException;
import SOA.task3.exceptions.IdNotFoundException;
import SOA.task3.services.CreatorsService;
import jakarta.validation.Valid;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/creators")
@Produces(MediaType.APPLICATION_JSON)
public class Creators {
	CreatorsService creatorsService = new CreatorsService();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Creator> getPublications() {
		return creatorsService.getAllCreators();
	}

	@GET
	@Path("/{creatorId}")
	public Creator getCreator(@PathParam("creatorId") long id) {
		Creator creator = creatorsService.getCreatorFromId(id);
		if (creator == null)
			throw new IdNotFoundException("Creators Id: " + id + " not found");

		return creator;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Creator addCreator(@Valid Creator ceator) {
		// TODO: Check if gnome ids are valid
		if (creatorsService.getCreatorFromId(ceator.getId()) != null)
			throw new IdAlreadyInUseException("Creators Id: " + ceator.getId() + " already in use");
		return creatorsService.addCreator(ceator);
	}
}
