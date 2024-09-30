package SOA.task3.services;

import java.util.ArrayList;
import java.util.List;

import SOA.task3.classes.Creator;
import SOA.task3.exceptions.IdNotFoundException;

public class CreatorsService {
	private static ArrayList<Creator> creatorsList = new ArrayList<Creator>();

	// for testing
	static {
		ArrayList<Long> a = new ArrayList<Long>();
		a.add(1l);
		a.add(2l);
		creatorsList.add(new Creator(0, "Alice", a));
		ArrayList<Long> b = new ArrayList<Long>();
		b.add(3l);
		creatorsList.add(new Creator(1, "Bob", b));
		creatorsList.add(new Creator(2, "Charlie", new ArrayList<Long>()));
	}

	public Creator getCreatorFromId(long id, boolean throwExeption) {
		for (Creator c : creatorsList) {
			if (c.getId() == id)
				return c;
		}
		if (throwExeption)
			throw new IdNotFoundException("Creators Id: " + id + " not found");
		return null;
	}

	public List<Creator> getAllCreators() {
		return creatorsList;
	}

	public Creator addCreator(Creator creator) {
		creatorsList.add(creator);
		return creator;
	}

	public Creator updateCreator(long id, Creator creator) {
		Creator c = getCreatorFromId(id, true);
		c.setName(creator.getName());
		c.setGnomesIds(creator.getGnomesIds());
		return c;
	}

	public Creator deleteCreator(long id) {
		Creator c = getCreatorFromId(id, true);
		creatorsList.remove(c);
		return c;
	}
}
