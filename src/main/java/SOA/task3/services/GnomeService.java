package SOA.task3.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import SOA.task3.classes.Gnome;
import SOA.task3.classes.SimpleLink;
import SOA.task3.exceptions.IdNotFoundException;

public class GnomeService {
	// The singleton instance, declared as volatile for thread safety
	private static volatile GnomeService instance;

	private List<Gnome> gnomeList = new ArrayList<>();
	private long idCounter = 1;

	// constructor for testing
	private GnomeService() {
		addGnome("beard", 0);
		addGnome("long_beard", 0);
		addGnome("creepy", 0);
	}

	// Public method to provide access to the singleton instance
	public static GnomeService getInstance() {
		if (instance == null) {
			// Double-checked locking to ensure thread safety and lazy initialization
			synchronized (GnomeService.class) {
				if (instance == null) {
					instance = new GnomeService();
				}
			}
		}
		return instance;
	}

	public Gnome addGnome(String nickName, long creatorId) {
		Gnome gnome = new Gnome();
		long gnome_id = idCounter++;
		gnome.setId(gnome_id);
		gnome.setNickName(nickName);
		gnome.setOwnerId(-1); // no owner by default
		gnome.setCreatorId(creatorId);
		gnomeList.add(gnome);
		setCreator_link(gnome_id, creatorId);

		// TODO: add gnome_link to creator

		return gnome;
	}

	public List<Gnome> getAllGnomes() {
		return new ArrayList<>(gnomeList);
	}

	public Optional<Gnome> getGnomeById(long id) {
		return gnomeList.stream().filter(gnome -> gnome.getId() == id).findFirst();
	}

	public Optional<Gnome> updateGnomeOwner(long gnomeId, long newOwnerId) {
		Optional<Gnome> gnomeOpt = getGnomeById(gnomeId);
		gnomeOpt.ifPresent(gnome -> gnome.setOwnerId(newOwnerId));
		setOwner_link(gnomeId, newOwnerId);
		return gnomeOpt;
	}

	public boolean deleteGnome(long id) {
		return gnomeList.removeIf(gnome -> gnome.getId() == id);
	}

	private void setCreator_link(long gnomeId, long creatorId) {
		Optional<Gnome> gnome = getGnomeById(gnomeId);
		if (gnome.isPresent()) {
			// Create the creator link using SimpleLink
			String creatorHref = "/creators/" + creatorId;
			SimpleLink creatorLink = new SimpleLink("creator", creatorHref);

			// Add the creator link to the gnome object
			gnome.get().addLink(creatorLink);

		} else {
			throw new IdNotFoundException("Gnome Id: " + gnomeId + " not found");
		}
	}

	private void setOwner_link(long gnomeId, long ownerId) {
		Optional<Gnome> gnome = getGnomeById(gnomeId);
		if (gnome.isPresent()) {
			// Create the owner link using SimpleLink
			String ownerHref = "/owners/" + ownerId;
			SimpleLink ownerLink = new SimpleLink("owner", ownerHref);

			// Add the owner link to the gnome object
			gnome.get().addLink(ownerLink);

		} else {
			throw new IdNotFoundException("Gnome Id: " + gnomeId + " not found");
		}
	}

}
