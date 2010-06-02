package fi.tkk.media.xide.client.Data;

import java.util.ArrayList;

public class User {
	private String id;
	private String name;
	private String pass;
	private ArrayList<String> roles;
	
	public User(String id, String name, String pass) {
		this.id = id;
		this.name  = name;
		this.pass = pass;
		roles = new ArrayList<String>();
	}
	
	public User getDefaultUser() {
		return new User("default_user_id", "Default User", "");
	}
	
	public void addRole(String newRole) {
		if(!roles.contains(newRole)) {
			roles.add(newRole);
		}
		else {
			System.out.println("User: add role. Role already assigned.");
		}
	}
	
	public void removeRole(String role) {
		if(roles.contains(role)) {
			roles.remove(role);
		}
		else {
			System.out.println("User: remove role. No such role.");
		}
	}
	
	public ArrayList<String> getRoles() {
		return roles;
	}
	
	public String getName() {
		return name;
	}
}
