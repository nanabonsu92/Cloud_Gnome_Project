package SOA.task3.classes;

public class Gnome {
	private int id;
	private String nickName;
	private Owner owner;
	private Creator creator;
	
	
	@Override
	public String toString() {
		return  String.format("Id: %d; Owner: %s; Creator %s", id, owner.getName(), creator.getName());
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public Owner getOwner() {
		return owner;
	}


	public void setOwner(Owner owner) {
		this.owner = owner;
	}


	public Creator getCreator() {
		return creator;
	}


	public void setCreator(Creator creator) {
		this.creator = creator;
	}


	public String getNickName() {
		return nickName;
	}


	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
}
