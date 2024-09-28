package SOA.task3;

import java.util.List;

import SOA.task3.Services.CreatorsService;
import SOA.task3.classes.Creator;
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
	public List<Creator> getPublications(){
		return creatorsService.getAllCreators();
	}

	@GET
	@Path("/{creatorId}")
	public Creator getCreator(@PathParam("creatorId") long id){
		Creator creator = creatorsService.getCreatorFromId(id);
		return creator;
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Creator addCreator(Creator ceator) {
		return creatorsService.addCreator(ceator);
	}
}
