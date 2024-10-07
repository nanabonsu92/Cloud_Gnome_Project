package SOA.task3.classes;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Owner {

	private long id;

	@NotNull(message = "Name cannot be null")
	@Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
	private String name;

	// List of Gnome objects owned by this Owner
	@NotNull(message = "Gnomes cannot be null")
	private List<Gnome> gnomes = new ArrayList<>();

	// HATEOAS links
	private List<SimpleLink> links = new ArrayList<>();

	public Owner() {
		this.gnomes = new ArrayList<>();
	}

	public Owner(String name) {
		this.name = name;
		this.gnomes = new ArrayList<>();
	}

	@Override
	public String toString() {
		return String.format("Id: %d; Name: %s; Gnomes: %s", id, name, gnomes.toString());
	}

	// Getters and setters
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

	// Get the list of Gnomes owned by this owner
	public List<Gnome> getGnomes() {
		return gnomes;
	}

	// Set the list of Gnomes owned by this owner
	public void setGnomes(List<Gnome> gnomes) {
		this.gnomes = gnomes;
	}

	// Add a Gnome to the Owner's list
	public void addGnome(Gnome gnome) {
		gnomes.add(gnome);
		gnome.setOwnerId(this.id); // Link the Gnome to this owner
	}

	// HATEOAS Links handling
	public List<SimpleLink> getLinks() {
		return links;
	}

	public void addLink(SimpleLink link) {
		links.add(link);
	}
}