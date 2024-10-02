package SOA.task3.classes;

import java.util.ArrayList;
import java.util.List;
import SOA.task3.classes.SimpleLink;


public class Gnome {
	private long id;
	private String nickName;
	private long ownerId; // -1 for no owner
	private long creatorId;
	
	// links
	private List<SimpleLink> links = new ArrayList<>();
	
	public Gnome() {
	}
	
	
	@Override
	public String toString() {
		return  String.format("Id: %d; Owner: %d; Creator %d", id, ownerId, creatorId);
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(long ownerId) {
		this.ownerId = ownerId;
	}

	public long getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
	}



    public List<SimpleLink> getLinks() {
        return links;
    }

    public void addLink(SimpleLink link) {
        this.links.add(link);
    }

	
}
