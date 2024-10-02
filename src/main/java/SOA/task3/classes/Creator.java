package SOA.task3.classes;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import SOA.task3.classes.SimpleLink;

public class Creator {
	private long id;
	@NotNull(message = "name is required")
	private String name;
	@NotNull(message = "gnomesIds is required")
	private ArrayList<Long> gnomesIds;

	private List<SimpleLink> links = new ArrayList<>();

	public Creator() {
	}

	public Creator(long id, String name, ArrayList<Long> gnomesIds) {
		this.id = id;
		this.name = name;
		this.gnomesIds = gnomesIds;

	}

	@Override
	public String toString() {
		String gnomesString = gnomesIds.toString();
		return String.format("Id: %d; Name: %s; Gnomes %s", id, name, gnomesString);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Long> getGnomesIds() {
		return gnomesIds;
	}

	public void setGnomesIds(ArrayList<Long> gnomesIds) {
		this.gnomesIds = gnomesIds;
	}

	public List<SimpleLink> getLinks() {
		return links;
	}

	public void setLinks(List<SimpleLink> links) {
		this.links = links;
	}

}
