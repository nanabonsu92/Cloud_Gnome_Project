package SOA.task3.classes;

import java.util.ArrayList;

public class Owner {
	private long id;
	private String name;
	private ArrayList<Long> gnomeIds;

	public Owner(long id, String name, ArrayList<Long> gnomeIds) {
		this.id = id;
		this.name = name;
		this.gnomeIds = gnomeIds;

	}

	@Override
	public String toString() {
		return String.format("Id: %d; String: %s; Creator %s", id, name, gnomeIds.toString());
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

	public ArrayList<Long> getGnomeIds() {
		return gnomeIds;
	}

	public void setGnomeIds(ArrayList<Long> gnomeIds) {
		this.gnomeIds = gnomeIds;
	}

}
