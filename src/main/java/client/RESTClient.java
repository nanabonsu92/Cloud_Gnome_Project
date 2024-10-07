package client;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import SOA.task3.classes.User;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;

public class RESTClient {
	public static void main(String[] args) {
		testBasicAuth();
		// testJWS();
	}

	static void testJWS() {
		Client client = ClientBuilder.newClient();
		WebTarget baseTarget = client.target("http://localhost:8080/task3/webapi/");

		WebTarget gnomesTarget = baseTarget.path("gnomes");
		// WebTarget gnomeTarget = gnomesTarget.path("{gnomeId}");

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
		System.out.println("-----------------------------");

		System.out.println("\n\n\n");
		Response getResponse = gnomesTarget.request().header("Authorization", "Bearer " + token).get();
		System.out.println(getResponse);
	}

	static void testBasicAuth() {
		Client client = ClientBuilder.newClient();
		WebTarget baseTarget = client.target("http://localhost:8080/task3/webapi/");

		WebTarget userTarget = baseTarget.path("users");
		WebTarget registerUserTarget = userTarget.path("register");

		WebTarget gnomesTarget = baseTarget.path("gnomes");

		String name = "stabiaal";
		String password = "12345678";

		User newUser = new User(name, password, "a@bc.com");
		List<String> roles = new ArrayList<String>();
		roles.add("admin");
		newUser.setRoles(roles);
		System.out.println(newUser.toString());
		Response postResponse = registerUserTarget.request().post(Entity.json(newUser));
		System.out.println(postResponse);
		System.out.println("-----------------------------");

		String authString = name + ":" + password;
		String authStringEnc = Base64.getEncoder().encodeToString(authString.getBytes());
		System.out.println("Base64 encoded auth string: " + authStringEnc);
		Response getResponse = gnomesTarget.request().header("Authorization", "Basic " + authStringEnc).get();
		System.out.println(getResponse);
	}
}
