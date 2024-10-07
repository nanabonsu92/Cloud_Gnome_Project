package SOA.task3.services;

import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

import SOA.task3.classes.User;

public class UserService {

	private static List<User> userList = new ArrayList<>(List.of(
			new User("jenny123", "hashedPassword", "$2a$10$3nl6dcPFNLGB1cMTUC8K/egAhfbelEH/SDzuww8I/Z3vQl9rC3sfe")));
	private static long idCounter = 1;

	public User registerUser(String username, String password, String email, List<String> roles) {
		if (getUserByUsername(username) != null) {
			throw new RuntimeException("Username already exists");
		}

		String hashedPassword = hashPassword(password);
		User newUser = new User(username, hashedPassword, email);
		newUser.setId(idCounter++);
		newUser.setRoles(roles);

		userList.add(newUser);
		return newUser;
	}

	public User getUserByUsername(String username) {
		return userList.stream().filter(user -> user.getUsername().equals(username)).findFirst().orElse(null);
	}

	public static String hashPassword(String plainPassword) {
		return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
	}

	public static boolean checkPassword(String plainPassword, String hashedPassword) {
		return BCrypt.checkpw(plainPassword, hashedPassword);
	}

	public static boolean userHasOneOfRoles(User user, List<String> roles) {
		List<String> userRoles = user.getRoles();
		return checkRoles(userRoles, roles);
	}

	public static boolean checkRoles(List<String> userRoles, List<String> requiredRoles) {
		boolean found = false;
		for (String userRole : userRoles) {
			if (requiredRoles.contains(userRole)) {
				found = true;
				break;
			}
		}
		return found;
	}
}
