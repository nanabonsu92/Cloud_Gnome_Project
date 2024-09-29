package SOA.task3.services;

import java.util.ArrayList;
import java.util.List;
import SOA.task3.classes.Owner;
import SOA.task3.exceptions.IdNotFoundException;
import SOA.task3.exceptions.IdAlreadyInUseException;

public class OwnersService {
    private static ArrayList<Owner> ownerList = new ArrayList<>();

    // Pre-populate some owners for testing
    static {
        ownerList.add(new Owner("Alice"));
        ownerList.add(new Owner("Bob"));
        ownerList.add(new Owner("Charlie"));
    }

    // Get an owner by ID
    public Owner getOwnerFromId(long id) {
        for (Owner owner : ownerList) {
            if (owner.getId() == id) {
                return owner;
            }
        }
     // If no owner is found, throw IdNotFoundException
        throw new IdNotFoundException("Owner ID: " + id + " not found");
    }

    // Get all owners
    public List<Owner> getAllOwners() {
        return ownerList;
    }

    // Add a new owner
    public Owner addOwner(Owner owner) {
    	// Check if the ID is already in use
        if (getOwnerFromId(owner.getId()) != null) {
            throw new IdAlreadyInUseException("Owner ID: " + owner.getId() + " already in use");
        }
        ownerList.add(owner);
        return owner;
    }

    // Update an existing owner (if needed)
    public Owner updateOwner(long id, Owner owner) {
        for (Owner o : ownerList) {
            if (o.getId() == id) {
                o.setName(owner.getName());
                o.setGnomeIds(owner.getGnomeIds());
                return o;
            }
        }
        return null;
    }

    // Delete an owner by ID
    public Owner deleteOwner(long id) {
        for (Owner owner : ownerList) {
            if (owner.getId() == id) {
                ownerList.remove(owner);
                return owner;
            }
        }
        return null;
    }
}