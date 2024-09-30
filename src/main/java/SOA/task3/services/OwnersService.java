package SOA.task3.services;

import java.util.ArrayList;
import java.util.List;

import SOA.task3.classes.Gnome;
import SOA.task3.classes.Owner;
import SOA.task3.exceptions.IdAlreadyInUseException;
import SOA.task3.exceptions.IdNotFoundException;

public class OwnersService {
    private static ArrayList<Owner> ownerList = new ArrayList<>();

    // Pre-populate some owners for testing
    static {
        Owner alice = new Owner("Alice");
        Owner bob = new Owner("Bob");
        Owner charlie = new Owner("Charlie");

        // Adding sample gnomes to owners for testing purposes
        Gnome gnome1 = new Gnome();
        gnome1.setId(1);
        gnome1.setNickName("Gnome1");
        gnome1.setOwnerId(alice.getId());

        Gnome gnome2 = new Gnome();
        gnome2.setId(2);
        gnome2.setNickName("Gnome2");
        gnome2.setOwnerId(bob.getId());

        alice.addGnome(gnome1);
        bob.addGnome(gnome2);

        ownerList.add(alice);
        ownerList.add(bob);
        ownerList.add(charlie);
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
        if (ownerList.stream().anyMatch(o -> o.getId() == owner.getId())) {
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

                // Update gnomes list instead of gnomeIds
                o.setGnomes(owner.getGnomes());
                return o;
            }
        }
        // If no owner is found, throw IdNotFoundException
        throw new IdNotFoundException("Owner ID: " + id + " not found");
    }

    // Delete an owner by ID
    public Owner deleteOwner(long id) {
        Owner ownerToRemove = null;
        for (Owner owner : ownerList) {
            if (owner.getId() == id) {
                ownerToRemove = owner;
                break;
            }
        }
        if (ownerToRemove != null) {
            ownerList.remove(ownerToRemove);
            return ownerToRemove;
        } else {
            throw new IdNotFoundException("Owner ID: " + id + " not found");
        }
    }
}