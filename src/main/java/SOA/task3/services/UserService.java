package SOA.task3.services;

import org.mindrot.jbcrypt.BCrypt;
import java.util.ArrayList;
import java.util.List;

import SOA.task3.classes.User;

public class UserService {
	
	private static List<User> userList = new ArrayList<>(
	        List.of(
	            new User("jenny123", "hashedPassword", "$2a$10$3nl6dcPFNLGB1cMTUC8K/egAhfbelEH/SDzuww8I/Z3vQl9rC3sfe")
	        )
	    );
    private static long idCounter = 1;

    public User registerUser(String username, String password, String email) {
        if (getUserByUsername(username) != null) {
            throw new RuntimeException("Username already exists");
        }

        String hashedPassword = hashPassword(password);
        User newUser = new User(username, hashedPassword, email);
        newUser.setId(idCounter++);

        userList.add(newUser);
        return newUser;
    }
    
    public User getUserByUsername(String username) {
        return userList.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    public String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    public boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}

