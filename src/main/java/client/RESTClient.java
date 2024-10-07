package client;

import java.util.ArrayList;
import java.util.List;

import SOA.task3.classes.User;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;

public class RESTClient {
	public static void main(String[] args) {
		Client client = ClientBuilder.newClient();
		WebTarget baseTarget = client.target("http://localhost:8080/task3/webapi/");

		WebTarget gnomesTarget = baseTarget.path("gnomes");
		WebTarget gnomeTarget = gnomesTarget.path("{gnomeId}");

		WebTarget userTarget = baseTarget.path("users");
		WebTarget registerUserTarget = userTarget.path("register");
		WebTarget loginUserTarget = userTarget.path("login");

		User newUser = new User("stabil", "12345678", "a@bc.com");
		List<String> roles = new ArrayList<String>();
		roles.add("admin");
		newUser.setRoles(roles);
		System.out.println(newUser.toString());
		Response postResponse = registerUserTarget.request().post(Entity.json(newUser));
		System.out.println(postResponse);
		System.out.println("-----------------------------");

		postResponse = loginUserTarget.request().post(Entity.json(newUser));
		System.out.println(postResponse);
		System.out.println("-----------------------------");
		String token = postResponse.readEntity(String.class);
		System.out.println(token);

	}
}
