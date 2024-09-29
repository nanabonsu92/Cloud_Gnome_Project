package SOA.task3.services;

import SOA.task3.classes.Gnome;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class GnomeService {
	// The singleton instance, declared as volatile for thread safety
    private static volatile GnomeService instance;
    
	private List<Gnome> gnomeList = new ArrayList<>();
	private long idCounter = 1;
	
	// constructor for testing
	private GnomeService() {
		addGnome("beard", 1);
		addGnome("long_beard", 2);
		addGnome("creepy", 3);
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
		gnome.setId(idCounter++);
		gnome.setNickName(nickName);
		gnome.setOwnerId(-1); // no owner by default
		gnome.setCreatorId(creatorId);
		gnomeList.add(gnome);
		return gnome;
	}
	
	public List<Gnome> getAllGnomes(){
		return new ArrayList<>(gnomeList);
	}
	
	public Optional<Gnome> getGnomeById(long id){
		return gnomeList.stream().filter(gnome -> gnome.getId() == id).findFirst();
	}
	
	public Optional<Gnome> updateGnomeOwner(long gnomeId, long newOwnerId){
		Optional<Gnome> gnomeOpt = getGnomeById(gnomeId);
		gnomeOpt.ifPresent(gnome -> gnome.setOwnerId(newOwnerId));
		return gnomeOpt;
	}
	
	public boolean deleteGnome(long id) {
		return gnomeList.removeIf(gnome -> gnome.getId() == id);
	}

}
