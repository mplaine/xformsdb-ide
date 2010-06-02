package fi.tkk.media.xide.client.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class AccessRights implements Serializable{
	
	public static final int RIGHT_GRANTED = 1;
	public static final int RIGHT_NOT_GRANTED = 0;
	public static final int RIGHT_DISABLED = -1;
	
	ArrayList<String> roles;
	ArrayList<User> users;
	// TODO: add
	// default user!
	// default role!

	
	public AccessRights() {
		roles = new ArrayList<String>();
		users = new ArrayList<User>();
	}
	
	public static ArrayList<Integer> getAccessRightsSettingsForChild(ArrayList<Integer> parenSettings){
		ArrayList<Integer> childSettings = new ArrayList<Integer>();
		for (Iterator<Integer> iterator = parenSettings.iterator(); iterator.hasNext();) {
			int o = iterator.next();
			if (o == RIGHT_NOT_GRANTED) {
				o = RIGHT_DISABLED;
			}
			childSettings.add(o);
		}
		
		return childSettings;
	}
	
	public ArrayList<String> getRoles(){
		return roles;
	}
	public ArrayList<User> getUsers(){
		return users;
	}
	
	public void setFakeAccessRights() {
		users.add(new User("id_1", "John", "ddd"));
		users.add(new User("id_3", "Jane", "ddd"));
		
		roles.add("Main person");
		roles.add("Simple User");
		
		users.get(0).addRole(roles.get(1));
		users.get(1).addRole(roles.get(0));
	}
	
	public void addUser(String id, String name, String pass) {
		User user = new User(id, name, pass);
		
		// TODO: check name consistency
		users.add(user);
	}
	
	public void addUser(User user) {
		if (!users.contains(user)) {
			users.add(user);	
		}
		else {
//			System.out.println("AccessRights: add user. User already exists.");
		}
	}
	
	public void addRole(String role) {
		if(!roles.contains(role)) {
			roles.add(role);
		}
		else {
			System.out.println("AccessRights: add role. Role already exists.");
		}
	}
	
	public ArrayList<User> getUsersByRole(String role) {
		ArrayList<User> users = new ArrayList<User>();
		for (Iterator<User> iterator = users.iterator(); iterator.hasNext();) {
			User user = iterator.next();
			if (user.getRoles().contains(role)) {
				users.add(user);
			}
		}
		
		return users;
	}
	
//	public User getUser() {
//		
//	}
}
