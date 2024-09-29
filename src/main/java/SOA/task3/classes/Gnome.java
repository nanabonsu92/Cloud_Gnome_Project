package SOA.task3.classes;

import java.util.ArrayList;
import java.util.List;
import SOA.task3.classes.Link;


public class Gnome {
	private long id;
	private String nickName;
	private long ownerId; // -1 for no owner
	private long creatorId;
	
	// links
	private Link owner_link;
	private Link creator_link;
	
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


	public Link getCreator_link() {
		return creator_link;
	}


	public void setCreator_link(Link creator_link) {
		this.creator_link = creator_link;
	}


	public Link getOwner_link() {
		return owner_link;
	}


	public void setOwner_link(Link owner_link) {
		this.owner_link = owner_link;
	}




	
}
