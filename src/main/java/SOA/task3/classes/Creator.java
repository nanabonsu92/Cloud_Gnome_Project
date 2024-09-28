package SOA.task3.classes;

import java.util.ArrayList;

import jakarta.validation.constraints.NotNull;

public class Creator {
	private long id;
	@NotNull(message = "name is required")
	private String name;
	@NotNull(message = "gnomesIds is required")
	private ArrayList<Long> gnomesIds;

	public Creator() {
	}

	public Creator(String name) {
		this.name = name;
		gnomesIds = new ArrayList<Long>();
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

}
