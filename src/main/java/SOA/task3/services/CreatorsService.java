package SOA.task3.services;

import java.util.ArrayList;
import java.util.List;

import SOA.task3.classes.Creator;

public class CreatorsService {
	private static ArrayList<Creator> creatorsList = new ArrayList<Creator>();
	
	//for testing
	static {
        creatorsList.add(new Creator("Alice"));
        creatorsList.add(new Creator("Bob"));
        creatorsList.add(new Creator("Charlie"));
    }
	
	
	public Creator getCreatorFromId(long id) {
		for (Creator c : creatorsList) {
			if (c.getId() == id) return c;
		}
		return null;
	}
	
	public List<Creator> getAllCreators() {
		return creatorsList;
	}
	
	public Creator addCreator(Creator creator) {
		creatorsList.add(creator);
		return creator;
	}
}
