package fi.tkk.media.xide.server;

public class User {
	// Username 
	String userName;
	// User ID received from ASI
	String userID;
	// Full session string received from ASI
	String session;

	public User(String userID, String session, String username) {
		this.userID = userID;
		this.session = session;
		this.userName = username;
	}

	public String getUserName() {
		return userName;
	}

	public String getUserID() {
		return userID;
	}

	public String getSession() {
		return session;
	}
	
}
