package SOA.task3.classes;

import java.util.ArrayList;

public class Owner {
	private int id;
	private String name;
	private ArrayList<Gnome> gnomes;
	
	@Override
	public String toString() {
		return  String.format("Id: %d; String: %s; Creator %s", id, name, gnomes.toString());
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<Gnome> getGnomes() {
		return gnomes;
	}
	public void setGnomes(ArrayList<Gnome> gnomes) {
		this.gnomes = gnomes;
	}
} 
