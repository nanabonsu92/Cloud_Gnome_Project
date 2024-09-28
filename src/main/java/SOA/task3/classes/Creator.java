package SOA.task3.classes;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Creator {
	static long id_counter = 0;
	
	private long id;
	private String name;
	private ArrayList<Gnome> gnomes;
	
	public Creator(String name) {
		this.id = id_counter++;
		this.name = name;
		gnomes = new ArrayList<Gnome>();
	}

	@Override
	public String toString() {
		String gnomesString = gnomes.stream()
                .map(Gnome::getNickName)
                .collect(Collectors.joining(", "));
		return  String.format("Id: %d; Name: %s; Gnomes %s", id, name, gnomesString);
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
	public ArrayList<Gnome> getGnomes() {
		return gnomes;
	}
	public void setGnomes(ArrayList<Gnome> gnomes) {
		this.gnomes = gnomes;
	}
	
	
}
