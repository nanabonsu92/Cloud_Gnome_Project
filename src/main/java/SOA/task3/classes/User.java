package SOA.task3.classes;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class User {
	private long id;

	@NotNull(message = "Username cannot be null")
	@Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
	private String username;

	@NotNull(message = "Password cannot be null")
	@Size(min = 8, message = "Password must be at least 8 characters")
	private String password;

	@Email(message = "Email should be valid")
	private String email;
	private List<String> roles;

	private String firstName;
	private String lastName;

	// Constructors, getters, and setters
	public User() {
	}

	public User(String username, String password, String email) {
		this.setUsername(username);
		this.setPassword(password);
		this.setEmail(email);
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

}
