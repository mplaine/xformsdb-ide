/**
 * 
 */
package fi.tkk.media.xide.server;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sun.corba.se.spi.activation.Server;
import com.sun.jndi.toolkit.dir.SearchFilter;
import com.sun.org.apache.bcel.internal.generic.NEW;

import fi.tkk.media.xide.client.Main;
import fi.tkk.media.xide.client.Data.APElement;
import fi.tkk.media.xide.client.Data.APElementApplication;
import fi.tkk.media.xide.client.Data.APElementPage;
import fi.tkk.media.xide.client.Data.ArrayProperty;
import fi.tkk.media.xide.client.Data.BaseProperty;
import fi.tkk.media.xide.client.Data.BooleanProperty;
import fi.tkk.media.xide.client.Data.DateProperty;
import fi.tkk.media.xide.client.Data.Option;
import fi.tkk.media.xide.client.Data.OptionProperty;
import fi.tkk.media.xide.client.Data.Property;
import fi.tkk.media.xide.client.Data.Tag;
import fi.tkk.media.xide.client.Data.Template;
import fi.tkk.media.xide.client.Data.TemplateShortInfo;
import fi.tkk.media.xide.client.Server.RPC.AuthenticationException;
import fi.tkk.media.xide.client.Server.RPC.PublishException;
import fi.tkk.media.xide.client.Server.RPC.XIDEException;
import fi.tkk.media.xide.client.Server.RPC.SearchService;
import fi.tkk.media.xide.client.UI.Widget.AdvancedTableV2;
import fi.tkk.media.xide.client.UI.Widget.WebPage;
import fi.tkk.media.xide.client.fs.XIDEFile;
import fi.tkk.media.xide.client.fs.XIDEFolder;
import fi.tkk.media.xide.client.popups.basic.PopupError;
import fi.tkk.media.xide.client.utils.Cons;
import fi.tkk.media.xide.server.transformation.filehandlers.PageToComponentHandler;
import fi.tkk.media.xide.server.transformation.filehandlers.PublishHandler;
import fi.tkk.media.xide.server.transformation.filehandlers.TemplateLanguageValidator;
import fi.tkk.tml.xformsdb.xml.sax.XFormsDBHandler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.*;
import java.util.Properties;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.httpclient.auth.InvalidCredentialsException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.exist.xmldb.DatabaseImpl;
import org.xml.sax.SAXException;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;

/**
 * @author Evgenia Samochadina
 * @date Nov 20, 2008
 * 
 */

// Server Implementation
public class SearchServiceImpl extends RemoteServiceServlet implements SearchService {

	static SearchServiceImpl instance;

	public static final HashMap<String, User> authorizedUsers = new HashMap<String, User>();

	// Connection to the database
	// Connection conn;
	// Normal statement
	// Statement statement;
	// Statement statementAdvanced;
	String value;

	/** Connection pool for SQL connections */
	private static final ConnectionPool connectionPool = new ConnectionPool();
	private static Connection initialConnection;
	private static final Logger loggerInstance = Logger.getLogger(SearchServiceImpl.class);

	public static final String[] IDs = { "component_0_id", "component_1_id", "component_2_id", "component_3_id",
			"component_4_id", "component_5_id", "component_6_id", "component_7_id" };

	// User management
	// Session attribute keys. Used to store information about authentificated
	// used.
	// ASI User ID
	public static final String USR_ID = "user_id";
	// ASI session ID
	public static final String SS_ID = "ss_id";
	// ASI username
	public static final String USR_NAME = "user_name";

	public SearchServiceImpl() {

		if (instance == null) {
			instance = this;
		}

		try {

			/** Exist-db driver */
			if (!Main.IS_RUNNING_ON_WINDOWS) {
				String driver = "org.exist.xmldb.DatabaseImpl";
				Class<?> cl = Class.forName(driver);
				Database database = (Database) cl.newInstance();
				DatabaseManager.registerDatabase(database);
			}
			/** SQL driver (used normally for local testing) */
			if (!Config.useConnectionPool()) {
				Class.forName("com.mysql.jdbc.Driver");
				initialConnection = DriverManager.getConnection(Config.getConnectionString(), Config
						.getConnectionStringProperties());
			}

			loggerInstance.log(Level.DEBUG, "Drivers for DB connections initialized");

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (XMLDBException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// savePageAsComponent("markku", "survey_widget", "index");
	}

	public static SearchServiceImpl getInstance() {
		if (instance != null) {
			return instance;
		} else {
			return new SearchServiceImpl();
		}
	}

	/**
	 * User management
	 * 
	 */
	static int i = 9;

	// User management
	public String login(String username, String password) throws AuthenticationException {
		// System.out.println("login: going to test: ");
		// If request contains any cookie
		if (this.getThreadLocalRequest().getCookies() != null) {
			// System.out.println("cookies exist");
			// Check if it contains cookie with the specified name
			for (int i = 0; i < this.getThreadLocalRequest().getCookies().length; i++) {
				// System.out.println("c: " +
				// this.getThreadLocalRequest().getCookies()[i].getName() + " "
				// + this.getThreadLocalRequest().getCookies()[i].getValue());
			}
		}

		if (Config.sendCrsToAsi()) {
			Map<String, String> authResult = ASIPlugin.login("xformsdb", "290heuobhc", username, password);

			// If user has successfully authenticated
			if (authResult != null) {
				setAuthEverywhere(authResult.get("cookie"), authResult.get("id"), username);
				return username;
			} else {
				throw new AuthenticationException("Credentials do not fit!");
			}
		} else {
			String fakeSession = "gfwffsdfsdfsdfsd389eiwf" + i + "; ";
			String fakeID = "gfwff389eiwf" + i;
			i++;
			setAuthEverywhere(fakeSession, fakeID, "fakeuser");
			return "fakeuser";
		}
	}

	public void setAuthEverywhere(String sessionID, String userID, String username) {

		if (Config.storeCredentialsInSession()) {
			// Store sessionID received from ASi into the cookies.
			// Store userID and sessionID into the current client-server
			// session.

			// Add session and user id to the client-server current session for
			// further check
			addSuccessfulUserSession(getSession(), userID, sessionID, username);
			HttpSession s = getSession();
			// System.out.println("SESSION ATTRIBUTE USR_ID: " +
			// s.getAttribute(USR_ID));
			// System.out.println("SESSION ATTRIBUTE USR_NAME: " +
			// s.getAttribute(USR_NAME));
			// System.out.println("SESSION ATTRIBUTE SS_ID: " +
			// s.getAttribute(SS_ID));
			// Add session id to the client cookies
			Cookie newCookie = new Cookie(SS_ID, sessionID);
			// Cookie newCookie = new Cookie(SS_ID, "" + sessionID);
			// Cookie will be expired after 10 minutes
			newCookie.setMaxAge(Cons.COOKIE_LIVE_TIME);
			this.getThreadLocalResponse().addCookie(newCookie);
		} else {
			// Store userID and sessionID into the authorizedUsers arraylist
			// Store only valuable part of the sessionID into the cookies
			authorizedUsers.put(getSessionIDFromCookie(sessionID), new User(userID, sessionID, username));
			// Store user credentials in session to be able to logout a user (if
			// session expireds)
			addSuccessfulUserSession(getSession(), userID, getSessionIDFromCookie(sessionID), username);
			// Add session id to the client cookies
			Cookie newCookie = new Cookie(SS_ID, getSessionIDFromCookie(sessionID));
			// Cookie newCookie = new Cookie(SS_ID, "" + sessionID);
			// Cookie will be expired after 10 minutes
			newCookie.setMaxAge(Cons.COOKIE_LIVE_TIME);
			this.getThreadLocalResponse().addCookie(newCookie);
		}
	}

	/**
	 * Gets valuable session id from the string like _trunk_session=BAh7BzoPc2Vzc2lvbl9pZCIlN2I5YWM0NjZkMTE1NzhhOGY1YzkxNjU3MzkwNjRhMzY6E2Nvc19zZXNzaW9uX2lkaQK12w
	 * %3D%3D--4e2d78757530f0b07e2773aa854fd5550aff51c1; path=/; HttpOnly
	 * 
	 * @param cookie
	 * @return
	 */
	public String getSessionIDFromCookie(String cookie) {
		return cookie.substring(0, cookie.indexOf("; "));
	}

	/**
	 * Gets the necessary cookie value (with SS_ID id)
	 * 
	 * @param array
	 * @return cookie value
	 */
	public String getCookieValue() {
		Cookie cookie = getCookie();
		if (cookie != null) {
			return cookie.getValue();
		} else {
			return null;
		}
	}

	/**
	 * Gets the necessary cookie (with SS_ID id)
	 * 
	 * @return Cookie
	 */
	public Cookie getCookie() {
		Cookie cookie = null;
		if (this.getThreadLocalRequest().getCookies() != null) {
			// System.out.println("cookies exist");
			// Check if it contains cookie with the specified name
			for (int i = 0; i < this.getThreadLocalRequest().getCookies().length; i++) {
				// System.out.println("c: " +
				// this.getThreadLocalRequest().getCookies()[i].getName() + " "
				// + this.getThreadLocalRequest().getCookies()[i].getValue());
				if (this.getThreadLocalRequest().getCookies()[i].getName().equals(SS_ID)) {
					cookie = this.getThreadLocalRequest().getCookies()[i];
				}
			}
		}
		if (cookie != null) {
			return cookie;
		} else {
			return null;
		}
	}

	/**
	 * Gets full sessionID required by ASI server depending on the method of
	 * storing authorized users
	 * 
	 * @return
	 */
	public String getFullSessionID() {
		String sessionID = null;
		if (Config.storeCredentialsInSession()) {
			sessionID = getInfoFromSession(SS_ID, getSession());
		} else {
			if (authorizedUsers.containsKey(getCookieValue())) {
				sessionID = authorizedUsers.get(getCookieValue()).getSession();
			}
		}
		return sessionID;
	}

	/**
	 * Gets userID required by ASI server depending on the method of storing
	 * authorized users
	 * 
	 * @return
	 */
	public String getUserID() {
		String sessionID = null;
		if (Config.storeCredentialsInSession()) {
			sessionID = getInfoFromSession(USR_ID, getSession());
		} else {
			if (authorizedUsers.containsKey(getCookieValue())) {
				sessionID = authorizedUsers.get(getCookieValue()).getUserID();
			}
		}
		return sessionID;
	}

	/**
	 * Gets username, which identifies a user in ASI, depending on the method of
	 * storing authorized users
	 * 
	 * @return
	 */
	public String getUniqueUserName() {
		String sessionID = null;
		if (Config.storeCredentialsInSession()) {
			sessionID = getInfoFromSession(USR_NAME, getSession());
		} else {
			if (authorizedUsers.containsKey(getCookieValue())) {
				sessionID = authorizedUsers.get(getCookieValue()).getUserName();
			}
		}
		return sessionID;
	}

	public void logout() throws AuthenticationException {

		// Get ASI session ID from the session
		String sessionID = getFullSessionID();
		if (sessionID == null) {
			sessionID = getInfoFromSession(SS_ID, getSession());
		}
		if (!sessionID.equals("")) {
			boolean logoutResult = ASIPlugin.logout(sessionID);
			if (logoutResult) {

				if (Config.storeCredentialsInSession()) {
					// Remove information about the user from session
					getSession().removeAttribute(SS_ID);
					getSession().removeAttribute(USR_ID);
					getSession().removeAttribute(USR_NAME);
				} else {
					// Remove user from the list
					authorizedUsers.remove(getCookieValue());
					getSession().removeAttribute(SS_ID);
					getSession().removeAttribute(USR_ID);
					getSession().removeAttribute(USR_NAME);
				}
				// Clear cookies
				Cookie newCookie = new Cookie(SS_ID, "");
				newCookie.setMaxAge(0);
				this.getThreadLocalResponse().addCookie(newCookie);
			}
		}

		// TODO: reset everything anyway
		// TODO: show initial page

	}

	public void changePassword(String newPass) throws AuthenticationException, XIDEException {
		// Get ASI session ID from the session
		String sessionID = getFullSessionID();

		if (!sessionID.equals("")) {
			boolean changePasswordResult = ASIPlugin.changePassword(sessionID, newPass);
			if (!changePasswordResult) {
				throw new XIDEException("Password hasn't been changed because of the ASI server error");
			}
		} else {
			throw new AuthenticationException(
					"Your session has expired, please relogin. Password has not been changed.");
		}

	}

	public void recoverPassword(String email) throws AuthenticationException, XIDEException {
		boolean recoverPasswordResult = ASIPlugin.recoverPassword("xformsdb", "290heuobhc", email);
		if (!recoverPasswordResult) {
			throw new XIDEException("Password hasn't been sent because of the ASI server error");
		}
	}

	public void register(String username, String password, String email) throws AuthenticationException, XIDEException {
		boolean recoverPasswordResult;
		try {
			recoverPasswordResult = ASIPlugin.register("xformsdb", "290heuobhc", username, password, email);

			if (!recoverPasswordResult) {
				throw new XIDEException("User hasn't been created! Unknown reason.");
			}

		} catch (InvalidCredentialsException e) {
			throw new XIDEException("User hasn't been created! Possible reason: " + e.getMessage());
		}

	}

	/**
	 * Checks whether user is authenticated. If not, then throws exception.
	 * 
	 * Check is based on the information about authenticated user, which stored
	 * in the session of the HTTPServlet.
	 * 
	 * @throws AuthenticationException
	 */
	private void authenticateUser() throws AuthenticationException {
		// System.out.println("authentication");
		String cookieValue = getCookieValue();
		String fullSessionID = getFullSessionID();

		// System.out.println("session should be " + fullSessionID);
		// System.out.println("cookie.getValue() should be " +
		// cookie.getValue());
		if (cookieValue != null && fullSessionID != null && fullSessionID.contains(cookieValue)) {

			// Change cookie live time
			Cookie c = getCookie();
			c.setMaxAge(Cons.COOKIE_LIVE_TIME);
			this.getThreadLocalResponse().addCookie(c);

			// ASIPlugin.getUserFullName(cookie.getValue(), cookie2.getValue());
			// ASIPlugin.getUserDetails("xformsdb", "290heuobhc",
			// cookie.getValue());
			// if (cookie.getValue().equals(getSession().getId())) {
			// System.out.println("Auth checked: user is ok");
			// }

			// BAh7BzoPc2Vzc2lvbl9pZCIlNDgyZDAwOTFmYzhlYzc4YmQxZWIwMWM5NTgyNzY0ZjE6E2Nvc19zZXNzaW9uX2lkaQIayw%3D%3D--e78cfbf674f01c111ecbc0a67d4d47f38b7c3954;
			// path=/; HttpOnly
			// BAh7BzoPc2Vzc2lvbl9pZCIlZmM1MTg2MWQ3MjNkMzg0NGMyYjdkMDY2ZTU1YTk5MGM6E2Nvc19zZXNzaW9uX2lkaQIWyw%3D%3D--2de65d3649e752f27b850adb4c216a10a1c6aad2;
			// path=/; HttpOnly
		} else {
			// System.out.println("authentication: exception thrown");
			throw new AuthenticationException("User session is invalidated, user must login.");
		}
	}

	public void checkUser() throws AuthenticationException {
		// System.out.println("check user");
		authenticateUser();
		// System.out.println("user has checked");
	}

	/**
	 * Check if user is logged in. If yes, gets user name. Otherwise returns
	 * null. Doesn't throw AuthenticationException.
	 */
	public String getUserName() throws AuthenticationException {
		// System.out.println("get username");
		authenticateUser();
		// Get ASI session ID from the session

		String sessionID = getFullSessionID();
		String userID = getUserID();
		// System.out.println("ok. get name from asi");
		if (Config.sendCrsToAsi()) {

			Map<String, String> details = ASIPlugin.getUserFullName(userID, sessionID);

			if (details != null) {
				String userName = details.get("name");
				// System.out.println("name received");
				return userName;
			} else {
				return null;
			}
		} else {
			return "name";
		}
	}

	/**
	 * Returns the current session
	 * 
	 * @return The current Session
	 */
	private HttpSession getSession() {
		// Get the current request and then return its session
		return this.getThreadLocalRequest().getSession();
	}

	public static String getInfoFromSession(String key, HttpSession session) {
		// If the session does not contain anything, return empty string
		if (session.getAttribute(key) == null) {
			return "";
		} else {
			return (String) session.getAttribute(key);
		}
	}

	/**
	 * Adds user name and session to the session. Used when user has been
	 * successfully identified.
	 * 
	 * @param key
	 *            Key to the HashMap stored in the session
	 * @param session
	 *            HttpSession from which to extract the HashMap
	 * @param zipCode
	 *            zipCode of successful query
	 * @param location
	 *            location of successful query
	 */
	public static void addSuccessfulUserSession(HttpSession session, String userID, String sessionID, String username) {
		// System.out.println("before saving to session: " +
		// session.getAttribute(USR_ID));
		// System.out.println("before saving to session: " +
		// session.getAttribute(SS_ID));
		session.removeAttribute(SS_ID);
		session.removeAttribute(USR_ID);
		session.removeAttribute(USR_NAME);
		session.setAttribute(USR_ID, userID);
		session.setAttribute(SS_ID, sessionID);
		session.setAttribute(USR_NAME, username);
		// System.out.println("after saving to session: " +
		// session.getAttribute(USR_ID));
		// System.out.println("after saving to session: " +
		// session.getAttribute(SS_ID));
	}

	/**
	 * Processes related path under the component template folder into absolute
	 * path according to the place where the application is running.
	 * 
	 * @param relatedPath
	 *            related path
	 * @return resulting absolute path
	 */
	// public String getAbsolutePath(String relatedPath) {
	public String getAbsoluteComponentsPath(String relatedPath) {

		// Get file system path according to current configuration
		String path = Config.getComponentsPath();

		if (path == null) {
			// System.out.println("Server: Problems with receiving file path from the server !");
			return relatedPath;
		} else {
			// Construct full path
			return path + trimRelatedPath(relatedPath);
		}
	}

	/**
	 * Processes related path under the application sources folder into absolute
	 * path according to the place where the application is running.
	 * 
	 * @param relatedPath
	 *            related path
	 * @return resulting absolute path
	 */
	// public String getAbsolutePath(String relatedPath) {
	public String getAbsoluteAppsSourcesPath(String relatedPath) {

		// Get file system path according to current configuration
		String path = Config.getAppsSourcePath();

		if (path == null) {
			// System.out.println("Server: Problems with receiving file path from the server !");
			return relatedPath;
		} else {
			// Construct full path
			return path + trimRelatedPath(relatedPath);
		}
	}

	private String trimRelatedPath(String relatedPath) {

		// Remove unnecessary "/" where needed
		if (relatedPath.startsWith("/")) {
			return relatedPath.substring(1);
		} else {
			return relatedPath;
		}
	}

	public OptionProperty getPageList(String app_id) {
		Property p = getApplicationByID(app_id).getProperties().get(Property.MAIN_PAGE);

		if (p instanceof OptionProperty) {
			return (OptionProperty) p;
		} else {
			return new OptionProperty();
		}
	}

	/**
	 * Performs manual testing of different things
	 * 
	 */
	public Boolean runTesting() throws XIDEException {
		boolean result = true;
		return result;
	}

	/*
	 * Gets the content of the file
	 */
	public String getFileContent(String absolutePath) throws XIDEException {

		try {
			String text = readFileAsString(absolutePath);
			return text;
		} catch (IOException e) {
			throw new XIDEException("Error occured while updating file content. " + e.getMessage());
		}

	}

	// How many files are going to be saved during current saving procedure
	public int numberOfFilesToSave;
	
	// Indicates whether any error has occurred during the current saving process;
	public ArrayList<String> errorTexts = new ArrayList<String>();

	// List of original files' absolute paths which should be saved
	public ArrayList<String> filesToSave = new ArrayList<String>();
	
	public static final String BACKUP_EXT = ".bak";
	
	private final Object synchSaving = new Object();

	public void clearEverythingForNextSaving() {
		System.out.println("lists cleared!");
		errorTexts = new ArrayList<String>();
		filesToSave = new ArrayList<String>();

		
	}
	/**
	 * Should be called before sending set of changed files to save. 
	 * Used to receive an information about how many files are going to be sent and initiate saving related variables
	 */
	public void onBeforeFileSetIUpdate(int numberOfFiles) throws XIDEException{
		numberOfFilesToSave = numberOfFiles;
		
		synchronized(synchSaving) {
			try {
				if (filesToSave.size() != numberOfFilesToSave) {
					// Wait for 20 seconds until all files are saved
					synchSaving.wait(20 * 1000);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			// After sending has finished
			
			// If not files are saved
			if (filesToSave.size() != numberOfFilesToSave) {
				// Iterate through list of backup files
				for (Iterator<String> iterator = filesToSave.iterator(); iterator.hasNext();) {
					String fileName =  iterator.next();
					// Restore old file version
					
					// Delete new file
					File fileNewCopyToDelete = new File(fileName);
					fileNewCopyToDelete.delete();
					
					// Rename backup file to be the main version
					File fileBackupToRestore = new File(fileName + BACKUP_EXT);
					fileBackupToRestore.renameTo(new File(fileName));
					
				}
				clearEverythingForNextSaving();
				throw new XIDEException("One of files was lost during the sending! ");
			}
			
			// All files are saved
		
			if (errorTexts.size() == 0) {
				// Process success
				
				// Iterate through list of backup files
				for (Iterator<String> iterator = filesToSave.iterator(); iterator.hasNext();) {
					String fileName =  iterator.next();
					// delete backups
					File fileBackupToDelete = new File(fileName + BACKUP_EXT);
					fileBackupToDelete.delete();
					
					// Renew values for the next saving procedure
					clearEverythingForNextSaving();

					// Finish without exception							
				}
				
			}
			else {
				// Process fail
				// Iterate through list of backup files
				for (Iterator<String> iterator = filesToSave.iterator(); iterator.hasNext();) {
					String fileName =  iterator.next();
					// Restore old file version
					
					// Delete new file
					File fileNewCopyToDelete = new File(fileName);
					fileNewCopyToDelete.delete();
					
					// Rename backup file to be the main version
					File fileBackupToRestore = new File(fileName + BACKUP_EXT);
					fileBackupToRestore.renameTo(new File(fileName));
					
				}
				// Finish with exception
				// Construct exception text
				String errorTextString = "";
				for (Iterator<String> iteratorErrors = errorTexts.iterator(); iteratorErrors.hasNext();) {
					errorTextString = errorTextString + "\n" +  iteratorErrors.next();
				}

				// Renew values for the next saving procedure
				clearEverythingForNextSaving();
				
				throw new XIDEException(errorTextString);
				
			}

		}
	}
	
	/**
	 * Stores the content of the file. 
	 */
	public void updateFileContent(boolean isSetSaving, boolean doValidate, String absolutePath, String content, int validationType) throws XIDEException {
		// Mark old version of file as .bak
		File fileBackUpOldPath = new File(absolutePath);
		String fileName = fileBackUpOldPath.getName();
		if (isSetSaving) {
			try {
				
				
				File fileBackUp = new File(absolutePath + BACKUP_EXT);
				
				// Rename
				
				if(!fileBackUpOldPath.renameTo(fileBackUp)) {
					throw new IOException("Original file '" + fileName +"' cannot be backuped! ");
				}
				
				// Create new file to the required path
				File fileNew = new File(absolutePath);
				fileNew.createNewFile();
				// Write content to new file
				
				writeStringToFile(absolutePath, content);
				// Validate new file
				if (doValidate) {
					TemplateLanguageValidator tlv = new TemplateLanguageValidator(fileNew, validationType);
				}
				
				filesToSave.add(absolutePath);
				
				checkIfAllFilesAreSaved();
			} catch (IOException e) {
				errorTexts.add("File '" + fileName +"' cannot be saved! " + e.getMessage());
				filesToSave.add(absolutePath);
				checkIfAllFilesAreSaved();
			} catch (ParserConfigurationException e) {
				errorTexts.add("File '" + fileName +"' cannot be validated! " + e.getMessage());
				filesToSave.add(absolutePath);
				checkIfAllFilesAreSaved();
			} catch (SAXException e) {
				errorTexts.add("File '" + fileName +"' cannot be validated! " + e.getMessage());
				filesToSave.add(absolutePath);
				checkIfAllFilesAreSaved();
			}catch (Exception e) {
				errorTexts.add("File '" + fileName +"' cannot be validated! " + e.getMessage());
				filesToSave.add(absolutePath);
				checkIfAllFilesAreSaved();
			}
			
			
		}
		else {
			// just save the file

			try {
//				File file = new File(absolutePath);
//				TemplateLanguageValidator tlv = new TemplateLanguageValidator(file, validationType);
				writeStringToFile(absolutePath, content);

			} catch (IOException e) {
				e.printStackTrace();
				loggerInstance.log(Level.DEBUG, "Update failed" + e.getMessage());
				throw new XIDEException("Error occured while updating file content. " + e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
				loggerInstance.log(Level.DEBUG, "Update failed" + e.getMessage());
				throw new XIDEException("Error occured while updating file content. " + e.getMessage());
			}
		}

	}
	
	private void checkIfAllFilesAreSaved() {
		// if it was the last file to get
		if (filesToSave.size() == numberOfFilesToSave){
			
			synchronized (synchSaving) {
				synchSaving.notify();
			}
			
		}

	}
	/*
	 * Stores the content of the file without validation (default)
	 */
	public void updateFileContent(String absolutePath, String content) throws XIDEException {
		updateFileContent(false, false, absolutePath, content, XIDEFile.NO_VALIDATION);
	}

	/*
	 * Saves the content of the file to the temporary directory used for preview
	 */
	public Boolean updateFileContentForPreview(String absolutePath, String content) throws XIDEException {

		// Get correct path fot preview folder
		String appPath = absolutePath.substring(Config.getAppsSourcePath().length());
		String path = Config.getPreviewSourcePath() + appPath;
		// System.out.println(path);
		try {
			writeStringToFile(path, content);
			return true;
		} catch (IOException e) {
			throw new XIDEException("Error occured while updating file content. " + e.getMessage());
		}

	}

	/**
	 * Verifies the content of the file. Verification mechanism is
	 * defined by validationType parameter, which possible values are defined in
	 * {@link #XIDEFile}
	 * 
	 * If validation is successful, a success event goes to the client. {@link UploadFileContent} method
	 * should be called to save the file to the required path.
	 * 
	 * Otherwise an exception is thrown and object saving is cancelled on a client.
	 */
	public void checkFileContent(String absolutePath, String content, int validationType) throws XIDEException {
		String tempFileName = absolutePath + "1";
		File file = new File(tempFileName);
		try {
			file.createNewFile();
			writeStringToFile(tempFileName, content);
			
			TemplateLanguageValidator tlv = new TemplateLanguageValidator(file, validationType);

		} catch (IOException e) {
			e.printStackTrace();
			loggerInstance.log(Level.DEBUG, "File validation failed" + e.getMessage());
			throw new XIDEException("Error occured while validating file content. " + e.getMessage());
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			loggerInstance.log(Level.DEBUG, "File validation failed" + e.getMessage());
			throw new XIDEException("Error occured while validating file content. " + e.getMessage());
		} catch (SAXException e) {
			e.printStackTrace();
			loggerInstance.log(Level.DEBUG, "File validation failed" + e.getMessage());
			throw new XIDEException("Error occured while validating file content. " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			loggerInstance.log(Level.DEBUG, "Update failed" + e.getMessage());
			throw new XIDEException("Error occured while updating file content. " + e.getMessage());
		}
		finally {
			file.delete();
		}

	}
	/**
	 * Method called from client
	 * 
	 */
	public TemplateShortInfo[] getTemplates(String searchString, String[] tags, int templateType) throws XIDEException {
		ArrayList<TemplateShortInfo> templateList = null;
		// If search string contains something valuable
		if ((searchString != null) && (!searchString.equals(""))) {
			// Make a search
			templateList = getTemplatesByTagsAndString(searchString, tags, templateType);
		} else {
			// Return all templates of such kind
			templateList = getTemplatesByTags(tags, templateType);
		}

		return templateList.toArray(new TemplateShortInfo[templateList.size()]);
	}

	public Template getTemplateDetailedInfo(String templateID) throws XIDEException {
		// LinkedHashMap <String, String> queries = new LinkedHashMap<String,
		// String>();
		// queries.put("Query N1",
		// "Here should be a query text of query N1 '\n next line");
		// queries.put("Query N2",
		// "Here should be a query text of query N2 '\n next line");
		//		
		// String CSS = "color: red";
		//		
		// LinkedHashMap<String, String> dataInstances = new
		// LinkedHashMap<String, String>();
		// dataInstances.put("data-instance-N1",
		// "Here should be a text of data instance N1");
		HashMap<String, Property> parameters = new HashMap<String, Property>();
		HashMap<String, Property> properties = new HashMap<String, Property>();

		properties = getTemplateProperties(templateID);

		// Create file structure
		ArrayList<String> possibleFileNames = new ArrayList<String>();
		// possibleFileNames.add("tpl_login_simple");

		ServerFileStructureParser folderStructureParser = new ServerFileStructureParser(null,
				getTemplateFolderPath(templateID), templateID);
		XIDEFolder rootfolder = folderStructureParser.getRootFolder();
		if (rootfolder != null) {
			Template template = new Template(folderStructureParser.cssFiles, folderStructureParser.dataFiles,
					folderStructureParser.dbFiles, folderStructureParser.queryFiles,
					folderStructureParser.resourceFiles, folderStructureParser.sourceFiles, parameters, properties,
					rootfolder);
			Template template2 = (new PageXMLParser(template)).template;
			return template2;
		} else {
			Template template = null;
			if (properties.size() != 0) {
				// Take real properties
				template = new Template(new LinkedHashMap<String, XIDEFile>(), new LinkedHashMap<String, XIDEFile>(),
						new LinkedHashMap<String, XIDEFile>(), new LinkedHashMap<String, XIDEFile>(),
						new LinkedHashMap<String, XIDEFile>(), new LinkedHashMap<String, XIDEFile>(), parameters,
						properties, null);

			} else {
				template = Template.fakeTemplate(templateID, templateID);
			}

			// new Template(new LinkedHashMap<String, XIDEFile>(),
			// new LinkedHashMap<String, XIDEFile>(),
			// new HashMap<String, Property>(),
			// new HashMap<String, Property>(),
			// new LinkedHashMap<String, XIDEFile>(),
			// new LinkedHashMap<String, XIDEFile>(),
			// new XIDEFolder());
			return template;
		}

	}

	public Boolean saveNewTemplate(Template template) throws XIDEException {

		// Parse tags to string
		// save template
		Property tags = template.getProperties().get(Property.TAGS);
		// if (tags.getValue() instanceof String) {
		// // Needed to be converted to String[]
		// tags.convertStringToArray();
		// }
		template.MakeEditable(false);
		saveTemplate(template.getProperties().get(Property.ID).getStringValue(), template.getProperties().get(
				Property.TITLE).getStringValue(), template.getProperties().get(Property.DESCR).getStringValue(),
				template.getProperties().get(Property.AUTHOR).getStringValue(), template.getProperties().get(
						Property.TYPE).getStringValue(), (ArrayList<String>) template.getProperties()
						.get(Property.TAGS).getValue());
		// if (templateList != null) {
		// Property tags = template.getShortInfo().getTags();
		// if (tags.getValue() instanceof String) {
		// // Needed to be converted to String[]
		// tags.convertStringToArray();
		// }
		// template.MakeEditable(false);
		// templateList.add(template.getShortInfo());
		// return true;
		// }
		updateTemplateFiles(template);
		// String sourceFileURL =
		// getSourceCodeURLForTemplate(template.getProperties().get(Property.ID).getStringValue());
		// try {
		// // TODO: read source code normally
		// writeStringToFile(sourceFileURL,
		// template.getSourceCode().get(template.getSourceCode().keySet().iterator().next())
		// .getContent());
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		return true;
	}

	public HashMap<String, Property> updateTemplateInfo(HashMap<String, Property> elementProperties)
			throws XIDEException {

		Property tags = elementProperties.get(Property.TAGS);
		// if (tags.getValue() instanceof String) {
		// // Needed to be converted to String[]
		// tags.convertStringToArray();
		// }
		Connection conn = null;
		try {
			// Update template
			conn = Config.useConnectionPool() ? connectionPool.getConnection() : initialConnection;
			Statement statement = conn.createStatement();
			String updateTemplateQuery = "UPDATE  templates SET" + " template_title = '"
					+ elementProperties.get(Property.TITLE).getStringValue() + "',"
					+ " creation_date = creation_date, " + " description = '"
					+ elementProperties.get(Property.DESCR).getStringValue() + "'" + " do_work = "
					+ elementProperties.get(Property.DO_WORK).getStringValue() + "," + " where template_id = '"
					+ elementProperties.get(Property.ID).getStringValue() + "';";
			statement.executeUpdate(updateTemplateQuery);
			statement.close();

			// Delete old tags tags
			deleteTagsReferencesForTemplate(elementProperties.get(Property.ID).getStringValue());

			// Save tags
			saveTagsReferences((ArrayList<String>) elementProperties.get(Property.TAGS).getValue(), elementProperties
					.get(Property.ID).getStringValue());

			// Update files
			// updateTemplateFiles(template);

			// String sourceFileURL =
			// getSourceCodeURLForTemplate(template.getProperties().get(Property.ID).getStringValue());
			// try {
			// Array lists for all file types

			// // TODO: read source code normally
			// // writeStringToFile(sourceFileURL, template.getSourceCode()
			// //
			// .get(template.getSourceCode().keySet().iterator().next()).getContent());
			// } catch (IOException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }

			return elementProperties;

		} catch (SQLException e) {
			throw new XIDEException(e.getMessage());
		} finally {
			connectionPool.closeConnection(conn);
		}

	}

	private void updateTemplateFiles(Template template) throws XIDEException {

		LinkedHashMap<String, XIDEFile> cssFiles = template.getCSS();
		LinkedHashMap<String, XIDEFile> dataFiles = template.getDataInstances();
		LinkedHashMap<String, XIDEFile> dbFiles = template.getDB();
		LinkedHashMap<String, XIDEFile> queryFiles = template.getQueries();
		LinkedHashMap<String, XIDEFile> resourceFiles = template.getResources();
		LinkedHashMap<String, XIDEFile> sourceFiles = template.getSourceCode();
		for (Iterator<String> iterator = cssFiles.keySet().iterator(); iterator.hasNext();) {
			String fileName = iterator.next();
			if (cssFiles.get(fileName).hasChanged) {
				updateFileContent(cssFiles.get(fileName).getAbsolutePath(), cssFiles.get(fileName)
						.getFileContentValue());
			}
		}

		for (Iterator<String> iterator = dataFiles.keySet().iterator(); iterator.hasNext();) {
			String fileName = iterator.next();
			if (dataFiles.get(fileName).hasChanged) {
				updateFileContent(dataFiles.get(fileName).getAbsolutePath(), dataFiles.get(fileName)
						.getFileContentValue());
			}
		}
		for (Iterator<String> iterator = dbFiles.keySet().iterator(); iterator.hasNext();) {
			String fileName = iterator.next();
			if (dbFiles.get(fileName).hasChanged) {
				updateFileContent(dbFiles.get(fileName).getAbsolutePath(), dbFiles.get(fileName).getFileContentValue());
			}
		}
		for (Iterator<String> iterator = queryFiles.keySet().iterator(); iterator.hasNext();) {
			String fileName = iterator.next();
			if (queryFiles.get(fileName).hasChanged) {
				updateFileContent(queryFiles.get(fileName).getAbsolutePath(), queryFiles.get(fileName)
						.getFileContentValue());
			}
		}
		for (Iterator<String> iterator = resourceFiles.keySet().iterator(); iterator.hasNext();) {
			String fileName = iterator.next();
			if (resourceFiles.get(fileName).hasChanged) {
				updateFileContent(resourceFiles.get(fileName).getAbsolutePath(), resourceFiles.get(fileName)
						.getFileContentValue());
			}
		}

		for (Iterator<String> iterator = sourceFiles.keySet().iterator(); iterator.hasNext();) {
			String fileName = iterator.next();
			if (sourceFiles.get(fileName).hasChanged) {
				updateFileContent(false, false,  sourceFiles.get(fileName).getAbsolutePath(), sourceFiles.get(fileName)
						.getFileContentValue(), sourceFiles.get(fileName).getValidationType());
			}
		}
	}

	/**
	 * Creates new folder described by parameter
	 */
	public Boolean addNewFolder(XIDEFolder folderToMake) throws XIDEException {

		File parentFolder = new File(folderToMake.getParent().getAbsolutePath());
		if (parentFolder.exists() && parentFolder.isDirectory()) {
			File folder = new File(folderToMake.getAbsolutePath());
			if (folder.exists()) {
				throw new XIDEException("Folder with such name alreary exist!");
			} else {
				folder.mkdir();
				return true;
			}
		}
		throw new XIDEException("Parent folder does not exist!");
	}

	public Boolean deleteTag(String tagTitle) throws XIDEException {

		Connection conn = null;

		try {
			// Delete tag references
			deleteTagsReferencesForTag(tagTitle);

			// Delete tag
			conn = Config.useConnectionPool() ? connectionPool.getConnection() : initialConnection;
			Statement statement = conn.createStatement();
			String deleteTagQuery = "DELETE from tags where title = '" + tagTitle + "';";
			statement.executeUpdate(deleteTagQuery);
			statement.close();

			return true;

		} catch (SQLException e) {
			throw new XIDEException("An errror occur during tag deleting procedure. " + e.getMessage());
		} finally {
			if (conn != null) {
				connectionPool.closeConnection(conn);
			}
		}
	}

	public Integer addTag(String tagTitle, String tagDescription) throws XIDEException {

		Connection conn = null;

		try {
			conn = Config.useConnectionPool() ? connectionPool.getConnection() : initialConnection;
			ResultSet resultTagId;
			Statement statement = conn.createStatement();
			String tagsQuery = "select tag_id from tags" + " where title = '" + tagTitle + "';";
			resultTagId = statement.executeQuery(tagsQuery);
			if (!resultTagId.next()) {
				// There is no tag with such name
				Statement statement2 = conn.createStatement();
				String tagAddQuery = "INSERT INTO tags SELECT count(*), '" + tagTitle + "', '" + tagDescription
						+ "' from tags" + " where title = '" + tagTitle + "';";
				statement2.executeUpdate(tagAddQuery);
				statement2.close();
				statement.close();
				return 1;
			} else {
				statement.close();
				throw new XIDEException("Tag with the given name already exist");
			}
		} catch (SQLException e) {
			throw new XIDEException("An errror occur during tag saving procedure. " + e.getMessage());
		} finally {
			connectionPool.closeConnection(conn);
		}

	}

	/**
	 * Gets all tags from the database
	 */
	public Tag[] getAllTags() {

		Connection conn = null;

		try {
			conn = Config.useConnectionPool() ? connectionPool.getConnection() : initialConnection;
			ArrayList<Tag> tags = new ArrayList<Tag>();

			ResultSet resultTags;
			Statement statement = conn.createStatement();
			String tagsQuery = "select * from tags;";
			resultTags = statement.executeQuery(tagsQuery);
			while (resultTags.next()) {
				Tag tag = new Tag(resultTags.getString("title"), resultTags.getString("description"));
				tags.add(tag);
			}
			statement.close();
			return tags.toArray(new Tag[tags.size()]);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			connectionPool.closeConnection(conn);
		}

	}

	public void checkAppRelURL(String rel_url) throws XIDEException {

		checkApplicationRelURLInWebapps(rel_url);
		checkApplicationRelURLUnicity(rel_url);
		// && ExistManager.canSaveToDb(rel_url);
	}

	public void checkPageRelURL(String rel_url, String appID) throws XIDEException {

		// checkApplicationRelURLInWebapps(rel_url);
		checkPageRelURLUnicity(rel_url, appID);
		// && ExistManager.canSaveToDb(rel_url);
	}

	public void checkTagName(String tagName) throws XIDEException {

		checkTagNameUnicity(tagName);
	}

	public void checkTemplateID(String id) throws XIDEException {

		checkTemplateIDUnicity(id);
	}

	public Boolean saveNewTag(Tag tag) throws XIDEException {

		return saveTag(tag);
	}

	public String deleteFile(String absolutePath) throws XIDEException {

		File file = new File(absolutePath);
		if (file.exists()) {
			// Get file type before deletion. This file type should be send beck
			// to client to delete file from approbriate file list
			String type = ServerFileStructureParser.getFileType(absolutePath);

			// Delete the file
			ServerFileStructureCreator.deleteFile(file);

			return type;
			// if (file.listFiles() != null && file.listFiles().length > 0) {
			// // Delete children previously
			// for (int i = 0; i < file.listFiles().length; i++) {
			// deleteFile(file.listFiles()[i].getAbsolutePath());
			// }
			// }
			// System.out.println("going to delete " + file.getAbsolutePath());
			// file.delete();
		} else {
			throw new XIDEException("Error while deleting the application. File/folder does not exist!");
		}
	}

	/**
	 * Gets list of applications created by user as a tree. Returns header
	 * element of the tree.
	 * 
	 * @throws AuthenticationException
	 */
	public APElement getApplication(String apID, boolean includeOnlyMainInfo) throws AuthenticationException {

		// authenticateUser();

		// Creates list of applications

		return getApplicationByID("", apID, includeOnlyMainInfo);

	}

	public ArrayList<String> getApplicationsList(boolean getPublicApps) throws AuthenticationException {
		authenticateUser();
		return getApplicationsListByUser(getUniqueUserName(), getPublicApps);
	}

	/**
	 * Update info about APElement in the database
	 */
	public HashMap<String, Property> updateAPElementInfo(HashMap<String, Property> elementProperties, int type) {

		if (type == APElement.WPAGE) {
			updatePageInfo(elementProperties);

			// // Update application changed date
			// updateApplicationChangedDate(elementProperties.get(Property.APP_ID).getStringValue());
			APElementPage page = getPageByID(elementProperties.get(Property.ID).getStringValue(), null, true);

			// Update application modified date with the date of page
			// modification
			updateApplicationChangedDate(page.getProperties().get(Property.APP_ID).getStringValue(), page
					.getProperties().get(Property.ID).getStringValue(), false);

			return page.properties;

		} else if (type == APElement.APPLICATION) {
			updateApplicationInfo(elementProperties, false);

			return getApplicationByID(elementProperties.get(Property.ID).getStringValue()).properties;
		}
		return null;
	}

	/**
	 * Updates modified date of the specific element and its parent (if the
	 * element is a page)
	 * 
	 * @param id
	 *            ID of the element to update the date
	 * @param type
	 *            Type of the element (can be {@link #APElement.WPAGE} or
	 *            {@link #APElement.APPLICATION})
	 */
	public HashMap<String, Property> updateAPElementModifiedDate(String id, int type) {

		if (type == APElement.WPAGE) {
			APElementPage page = getPageByID(id, null, true);

			// Update page modification date
			updatePageChangedDate(id);

			// Update application modified date with the date of page
			// modification
			updateApplicationChangedDate(page.getProperties().get(Property.APP_ID).getStringValue(), page
					.getProperties().get(Property.ID).getStringValue(), false);

			APElementApplication application = getApplicationByID("", id, true);
			HashMap<String, Property> result = new HashMap<String, Property>();
			result.put(Property.DATE_MOD, application.getProperties().get(Property.DATE_MOD));

			return result;

		} else if (type == APElement.APPLICATION) {
			updateApplicationChangedDate(id, null, false);

			APElementApplication application = getApplicationByID("", id, true);
			HashMap<String, Property> result = new HashMap<String, Property>();
			result.put(Property.DATE_MOD, application.getProperties().get(Property.DATE_MOD));

			return result;
		}
		return null;
	}

	/**
	 * Publish application on the server Does required transformation and file
	 * copying Change application status
	 */

	public HashMap<String, Property> publishApplication(APElementApplication application, boolean doReloadDB)
			throws XIDEException {

		// TODO: Check if application structure is correct
		if (application.getRootFolder() == null) {
			throw new XIDEException(
					"Application sources cannot be accessed. Probably sources are on the other machine.");
		}
		// Set main page into the web_xml file
		String mainPageID = application.properties.get(Property.MAIN_PAGE).getStringValue();
		String mainPageName = "";
		// If main page is set by the user
		if (mainPageID != null && !mainPageID.equals("")) {
			// Use main page name
			for (Iterator<APElement> iterator = application.getChildren().iterator(); iterator.hasNext();) {
				APElement child = iterator.next();
				if (child.properties.get(Property.ID).getStringValue().equals(mainPageID)) {
					mainPageName = child.properties.get(Property.RELATED_URL).getStringValue();
				}
			}
		} else {
			// Check if there is any page
			if (!application.getChildren().isEmpty()) {
				mainPageName = application.getChildren().iterator().next().properties.get(Property.RELATED_URL)
						.getStringValue();
			}
		}

		// APPLICATION getRootFolder() RETURNS NULL!
		if (mainPageName.equals("")) {
			ServerFileStructureCreator.configureWebXML(application.getRootFolder().getAbsolutePath(), "");
		} else {
			ServerFileStructureCreator.configureWebXML(application.getRootFolder().getAbsolutePath(), mainPageName);
		}

		String user = application.properties.get(Property.AUTHOR).getStringValue();
		String name = application.properties.get(Property.RELATED_URL).getStringValue();
		ArrayList<String> pages = getPageRelatedNames(application);

		try {

			new PublishHandler(user, name, pages, PublishHandler.PUBLISH_TO_PRODUCTION, doReloadDB);

			boolean isReady = false;
			int counter = 0;
			while (!isReady) {

				if (counter > 20) {
					break;
				}
				counter++;

				int httpResponseCode = getResponseCodeFromUrl(Config.getServerURL() + user + "/" + name);
				if (httpResponseCode == HttpURLConnection.HTTP_NOT_FOUND) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else if (httpResponseCode == -1) {
					throw new XIDEException("Unknown error! Probably the Tomcat defined in config is not running.");
				} else {
					isReady = true;
				}
			}

		} catch (PublishException e) {

			if (e.getCode() != PublishException.APPLICATION_ALREADY_IN_TOMCAT) {

				loggerInstance.log(Level.DEBUG, "Publishing failed " + e.getMessage());

				/** Clean the file systems */
				PublishHandler.deleteDir(new File(Config.getTomcatContextFilesFolder() + user + "#" + name + ".xml"));
				PublishHandler.deleteDir(new File(Config.getAppsPublishedPath() + user + "/" + name));
				if (Config.useExistDb()) {
					ExistManager.deleteApplicationCollection(name, user, PublishHandler.PUBLISH_TO_PRODUCTION);
				}
			}

			throw new XIDEException(e.getMessage());
		}

		/**
		 * Everything has gone well if we reach this point (no exceptions
		 * thrown)
		 */

		String url = Config.getServerURL() + user + "/" + name;
		// Change application status
		application.setPublishedState(true);
		application.properties.get(Property.URL).setValue(url);
		updateApplicationInfo(application.getProperties(), true);
		return getApplicationByID(application.properties.get(Property.ID).getStringValue()).properties;

	}

	/**
	 * Unpublish application on the server Removes files from production folder
	 * and from Tomcat Change application status
	 */
	public HashMap<String, Property> unpublishApplication(APElementApplication application, boolean doReloadDB)
			throws XIDEException {

		String user = application.properties.get(Property.AUTHOR).getStringValue();
		String name = application.properties.get(Property.RELATED_URL).getStringValue();

		PublishHandler.deleteDir(new File(Config.getAppsPublishedPath() + user + "/" + name));
		PublishHandler.deleteDir(new File(Config.getTomcatContextFilesFolder() + user + "#" + name + ".xml"));
		if (Config.useExistDb() && doReloadDB) {
			ExistManager.deleteApplicationCollection(name, user, PublishHandler.PUBLISH_TO_PRODUCTION);
		}

		// Change application status
		application.setPublishedState(false);
		// URL property
		application.properties.get(Property.URL).setValue("");
		// Update application information in the database
		updateApplicationInfo(application.getProperties(), false);

		return getApplicationByID(application.properties.get(Property.ID).getStringValue()).properties;
	}

	/**
	 * Republish application on the server. Removes old files from production
	 * folder and from Tomcat Change application status. Then publishes it
	 * again.
	 * 
	 * Boolean flag indicates whether it is necessary to reload the DB.
	 * 
	 * Can be used when application is played or stopped.
	 */
	public HashMap<String, Property> republishApplication(APElementApplication application, boolean doReloadDB)
			throws XIDEException {

		// First unpublish
		unpublishApplication(application, doReloadDB);

		// Then publish
		HashMap<String, Property> result = publishApplication(application, doReloadDB);
		return result;

	}

	/**
	 * Stops application on the server. Leaves all application files in
	 * appspublished. Removes only information about application from Tomcat.
	 */
	public HashMap<String, Property> stopApplication(APElementApplication application) throws XIDEException {
		String user = application.properties.get(Property.AUTHOR).getStringValue();
		String name = application.properties.get(Property.RELATED_URL).getStringValue();

		// Delete the context descriptor from Tomcat
		PublishHandler.deleteDir(new File(Config.getTomcatContextFilesFolder() + user + "#" + name + ".xml"));

		// fake finish

		// Change application status
		application.setPublishedState(false);
		// Update application information in the database
		updateApplicationInfo(application.getProperties(), false);

		return getApplicationByID(application.properties.get(Property.ID).getStringValue()).properties;
	}

	/**
	 * Plays application back on the server. Adds information about application
	 * from Tomcat. Assumes that all application files are already in
	 * appspublished.
	 */
	public HashMap<String, Property> playApplication(APElementApplication application) throws XIDEException {
		String user = application.properties.get(Property.AUTHOR).getStringValue();
		String name = application.properties.get(Property.RELATED_URL).getStringValue();

		File appFolder = new File(Config.getAppsPublishedPath() + user + "/" + name);

		try {

			// Check that we have the application in production
			if (appFolder.exists()) {

				// Add a context descriptor for Tomcat
				String applicationAbsolutePath = Config.getAppsPublishedPath() + user + "/" + name;
				String contextString = "<Context override=\"true\" crossContext=\"true\" docBase=\""
						+ applicationAbsolutePath + "\" />";
				String fileName = user + "#" + name + ".xml";
				String contextFilePath = Config.getTomcatContextFilesFolder() + fileName;
				SearchServiceImpl.writeStringToFile(contextFilePath, contextString);

			} else {

				// Do a standard publish, since the application is not in
				// production
				publishApplication(application, true);
			}

			// Change application status
			application.setPublishedState(true);
			// Update application information in the database
			updateApplicationInfo(application.getProperties(), false);

			return getApplicationByID(application.properties.get(Property.ID).getStringValue()).properties;

		} catch (IOException e) {
			throw new XIDEException("Unable to write context information in Tomcat." + e.getMessage());
		}
	}

	/**
	 * Creates a temporary copy of the page's application source codes. The
	 * files, which are currently modified by user, will be saved into this
	 * temporary copy. A preview of the page will be made based on these
	 * sources.
	 */
	public Boolean onBeforePreview(String appID, String pageID) throws XIDEException {

		// Folder name of the application
		String appName = getApplicationByID(appID).getProperties().get(Property.RELATED_URL).getStringValue();
		// User
		String userName = getApplicationByID(appID).getProperties().get(Property.AUTHOR).getStringValue();

		File src = new File(getAbsoluteAppsSourcesPath(userName + "/" + appName + "/"));

		File dest = new File(Config.getPreviewSourcePath() + userName + "/" + appName + "/");
		if (!dest.exists()) {
			dest.mkdirs();
		}
		try {
			ServerFileStructureCreator.copyFiles(src, dest);
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new XIDEException(e.getMessage());
		}

	}

	/**
	 * Is called when a preview tab is clicked. Publishes required page to a
	 * temporary address specific for each user and gives the address back.
	 */
	public String onStartPreview(String appID, String pageID) throws XIDEException {

		// Folder name of the application
		String appName = getApplicationByID(appID).getProperties().get(Property.RELATED_URL).getStringValue();
		// User
		String userName = getApplicationByID(appID).getProperties().get(Property.AUTHOR).getStringValue();
		// Folder name of the page
		String pageName = getPageByID(pageID, null, true).getProperties().get(Property.RELATED_URL).getStringValue();

		// Check if previous pafe preview files were deleted successfully
		// (onStopPreview was called)

		boolean previewResult = true;
		// Make a preview transformation

		try {

			ArrayList<String> pages = new ArrayList<String>();
			pages.add(pageName);
			new PublishHandler(userName, appName, pages, PublishHandler.PUBLISH_TO_PREVIEW, true);

			boolean isReady = false;
			int counter = 0;
			while (!isReady) {

				if (counter > 20) {
					previewResult = false;
					break;
				}
				counter++;

				int httpResponseCode = getResponseCodeFromUrl(Config.getServerURL() + "preview" + "/" + userName + "/"
						+ appName + "/" + pageName + ".xformsdb");
				if (httpResponseCode == HttpURLConnection.HTTP_NOT_FOUND) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else if (httpResponseCode == -1) {
					throw new XIDEException("Unknown error! Probably the Tomcat defined in config is not running.");
				} else {
					isReady = true;
				}
			}

		} catch (PublishException e) {

			File previewApp = new File(Config.getTempFilesPath() + userName + "/" + appName);
			File contextFile = new File(Config.getTomcatContextFilesFolder() + "preview#" + userName + "#" + appName
					+ ".xml");
			PublishHandler.deleteDir(previewApp);
			PublishHandler.deleteDir(contextFile);

			if (Config.useExistDb()) {
				ExistManager.deleteApplicationCollection(appName, userName, PublishHandler.PUBLISH_TO_PREVIEW);
			}

			previewResult = false;
			throw new XIDEException(e.getMessage());

		}

		if (previewResult) {
			// Returns a URL where a published page can be found

			String url = Config.getServerURL() + "preview" + "/" + userName + "/" + appName + "/" + pageName
					+ ".xformsdb";
			return (url);
		} else {
			throw new XIDEException("Unknown error");
		}
	}

	private int getResponseCodeFromUrl(String urlString) {

		int response = -1;
		URL url;
		try {
			url = new URL(urlString);
			URLConnection connection = url.openConnection();
			if (connection instanceof HttpURLConnection) {
				HttpURLConnection httpConnection = (HttpURLConnection) connection;
				httpConnection.setRequestMethod("GET");
				httpConnection.connect();
				response = httpConnection.getResponseCode();
				loggerInstance.log(Level.DEBUG, "Server response for \"" + urlString + "\" :: " + response);

			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}

	/**
	 * Is called when Preview tab is closed.
	 * 
	 * Removes the page which was published for a preview.
	 */
	public void onStopPreview(String appID, String pageID) throws XIDEException {

	}

	/**
	 * Deletes temporary created application source codes used for preview
	 */
	public Boolean onAfterPreview(String appID, String pageID) throws XIDEException {
		// System.out.println("onAfterPreview()method called");
		// Folder name of the application
		String appName = getApplicationByID(appID).getProperties().get(Property.RELATED_URL).getStringValue();
		// User
		String userName = getApplicationByID(appID).getProperties().get(Property.AUTHOR).getStringValue();

		File dest = new File(Config.getPreviewSourcePath() + userName + "/");
		ServerFileStructureCreator.deleteFile(dest);

		File previewApp = new File(Config.getPreviewPublishedPath() + userName + "/" + appName);
		File contextFile = new File(Config.getTomcatContextFilesFolder() + "preview#" + userName + "#" + appName
				+ ".xml");
		PublishHandler.deleteDir(previewApp);
		PublishHandler.deleteDir(contextFile);
		if (Config.useExistDb()) {
			ExistManager.deleteApplicationCollection(appName, userName, PublishHandler.PUBLISH_TO_PREVIEW);
		}

		return true;

	}

	/**
	 * Is called when the the page is saved as new component
	 * 
	 * Makes required transformation, stores new files in the file system, add
	 * corresponding information to the template database
	 */
	public Template savePageAsComponent(String id, String title, String descr, String tags, String appID, String pageID)
			throws XIDEException {

		// Folder name of the application
		String appName = getApplicationByID(appID).getProperties().get(Property.RELATED_URL).getStringValue();
		// User
		String userName = getApplicationByID(appID).getProperties().get(Property.AUTHOR).getStringValue();
		// Folder name of the page
		String pageName = getPageByID(pageID, null, true).getProperties().get(Property.RELATED_URL).getStringValue();
		// Name (ID) of the new component
		String newComponentName = id;

		// System.out.println("app  = " + appName + " user = " + userName +
		// " page = " + pageName + " new id = "
		// + newComponentName + " new name = " + title + " new descr = " + descr
		// + " new tags = " + tags);
		// Save as new copmonent
		if (savePageAsComponent(userName, appName, pageName, newComponentName) == true) {
			// System.out.println("trying to save to sql");
			// If succeeded then change the database
			saveTemplate(id, title, descr, userName, "3", ArrayProperty.convertStringToArray(tags));
		}
		return null;
	}

	public boolean savePageAsComponent(String userName, String applicationName, String pageName, String componentName) {

		boolean result = true;

		try {

			new PageToComponentHandler(userName, applicationName, pageName, componentName);

		} catch (PublishException e) {

			result = false;

			PageToComponentHandler.deleteComponentFromFileSystem(componentName);
			SearchServiceImpl.deleteComponentFromDatabase(componentName);

		}
		return result;
	}

	public void deleteApplication(String applicationID) throws XIDEException {

		Connection conn = null;

		try {
			conn = Config.useConnectionPool() ? connectionPool.getConnection() : initialConnection;

			Statement statementAdvanced = conn.createStatement();
			String getAppInfo = "select * from applications where application_id = " + "'" + applicationID + "';";
			ResultSet resultIDs = statementAdvanced.executeQuery(getAppInfo);
			String rel_url = null;
			String user = null;
			if (resultIDs.next()) {
				// Template source code already exists
				rel_url = resultIDs.getString("rel_url");
				user = resultIDs.getString("author");
			}

			statementAdvanced.close();

			ServerFileStructureCreator.deleteApplicationStructure(rel_url, user);

			// Delete pages
			statementAdvanced = conn.createStatement();
			String getPageIDs = "select page_id from pages where application_id = " + "'" + applicationID + "';";
			resultIDs = statementAdvanced.executeQuery(getPageIDs);
			if (resultIDs.next()) {
				// Template source code already exists
				String pageID = resultIDs.getString("page_id");
				deletePage(pageID);
			}

			statementAdvanced.close();

			// Delete application
			Statement statement = conn.createStatement();
			String deleteApplications = "delete from applications where application_id = " + "'" + applicationID + "';";
			statement.executeUpdate(deleteApplications);
			statement.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new XIDEException("Application with ID = " + applicationID
					+ " was not deleted due to server database error!");
		} finally {
			connectionPool.closeConnection(conn);
		}
	}

	/**
	 * @return Properties of the application (including modified date and main
	 *         page value)
	 */
	public HashMap<String, Property> deletePage(String pageID) throws XIDEException {

		Connection conn = null;

		try {
			conn = Config.useConnectionPool() ? connectionPool.getConnection() : initialConnection;
			// Get applicationID
			APElementPage page = getPageByID(pageID, null, true);
			String appID = page.getProperties().get(Property.APP_ID).getStringValue();

			// Update template
			Statement statement = conn.createStatement();
			String deleteQueries = "delete from pages where page_id = " + "'" + pageID + "';";
			statement.executeUpdate(deleteQueries);
			statement.close();

			// Check if this page was a main one
			statement = conn.createStatement();
			// Updates application where this page was set as a main one
			// If the application has other pages, then first of them will be
			// selected as a main one
			// Otherwise there will be no main page
			String checkMainPage = "update applications set  main_page = (select page_id from pages where application_id = "
					+ appID
					+ " limit 0, 1) and creation_date = creation_date where main_page ="
					+ pageID
					+ " and application_id =" + appID + ";";
			// String checkMainPage =
			// "update applications set main_page = null where main_page = " +
			// pageID + ";";
			statement.executeUpdate(checkMainPage);
			statement.close();
			updateApplicationInfo(getApplicationByID(appID).getProperties(), false);

			// Delete page files
			// TODO: sync with database update
			deletePageFiles(page);
			return getApplicationByID(appID).getProperties();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new XIDEException("Page with ID = " + pageID + " was not deleted due to server database error! "
					+ e.getMessage());
		} finally {
			connectionPool.closeConnection(conn);
		}
	}

	public void deletePageFiles(APElementPage page) throws XIDEException{
		ArrayList<File> files = new ServerFileStructureParserForDelete(
				getApplicationFolderPath(page.properties.get(Property.APP_ID).getStringValue()),
				page.getProperties().get(Property.RELATED_URL).getStringValue()).getFilesToDelete();
		
		for (Iterator<File> iterator = files.iterator(); iterator.hasNext();) {
			File file =  iterator.next();
			ServerFileStructureCreator.deleteFile(file);
		}
		
	}
	
	
	public APElementApplication createApplication(String appName, String relPath, String appDescription,
			boolean isPublic) throws XIDEException, AuthenticationException {
		authenticateUser();
		String userName = getUniqueUserName();
		String path = userName + "/" + relPath + "/";

		ServerFileStructureCreator.createApplicationStructure(relPath, userName);
		String appID = addApplication(appName, relPath, appDescription, isPublic, userName, path);

		// APElementApplication application = getApplicationsByUser(headElement,
		// user);
		return getApplicationByID(appID);
	}

	private String addApplication(String appName, String relPath, String appDescription, boolean isPublic,
			String userName, String path) throws XIDEException {

		Connection conn = null;

		try {
			conn = Config.useConnectionPool() ? connectionPool.getConnection() : initialConnection;
			// Update template
			Statement statement = conn.createStatement();

			String updateTemplateQuery = "INSERT INTO applications (application_title, description, author, url, rel_url,"
					+ "creation_date, modification_date, publishing_date, is_public, status, folder_path) VALUES ("
					+ "'"
					+ appName
					+ "',"
					+ "'"
					+ appDescription
					+ "',"
					+ "'"
					+ userName
					+ "',"
					+ "'',"
					+ "'"
					+ relPath
					+ "'," + "NOW()," + 0 + "," + 0 + "," + isPublic + "," + false + "," + "'" + path + "');";

			// SELECT LAST_INSERT_ID();
			statement.executeUpdate(updateTemplateQuery);
			statement.close();

			statement = conn.createStatement();
			String getID = "SELECT LAST_INSERT_ID() id;";
			ResultSet resultID = statement.executeQuery(getID);
			String id = null;
			while (resultID.next()) {
				id = resultID.getString("id");
			}
			return id;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			connectionPool.closeConnection(conn);
		}
	}

	public APElementPage createPage(APElementApplication application, String relPath, String pageName, boolean addXML,
			boolean addDefaultContainer, boolean markAsMain, String pageDescription) throws XIDEException {
		// TODO: change the statement! it should be linked to the application
		String path = Config.getAppsSourcePath()
				+ application.properties.get(Property.SERVER_FOLDER_PATH).getStringValue();

		ServerFileStructureCreator.createPageStructure(relPath, path, addXML, addDefaultContainer);
		String pageID = addPage(pageName, relPath, addXML, pageDescription, application.properties.get(Property.ID)
				.getStringValue());
		if (markAsMain) {
			// Update application page list
			application.properties.put(Property.MAIN_PAGE, getPageList(application.getProperties().get(Property.ID)
					.getStringValue()));
			application.properties.get(Property.MAIN_PAGE).setValue(pageID);
			updateApplicationInfo(application.getProperties(), false);
		}
		APElementPage page = getPageByID(pageID, null, false);

		// Update application modified date with the date of new page creation
		updateApplicationChangedDate(application.getProperties().get(Property.ID).getStringValue(), page
				.getProperties().get(Property.ID).getStringValue(), true);

		return page;
	}

	private String addPage(String pageName, String relPath, boolean addXML, String pageDescription, String appID)
			throws XIDEException {

		Connection conn = null;

		try {
			conn = Config.useConnectionPool() ? connectionPool.getConnection() : initialConnection;
			// Update template
			Statement statement = conn.createStatement();

			String updateTemplateQuery = "INSERT INTO pages (page_title, description, creation_date, modification_date, rel_url, "
					+ "application_id) VALUES("
					+ "'"
					+ pageName
					+ "',"
					+ "'"
					+ pageDescription
					+ "',"
					+ "NOW(),"
					+ 0
					+ "," + "'" + relPath + "'," + "'" + appID + "');";

			// SELECT LAST_INSERT_ID();
			statement.executeUpdate(updateTemplateQuery);
			statement.close();

			statement = conn.createStatement();
			String getID = "SELECT LAST_INSERT_ID() id;";
			ResultSet resultID = statement.executeQuery(getID);
			String id = null;
			while (resultID.next()) {
				id = resultID.getString("id");
			}
			return id;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			connectionPool.closeConnection(conn);
		}
	}

	public Template getWebPage(APElementPage page)  throws XIDEException{

		Template template = convertAPElToTemplate(page);
		if (template != null) {
			return (new PageXMLParser(template)).template;
		} else {
			throw new XIDEException("Information about this page was not found!");
		}
	}

	public static ArrayList<String> getPageRelatedNames(APElementApplication application) {

		ArrayList<String> result = new ArrayList<String>();
		for (Iterator<APElement> iterator = application.getChildren().iterator(); iterator.hasNext();) {
			APElement element = iterator.next();
			result.add(element.properties.get(Property.RELATED_URL).getStringValue());
		}
		return result;
	}

	private Template convertAPElToTemplate(APElementPage page) {

		// Create file structure
		ArrayList<String> possibleFileNames = new ArrayList<String>();
		possibleFileNames.add(page.properties.get(Property.RELATED_URL).getStringValue());
		// possibleFileNames.add("tpl_login_simple");
		ServerFileStructureParser folderStructureParser = new ServerFileStructureParser(possibleFileNames,
				getApplicationFolderPath(page.properties.get(Property.APP_ID).getStringValue()),
				// page.properties.get(Property.ID).getStringValue()
				page.properties.get(Property.RELATED_URL).getStringValue());
		XIDEFolder rootfolder = folderStructureParser.getRootFolder();
		if (rootfolder != null) {
			Template template = new Template(folderStructureParser.cssFiles, folderStructureParser.dataFiles,
					folderStructureParser.dbFiles, folderStructureParser.queryFiles,
					folderStructureParser.resourceFiles, folderStructureParser.sourceFiles, page.getParameters(), page
							.getProperties(), rootfolder);
			return template;
		} else {
			return null;
		}
	}

	/**
	 * Gets absolute path of the application folder on a server
	 * 
	 * @param application_id
	 * @return
	 */
	private String getApplicationFolderPath(String application_id) {

		ResultSet resultSourceCode;
		Connection conn = null;

		try {
			conn = Config.useConnectionPool() ? connectionPool.getConnection() : initialConnection;
			Statement statement = conn.createStatement();
			// TODO: add type of the file
			String folderPathQuery = "select applications.folder_path from applications"
					+ " where applications.application_id= '" + application_id + "';";

			resultSourceCode = statement.executeQuery(folderPathQuery);
			String result = null;
			if (resultSourceCode.next()) {
				// Template source code already exists
				String relatedPath = resultSourceCode.getString("folder_path");
				// TODO: remove hardcoded stuff
				result = getAbsoluteAppsSourcesPath(relatedPath);
				return result;
			} else {
				// System.out.println("Server: No application with the id " +
				// application_id + " has found");
				return null;
			}

		} catch (SQLException e) {
			return null;
		} finally {
			connectionPool.closeConnection(conn);
		}
	}

	/**
	 * Gets absolute path of the template folder on a server
	 * 
	 * @param template_id
	 * @return
	 */
	private String getTemplateFolderPath(String template_id) {

		ResultSet resultSourceCode;
		Connection conn = null;

		try {
			conn = Config.useConnectionPool() ? connectionPool.getConnection() : initialConnection;
			Statement statement = conn.createStatement();
			// TODO: add type of the file
			String folderPathQuery = "select templates.folder_path from templates" + " where templates.template_id= '"
					+ template_id + "';";

			resultSourceCode = statement.executeQuery(folderPathQuery);
			String result = null;
			if (resultSourceCode.next()) {
				// Template source code already exists
				String relatedPath = resultSourceCode.getString("folder_path");
				result = getAbsoluteComponentsPath(relatedPath);
				return result;
			} else {
				// System.out.println("Server: No template with the title " +
				// template_id + " has found");
				return null;
			}

		} catch (SQLException e) {
			return null;
		} finally {
			connectionPool.closeConnection(conn);
		}
	}

	/**
	 * Updates information about Page element in the database
	 * 
	 * @param page
	 */
	private void updatePageInfo(HashMap<String, Property> pageProperties) {

		Connection conn = null;

		try {
			// Update template
			conn = Config.useConnectionPool() ? connectionPool.getConnection() : initialConnection;
			Statement statement = conn.createStatement();

			String updateTemplateQuery = "UPDATE pages SET" + " page_title = '"
					+ pageProperties.get(Property.TITLE).getStringValue() + "'," + " description = '"
					+ pageProperties.get(Property.DESCR).getStringValue() + "'," + " rel_url = '"
					+ pageProperties.get(Property.RELATED_URL).getStringValue() + "',"
					+ " creation_date = creation_date, " + " modification_date = NOW()" + " where page_id = '"
					+ pageProperties.get(Property.ID).getStringValue() + "';";
			statement.executeUpdate(updateTemplateQuery);
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connectionPool.closeConnection(conn);
		}

	}

	/**
	 * Updates the application modified date to the given one.
	 * 
	 * This method is used when the application modified date should be updated
	 * because the page has updated
	 * 
	 * @param application_id
	 *            id of the application
	 * @param pageID
	 *            id of the page to copy modification date from it (problem with
	 *            data transferring). If null then NOW() is set as modification
	 *            date
	 * @param useCreationDate 
	 * 			  flag which indicates that the date for application should be taken not from modification_date column, but from creation_date column
	 * 			  Id used when the application should be updated because of new page creation (page does not tave modification_date)
	 */
	private void updateApplicationChangedDate(String application_id, String pageID, boolean useCreationDate) {
		Connection conn = null;

		try {
			// Update template
			conn = Config.useConnectionPool() ? connectionPool.getConnection() : initialConnection;
			Statement statement = conn.createStatement();
			String updateTemplateQuery = null;
			updateTemplateQuery = "UPDATE applications SET";
			if (pageID != null) {
				updateTemplateQuery = updateTemplateQuery
						+ " modification_date =  (SELECT ";
				
				// Select modification or creation date to be used
				if (!useCreationDate) {
					updateTemplateQuery += "modification_date"; 
				}
				else {
					updateTemplateQuery += "creation_date";
				}
				
				updateTemplateQuery += " FROM pages where page_id = " + pageID + ")";
			} else {
				// update with current date
				updateTemplateQuery = updateTemplateQuery + " modification_date =  NOW()";
			}

			updateTemplateQuery = updateTemplateQuery + " where application_id = '" + application_id + "';";
			statement.executeUpdate(updateTemplateQuery);
			statement.close();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connectionPool.closeConnection(conn);
		}
	}

	
	/**
	 * Updates the page modified date to the given one.
	 * 
	 * This method is used when the page modified date should be updated because
	 * the page has updated without any changes to the properties (e.g. file
	 * management)
	 * 
	 * @param page_id
	 *            id of the application
	 * @param date
	 *            date of modification to set
	 */
	private void updatePageChangedDate(String page_id) {
		Connection conn = null;

		try {
			// Update template
			conn = Config.useConnectionPool() ? connectionPool.getConnection() : initialConnection;
			Statement statement = conn.createStatement();
			String updateTemplateQuery = null;
			updateTemplateQuery = "UPDATE pages SET";

			// update with current date
			updateTemplateQuery = updateTemplateQuery + " modification_date =  NOW()";

			updateTemplateQuery = updateTemplateQuery + " and creation_date = creation_date where page_id = '"
					+ page_id + "';";

			statement.executeUpdate(updateTemplateQuery);
			statement.close();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connectionPool.closeConnection(conn);
		}
	}

	/**
	 * Updates information about Application in the database
	 * 
	 * @param application
	 */
	private void updateApplicationInfo(HashMap<String, Property> applicationProperties, boolean changePublished) {

		Connection conn = null;

		try {
			// Update template
			conn = Config.useConnectionPool() ? connectionPool.getConnection() : initialConnection;
			Statement statement = conn.createStatement();
			String updateTemplateQuery = null;
			updateTemplateQuery = "UPDATE applications SET" + " application_title = '"
					+ applicationProperties.get(Property.TITLE).getStringValue() + "'," + " description = '"
					+ applicationProperties.get(Property.DESCR).getStringValue() + "'," + " is_public = "
					+ applicationProperties.get(Property.IS_SHOWN_PUBLIC).getStringValue() + "," + " status = "
					+ applicationProperties.get(Property.IS_PUBLISHED).getStringValue() + "," + " rel_url = '"
					+ applicationProperties.get(Property.RELATED_URL).getStringValue() + "'," + " url = '"
					+ applicationProperties.get(Property.URL).getStringValue() + "'," + " main_page = '"
					+ applicationProperties.get(Property.MAIN_PAGE).getStringValue() + "',"
					+ " creation_date = creation_date, " + " modification_date =  NOW(), ";

			// Manage published date
			if (changePublished) {
				// Published just before
				updateTemplateQuery = updateTemplateQuery + " publishing_date = NOW()";
			} else {
				if (applicationProperties.get(Property.DATE_PUB).getStringValue().equals("")) {
					// Not published
					updateTemplateQuery = updateTemplateQuery + " publishing_date = 0";
				} else {
					// Published earlier: do not touch the date
					updateTemplateQuery = updateTemplateQuery + " publishing_date = creation_date";
				}
			}
			updateTemplateQuery = updateTemplateQuery + " where application_id = '"
					+ applicationProperties.get(Property.ID).getStringValue() + "';";

			statement.executeUpdate(updateTemplateQuery);
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connectionPool.closeConnection(conn);
		}
	}

	/**
	 * Gets source URL for template. If source file exists then returns it's
	 * URL, otherwise generate new file name for the template source and returns
	 * it
	 * 
	 * @param template_id
	 * @return
	 */
	private String getSourceCodeURLForTemplate(String template_id) {

		ResultSet resultSourceCode;
		Statement statement2;
		Connection conn = null;

		try {
			conn = Config.useConnectionPool() ? connectionPool.getConnection() : initialConnection;
			statement2 = conn.createStatement();
			// TODO: add type of the file
			String filesQuery = "select templates_files.source from templates_files"
					+ " where templates_files.template_id = '" + template_id + "';";

			resultSourceCode = statement2.executeQuery(filesQuery);
			String result = null;
			if (resultSourceCode.next()) {
				// Template source code already exists
				String relatedPath = resultSourceCode.getString("source");
				result = getAbsoluteAppsSourcesPath(relatedPath);
			} else {

				// No template code entry
				// Get the number of tags in the table
				ResultSet resultFilesNumber;
				Statement statement = conn.createStatement();

				String st = "SELECT count(*)  as c from templates_files;";
				resultFilesNumber = statement.executeQuery(st);
				resultFilesNumber.next();

				int numberOfFiles = resultFilesNumber.getInt("c");
				statement.close();

				statement = conn.createStatement();
				String relatedPath = "sourcecodes/" + template_id + ".xml";

				String st2 = "INSERT INTO templates_files VALUES (" + " '" + numberOfFiles + "'," + " '" + template_id
						+ "'," + " '" + relatedPath + "');";
				statement.executeUpdate(st2);
				statement.close();

				result = getAbsoluteAppsSourcesPath(relatedPath);
			}
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally {
			connectionPool.closeConnection(conn);
		}

	}

	/**
	 * Get list of tags for the template
	 * 
	 * @param template_id
	 * @return List of tags
	 * @throws SQLException
	 */
	private String[] getTagsForTemplate(String template_id) throws SQLException {

		ResultSet resultTags;
		Connection conn = Config.useConnectionPool() ? connectionPool.getConnection() : initialConnection;
		Statement statement2 = conn.createStatement();
		String tagsQuery = "select tags.title from tags, used_tags_map"
				+ " where tags.tag_id = used_tags_map.tag_id and used_tags_map.template_id = '" + template_id + "';";
		resultTags = statement2.executeQuery(tagsQuery);
		ArrayList<String> tagsList = new ArrayList<String>();
		while (resultTags.next()) {
			tagsList.add(resultTags.getString("title"));
		}

		connectionPool.closeConnection(conn);
		return tagsList.toArray(new String[tagsList.size()]);
	}

	/**
	 * Get list of templates which have required tags
	 * 
	 * @param searchTags
	 *            required tags
	 * @param templateType
	 *            type of the template (Component, Page, Application)
	 * @return
	 */
	private ArrayList<TemplateShortInfo> getTemplatesByTags(String[] searchTags, int templateType) throws XIDEException {

		ArrayList<TemplateShortInfo> result = new ArrayList<TemplateShortInfo>();
		Connection conn = null;

		try {
			conn = Config.useConnectionPool() ? connectionPool.getConnection() : initialConnection;
			ResultSet resultTemplates;
			Statement statement = conn.createStatement();
			// There are some tags for searching
			if (searchTags.length > 0) {
				// Main search query
				String st = "SELECT templates.* FROM ("
						+ "SELECT used_tags_map.template_id AS template_id, count(*) AS c FROM";
				st = st + "(SELECT tag_id from tags where title in (";
				for (int i = 0; i < searchTags.length; i++) {
					st = st + "'" + searchTags[i] + "'";
					if (i < searchTags.length - 1) {
						st = st + ", ";
					}
				}
				st = st + ") )";

				st = st + " as tags, used_tags_map" + " WHERE tags.tag_id = used_tags_map.tag_id"
						+ " group by used_tags_map.template_id) as template_ids, templates" + " where c = "
						+ searchTags.length
						+ " and template_ids.template_id = templates.template_id and templates.template_type ="
						+ +templateType;

				resultTemplates = statement.executeQuery(st);
			}
			// No tags so return everything
			else {
				String st = "SELECT templates.* FROM templates where templates.template_type =" + +templateType
						+ " limit 0, 15";
				resultTemplates = statement.executeQuery(st);
			}

			result = processResultTemplateSearch(resultTemplates);

			resultTemplates.close();
			statement.close();

		} catch (SQLException e) {
			throw new XIDEException(e.getMessage());
		} catch (IOException e) {
			throw new XIDEException(e.getMessage());
		} finally {
			connectionPool.closeConnection(conn);
		}
		return result;
	}

	private ArrayList<TemplateShortInfo> processResultTemplateSearch(ResultSet resultTemplates) throws SQLException,
			IOException {
		ArrayList<TemplateShortInfo> result = new ArrayList<TemplateShortInfo>();
		while (resultTemplates.next()) {
			TemplateShortInfo t = null;

			// Create new ShortInfo object
			Property title = new BaseProperty(TemplateShortInfo.TITLE, resultTemplates.getString("template_title"),
					false);
			Property id = new BaseProperty(TemplateShortInfo.ID, resultTemplates.getString("template_id"), false);
			Property descr = new BaseProperty(TemplateShortInfo.DESCR, resultTemplates.getString("description"), false);
			// Get tags
			Property tags = new ArrayProperty(TemplateShortInfo.TAGS, getTagsForTemplate(resultTemplates
					.getString("template_id")), false);

			// t = new TemplateShortInfo(id, title, descr, tags);
			// System.out.println("getting2 : " +
			// resultTemplates.getString("template_id") + "/"
			// + resultTemplates.getString("template_id") + ".xml");
			File file = new File(getAbsoluteComponentsPath(resultTemplates.getString("template_id") + "/"
					+ resultTemplates.getString("template_id") + ".xml"));
			if (file.exists()) {
				BooleanProperty doWork = new BooleanProperty(TemplateShortInfo.DO_WORK, false, false,
						"Does it really work", resultTemplates.getBoolean("do_work"));
				t = new TemplateShortInfo(id, title, descr, tags, doWork, readFileAsString(file.getAbsolutePath()));
			} else {
				BooleanProperty doWork = new BooleanProperty(TemplateShortInfo.DO_WORK, false, false,
						"Does it really work", false);
				t = new TemplateShortInfo(id, title, descr, tags, doWork, "");
			}
			result.add(t);
		}

		return result;
	}

	/**
	 * Get list of templates which have required tags and contain required
	 * string
	 * 
	 * @param keyString
	 *            required string
	 * @param searchTags
	 *            required tags
	 * @param templateType
	 *            type of the template (Component, Page, Application)
	 * @return
	 */
	private ArrayList<TemplateShortInfo> getTemplatesByTagsAndString(String keyString, String[] searchTags,
			int templateType) throws XIDEException {

		ArrayList<TemplateShortInfo> result = new ArrayList<TemplateShortInfo>();
		Connection conn = null;

		try {
			conn = Config.useConnectionPool() ? connectionPool.getConnection() : initialConnection;

			ResultSet resultTemplates;
			Statement statement = conn.createStatement();
			// There are some tags for searching
			if (searchTags.length > 0) {
				// Main search query

				String st = "SELECT templates.* FROM ("
						+ "SELECT used_tags_map.template_id AS template_id, count(*) AS c FROM"
						+ " (SELECT tag_id from tags where title in ( ";
				for (int i = 0; i < searchTags.length; i++) {
					st = st + "'" + searchTags[i] + "'";
					if (i < searchTags.length - 1) {
						st = st + ", ";
					}
				}
				st = st + ") )";
				st = st + " as tags, used_tags_map" + " WHERE tags.tag_id = used_tags_map.tag_id"
						+ " group by used_tags_map.template_id) as template_ids, templates" + " where c = "
						+ searchTags.length
						+ " and template_ids.template_id = templates.template_id and templates.template_type ="
						+ +templateType;
				st = st + " and ( templates.template_title LIKE '%" + keyString + "%' or description LIKE '%"
						+ keyString + "%')";
				// st = st + " and ( templates.template_title ~* '" + keyString
				// + "' or description ~* '" + keyString + "')";
				resultTemplates = statement.executeQuery(st);
			} else {
				String st = "SELECT templates.* FROM templates where templates.template_type =" + +templateType;
				st = st + " and ( template_title LIKE '%" + keyString + "%' or description LIKE '%" + keyString + "%')";
				// st = st + " and ( template_title ~* '" + keyString +
				// "' or description ~* '" + keyString + "')";
				resultTemplates = statement.executeQuery(st);
			}

			result = processResultTemplateSearch(resultTemplates);

			resultTemplates.close();
			statement.close();

		} catch (SQLException e) {
			throw new XIDEException(e.getMessage());
		} catch (IOException e) {
			throw new XIDEException(e.getMessage());
		} finally {
			connectionPool.closeConnection(conn);
		}
		return result;
	}

	/**
	 * Based on meta data (of the query's result set) constructs list of
	 * properties with appropriate titles, descriptions, etc. received from
	 * Properties_info table If there is no property with required name,
	 * constructs empty property
	 * 
	 * @param metadata
	 * @param tableName
	 * @return
	 */
	private Property[] getPropertiesInfo(ResultSetMetaData metadata, String tableName) {

		Connection conn = null;

		try {
			Property[] result = new Property[metadata.getColumnCount()];

			ResultSet resultPropInfo;
			conn = Config.useConnectionPool() ? connectionPool.getConnection() : initialConnection;
			Statement statement = conn.createStatement();

			String st = "SELECT * FROM properties_info where table_name = '" + tableName + "';";
			resultPropInfo = statement.executeQuery(st);

			HashMap<String, Property> props = new HashMap<String, Property>();
			while (resultPropInfo.next()) {
				int propertyDataType = resultPropInfo.getInt("data_type");
				Property p = null;
				switch (propertyDataType) {
				// Data types are:
				// 0 - text
				// 1 - boolean
				// 2 - textarea
				// 3 - system (like template_type: shouldn't be shown to a user)
				// 4 - array (not really necessary in the table)
				// 5 - date
				// Is not used now
				// case 4:
				// break;
				case 2:
					p = new BaseProperty(resultPropInfo.getString("prop_title"),
							resultPropInfo.getBoolean("is_hidden"), resultPropInfo.getBoolean("is_never_editable"),
							resultPropInfo.getString("description"));
					break;
				case 1:
					p = new BooleanProperty(resultPropInfo.getString("prop_title"), resultPropInfo
							.getBoolean("is_hidden"), resultPropInfo.getBoolean("is_never_editable"), resultPropInfo
							.getString("description"));
					break;
				case 5:
					p = new DateProperty(resultPropInfo.getString("prop_title"),
							resultPropInfo.getBoolean("is_hidden"), resultPropInfo.getBoolean("is_never_editable"),
							resultPropInfo.getString("description"));
					break;
				default: // Here goes 3, 4, 0 and non-defined ones: text
					// property is a default one
					p = new BaseProperty(resultPropInfo.getString("prop_title"),
							resultPropInfo.getBoolean("is_hidden"), resultPropInfo.getBoolean("is_never_editable"),
							resultPropInfo.getString("description"));
				}
				props.put(resultPropInfo.getString("column_name"), p);
				// for(int i = 1; i <= metadata.getColumnCount(); i++) {
				//					
				// }
			}
			for (int i = 1; i <= metadata.getColumnCount(); i++) {
				if (props.get(metadata.getColumnName(i)) != null) {
					result[i - 1] = props.get(metadata.getColumnName(i));
				} else {
					result[i - 1] = new BaseProperty("no title", false, false, "no descr");
				}
			}
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			connectionPool.closeConnection(conn);
		}
	}

	/**
	 * Based on meta data (of the query's result set) constructs list of
	 * property's codes If there is no property with required name, adds
	 * "NO_CODE" as a code
	 * 
	 * @param metadata
	 * @param tableName
	 * @return
	 */
	private String[] getPropertiesCodes(ResultSetMetaData metadata, String tableName) {

		Connection conn = null;

		try {
			String[] result = new String[metadata.getColumnCount()];

			ResultSet resultPropInfo;
			conn = Config.useConnectionPool() ? connectionPool.getConnection() : initialConnection;
			Statement statement = conn.createStatement();

			String st = "SELECT * FROM properties_info where table_name = '" + tableName + "';";
			resultPropInfo = statement.executeQuery(st);

			// Iterate on application list
			HashMap<String, String> props = new HashMap<String, String>();
			while (resultPropInfo.next()) {

				Property p = new BaseProperty(resultPropInfo.getString("prop_title"), resultPropInfo
						.getString("description"), false);
				props.put(resultPropInfo.getString("column_name"), resultPropInfo.getString("prop_code"));
				// for(int i = 1; i <= metadata.getColumnCount(); i++) {
				//					
				// }
			}
			for (int i = 1; i <= metadata.getColumnCount(); i++) {
				if (props.get(metadata.getColumnName(i)) != null) {
					result[i - 1] = props.get(metadata.getColumnName(i));
				} else {
					result[i - 1] = "NO_CODE";
				}
			}
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			connectionPool.closeConnection(conn);
		}
	}

	/**
	 * Gets template properties as a hash map
	 * 
	 * @param templateID
	 * @return
	 */
	private HashMap<String, Property> getTemplateProperties(String templateID) throws XIDEException {

		Connection conn = null;

		try {

			ResultSet resultTemplate;
			conn = Config.useConnectionPool() ? connectionPool.getConnection() : initialConnection;
			Statement statement = conn.createStatement();
			// Main search query
			String st = "SELECT * FROM templates where template_id = '" + templateID + "';";
			resultTemplate = statement.executeQuery(st);

			HashMap<String, Property> properties = new HashMap<String, Property>();

			Property[] propertySources = getPropertiesInfo(resultTemplate.getMetaData(), "templates");
			String[] propertyCodes = getPropertiesCodes(resultTemplate.getMetaData(), "templates");

			while (resultTemplate.next()) {
				for (int i = 0; i < resultTemplate.getMetaData().getColumnCount(); i++) {
					Property p = propertySources[i].clone();
					if (resultTemplate.getObject(i + 1) != null) {
						if (p instanceof DateProperty) {
							((DateProperty) p).setValue(resultTemplate.getTimestamp(i + 1).getTime(), resultTemplate
									.getObject(i + 1).toString());
						} else {
							p.setValue(resultTemplate.getObject(i + 1).toString());
						}
					} else {
						if (p instanceof DateProperty) {
							((DateProperty) p).setValue(0, "");
						} else {
							p.setValue("");
						}
					}
					properties.put(propertyCodes[i], p);
				}
			}
			resultTemplate.close();
			statement.close();

			// Get tags
			Property tags = new ArrayProperty(TemplateShortInfo.TAGS, getTagsForTemplate(templateID), false);
			properties.put(Property.TAGS, tags);

			return properties;

		} catch (SQLException e) {
			throw new XIDEException("An error occured when getting template information from the database. "
					+ e.getMessage());
		} finally {
			connectionPool.closeConnection(conn);
		}
	}

	/**
	 * Search for applications created by given user. Applications ID are stored
	 * as array list and send back to user.
	 * 
	 * @param headElement
	 * @param user
	 */
	public ArrayList<String> getApplicationsListByUser(String user, boolean getPublicApps) {

		// TODO: update search string with the user name search
		Connection conn = null;

		try {
			Calendar c = new GregorianCalendar();
			ResultSet resultApplications;
			conn = Config.useConnectionPool() ? connectionPool.getConnection() : initialConnection;
			Statement statement = conn.createStatement();
			// Main search query
			String st;
			if (user != null && !user.equals("")) {
				if (!getPublicApps) {
					// Application of the current user only

					st = "select application_id from "
							+ "(select application_id, creation_date as sortingDate from applications where modification_date = 0 and author='"
							+ user
							+ "'"
							+ "union "
							+ "select application_id, modification_date as sortingDate from applications where modification_date <> 0 and author='"
							+ user + "'" + ") as final order by final.sortingDate desc";
					// st =
					// "SELECT application_id FROM applications where author='"
					// + user
					// + "' ORDER BY application_title;";
				} else {
					// Show application of the current user + old application
					// marked by markku

					// st =
					// "SELECT application_id FROM applications where (author='"
					// + user
					// + "' or author='markku') ORDER BY application_title;";

					st = "select application_id from "
							+ "(select application_id, creation_date as sortingDate from applications where modification_date = 0 and (author='"
							+ user
							+ "' or is_demo=true) "
							+ "union "
							+ "select application_id, modification_date as sortingDate from applications where modification_date <> 0 and (author='"
							+ user + "' or is_demo=true)" + ") as final order by final.sortingDate desc";
				}
			} else {
				st = "SELECT application_id FROM applications ORDER BY application_title;";
			}

			resultApplications = statement.executeQuery(st);

			ArrayList<String> ids = new ArrayList<String>();
			// Iterate on application list
			while (resultApplications.next()) {
				if (resultApplications.getObject(1) != null) {
					ids.add(resultApplications.getObject(1).toString());
				}
			}
			resultApplications.close();
			statement.close();
			return ids;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connectionPool.closeConnection(conn);
		}
		return null;
	}

	private void parseDBObjectIntoPrpertyValue(Property p, ResultSet resultSet, int columnN) throws SQLException {
		if (resultSet.getObject(columnN) != null) {
			if (p instanceof DateProperty) {
				((DateProperty) p).setValue(resultSet.getTimestamp(columnN).getTime(), resultSet.getString(columnN)
						.toString());
			} else {
				p.setValue(resultSet.getObject(columnN).toString());
			}
		} else {
			if (p instanceof DateProperty) {
				((DateProperty) p).setValue(0, "");
			} else {
				p.setValue("");
			}
		}
	}

	// Old version of this function
	public APElementApplication getApplicationByID(String applicationID) {
		return getApplicationByID("", applicationID, false);
	}

	/**
	 * Search for application by ID by given user. Applications and pages can be
	 * created including only main information or all available information
	 * depending on the value of includeOnlyMainInfo parameter.
	 * 
	 * @param headElement
	 * @param user
	 */
	public APElementApplication getApplicationByID(String user, String applicationID, boolean includeOnlyMainInfo) {

		includeOnlyMainInfo = false;

		APElementApplication element = null;
		// TODO: update search string with the user name search
		Connection conn = null;

		try {
			ResultSet resultApplications;
			conn = Config.useConnectionPool() ? connectionPool.getConnection() : initialConnection;
			Statement statement = conn.createStatement();
			// Main search query
			String st = "SELECT * FROM applications where application_id = '" + applicationID + "';";

			resultApplications = statement.executeQuery(st);

			String[] propertyCodes = getPropertiesCodes(resultApplications.getMetaData(), "applications");
			Property[] propertySources = getPropertiesInfo(resultApplications.getMetaData(), "applications");
			// Iterate on application list

			while (resultApplications.next()) {
				HashMap<String, Property> properties = new HashMap<String, Property>();
				for (int i = 0; i < resultApplications.getMetaData().getColumnCount(); i++) {
					Property p = propertySources[i].clone();
					parseDBObjectIntoPrpertyValue(p, resultApplications, i + 1);
					p.setEditableNow(true);
					properties.put(propertyCodes[i], p);
				}
				if (includeOnlyMainInfo) {
					element = new APElementApplication(properties, null, null);
				} else {
					element = new APElementApplication(properties, null, (new ServerFileStructureParser(null,
							getApplicationFolderPath(resultApplications.getString("application_id")), null))
							.getRootFolder());
				}

				// Pages
				// Create application pages
				Statement statementAdvanced = conn.createStatement();
				String st2 = "SELECT * FROM pages where application_id = '"
						+ resultApplications.getString("application_id") + "';";
				ResultSet resultPages;
				resultPages = statementAdvanced.executeQuery(st2);

				ArrayList<Option> options = new ArrayList<Option>();
				int i = 0;
				int selectedOption = -1;

				while (resultPages.next()) {
					getPageByID(resultPages.getString("page_id"), element, includeOnlyMainInfo);
					options.add(new Option(resultPages.getString("page_title"), resultPages.getString("page_id")));
					String optionValue = options.get(i).getValue();
					String mainPage = element.getProperties().get(Property.MAIN_PAGE).getStringValue();
					if (optionValue.equals(mainPage)) {
						// This page is set as a Main page
						selectedOption = i;
					}
					i++;
				}
				// Add page list
				Property mainPageOldVersion = element.getProperties().get(Property.MAIN_PAGE);
				Property pageList = null;
				if (options.isEmpty()) {
					pageList = new OptionProperty(mainPageOldVersion.getName(), null, -1, mainPageOldVersion
							.getDescription(), true);
				} else {
					pageList = new OptionProperty(mainPageOldVersion.getName(), options.toArray(new Option[options
							.size()]), selectedOption, mainPageOldVersion.getDescription(), true);
				}

				element.getProperties().remove(Property.MAIN_PAGE);
				element.getProperties().put(Property.MAIN_PAGE, pageList);

				resultPages.close();
				statementAdvanced.close();
			}
			resultApplications.close();
			statement.close();

			return element;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			connectionPool.closeConnection(conn);
		}
	}

	/**
	 * Search for application by ID
	 * 
	 * @param headElement
	 * @param user
	 */
	public APElementPage getPageByID(String pageID, APElement parent, boolean includeOnlyMainInfo) {

		APElementPage element = null;
		// TODO: update search string with the user name search
		Connection conn = null;

		try {

			// Pages
			// Create application pages
			conn = Config.useConnectionPool() ? connectionPool.getConnection() : initialConnection;
			Statement statementAdvanced = conn.createStatement();
			String st2 = "SELECT * FROM pages where page_id = '" + pageID + "';";
			ResultSet resultPages;
			resultPages = statementAdvanced.executeQuery(st2);

			String[] propertyCodesPages = getPropertiesCodes(resultPages.getMetaData(), "pages");
			Property[] propertySourcesPages = getPropertiesInfo(resultPages.getMetaData(), "pages");

			while (resultPages.next()) {
				HashMap<String, Property> propertiesPages = new HashMap<String, Property>();
				for (int i = 0; i < resultPages.getMetaData().getColumnCount(); i++) {
					Property p = propertySourcesPages[i].clone();

					parseDBObjectIntoPrpertyValue(p, resultPages, i + 1);
					p.setEditableNow(true);
					propertiesPages.put(propertyCodesPages[i], p);
				}
				ArrayList<String> possibleFolderNames = new ArrayList<String>();
				possibleFolderNames.add(propertiesPages.get(Property.RELATED_URL).getStringValue());
				if (includeOnlyMainInfo) {
					element = new APElementPage(propertiesPages, parent, null);
				} else {
					element = new APElementPage(propertiesPages, parent, (new ServerFileStructureParser(
							possibleFolderNames, getApplicationFolderPath(resultPages.getString("application_id")),
							// "/Users/evgeniasamochadina/Documents/Workspace/file_structure/xide_file_system/users/markku/news_widget/",
							propertiesPages.get(Property.RELATED_URL).getStringValue())).getRootFolder());
				}

			}
			resultPages.close();
			statementAdvanced.close();

			return element;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			connectionPool.closeConnection(conn);
		}
	}

	/**
	 * Saves information about newly created template in the database
	 * 
	 * @param id
	 * @param title
	 * @param descr
	 * @param author
	 * @param type
	 * @param tags
	 */
	private void saveTemplate(String id, String title, String descr, String author, String type, ArrayList<String> tags)
			throws XIDEException {// Template

		// template)
		// {
		Connection conn = null;

		try {
			conn = Config.useConnectionPool() ? connectionPool.getConnection() : initialConnection;
			Statement statement = conn.createStatement();

			// Save template into templates
			String st = "INSERT INTO templates VALUES (" + " '" + id + "'," + " '" + title + "'," + " '" + descr + "',"
					+ " '" + author + "'," + " " + "NOW()," + "" + "3, '" + id + "/" + "');";

			statement.executeUpdate(st);
			statement.close();

			// Save tags
			saveTagsReferences(tags, id);

		} catch (SQLException e) {
			throw new XIDEException(e.getMessage());
		} finally {
			connectionPool.closeConnection(conn);
		}
	}

	private boolean saveTag(Tag tag) throws XIDEException {

		Connection conn = null;
		try {
			conn = Config.useConnectionPool() ? connectionPool.getConnection() : initialConnection;
			// Get the number of tags in the table
			ResultSet resultTagsNumber;
			Statement statement = conn.createStatement();

			String st = "SELECT count(*) as c from tags;";
			resultTagsNumber = statement.executeQuery(st);
			resultTagsNumber.next();

			int numberOfTags = resultTagsNumber.getInt("c");
			statement.close();

			// Save new tag
			statement = conn.createStatement();
			st = "INSERT INTO tags VALUES (" + " '" + (numberOfTags + 1) + "'," + " '" + tag.getTitle() + "'," + " '"
					+ tag.getDescription() + "');";
			statement.executeUpdate(st);
			statement.close();

			return true;

		} catch (SQLException e) {
			throw new XIDEException("An errror occur during tag adding procedure. " + e.getMessage());
		} finally {
			connectionPool.closeConnection(conn);
		}
	}

	private boolean checkApplicationRelURLInWebapps(String rel_url) throws XIDEException {

		File webapps = new File(Config.getTomcatWebappsPath());
		String[] appNames = webapps.list();
		boolean result = true;
		for (int i = 0; i < appNames.length; i++) {
			if (appNames[i].equals(rel_url)) {
				throw new XIDEException("This url is already in use! Please try another one.");
			}
		}
		return result;
	}

	private boolean checkApplicationRelURLUnicity(String rel_url) throws XIDEException {

		Connection conn = null;
		try {
			// Get the number of tags in the table
			ResultSet resultApps;
			conn = Config.useConnectionPool() ? connectionPool.getConnection() : initialConnection;
			Statement statement = conn.createStatement();

			String st = "SELECT count(*) as c from applications where LOWER(rel_url) = '" + rel_url.toLowerCase()
					+ "';";
			resultApps = statement.executeQuery(st);
			resultApps.next();

			int numberOfTags = resultApps.getInt("c");
			statement.close();

			if (numberOfTags > 0) {
				throw new XIDEException("This url is already in use! Please try another one.");
			} else {
				return true;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} finally {
			connectionPool.closeConnection(conn);
		}
	}

	private boolean checkPageRelURLUnicity(String rel_url, String appID) throws XIDEException {

		Connection conn = null;
		try {
			// Get the number of tags in the table
			ResultSet resultApps;
			conn = Config.useConnectionPool() ? connectionPool.getConnection() : initialConnection;
			Statement statement = conn.createStatement();

			String st = "SELECT count(*) as c from pages where LOWER(rel_url) = '" + rel_url.toLowerCase()
					+ "' and application_id = " + appID + ";";
			resultApps = statement.executeQuery(st);
			resultApps.next();

			int numberOfTags = resultApps.getInt("c");
			statement.close();

			if (numberOfTags > 0) {
				throw new XIDEException("This url is already in use! Please try another one.");
			} else {
				return true;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} finally {
			connectionPool.closeConnection(conn);
		}
	}

	private void checkTagNameUnicity(String tagName) throws XIDEException {

		Connection conn = null;

		try {
			// Get the number of tags in the table
			ResultSet resultTagsNumber;
			conn = Config.useConnectionPool() ? connectionPool.getConnection() : initialConnection;
			Statement statement = conn.createStatement();

			String st = "SELECT count(tag_id) as c from tags where LOWER(title) = '" + tagName.toLowerCase() + "';";
			resultTagsNumber = statement.executeQuery(st);
			resultTagsNumber.next();

			int numberOfTags = resultTagsNumber.getInt("c");
			statement.close();

			if (numberOfTags > 0) {
				throw new XIDEException("This tag name is already in use! Please try another one");
			}
		} catch (SQLException e) {

			throw new XIDEException(e.getMessage());

		} finally {
			connectionPool.closeConnection(conn);
		}
	}

	private void checkTemplateIDUnicity(String templateID) throws XIDEException {

		Connection conn = null;

		try {
			// Get the number of tags in the table
			ResultSet resultTagsNumber;
			conn = Config.useConnectionPool() ? connectionPool.getConnection() : initialConnection;
			Statement statement = conn.createStatement();

			String st = "SELECT count(template_id) as c from templates where LOWER(template_id) = '"
					+ templateID.toLowerCase() + "';";
			resultTagsNumber = statement.executeQuery(st);
			resultTagsNumber.next();

			int numberOfTemplates = resultTagsNumber.getInt("c");
			statement.close();

			if (numberOfTemplates > 0) {
				throw new XIDEException("This tag name is already in use! Please try another one");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new XIDEException(e.getMessage());
		} finally {
			connectionPool.closeConnection(conn);
		}
	}

	private void deleteTagsReferencesForTemplate(String templateID) throws SQLException {

		// Delete old tags tags
		Connection conn = Config.useConnectionPool() ? connectionPool.getConnection() : initialConnection;
		Statement statement = conn.createStatement();
		String deleteTagReferences = "DELETE from used_tags_map where" + " template_id = '" + templateID + "';";
		statement.executeUpdate(deleteTagReferences);
		statement.close();
		connectionPool.closeConnection(conn);
	}

	private void deleteTagsReferencesForTag(String tagTitle) throws SQLException {

		// Delete old tags tags
		Connection conn = Config.useConnectionPool() ? connectionPool.getConnection() : initialConnection;
		Statement statement = conn.createStatement();
		String deleteTagReferences = "DELETE from used_tags_map where"
				+ " tag_id in ( SELECT tag_id from tags where title = '" + tagTitle + "');";
		statement.executeUpdate(deleteTagReferences);
		statement.close();
		connectionPool.closeConnection(conn);
	}

	private void saveTagsReferences(ArrayList<String> tags, String templateID) throws SQLException {

		Connection conn = Config.useConnectionPool() ? connectionPool.getConnection() : initialConnection;

		// Save tags
		// ArrayList<String> tags = (ArrayList<String>)
		// template.getProperties().get(Property.TAGS).getValue();
		for (int i = 0; i < tags.size(); i++) {
			Statement statement = conn.createStatement();
			String tagsUpdateQuery = "INSERT INTO used_tags_map SELECT tag_id, '" + templateID
					+ "' from tags where tags.title = '" + tags.get(i);
			tagsUpdateQuery = tagsUpdateQuery + "' ";
			statement.executeUpdate(tagsUpdateQuery);
			statement.close();
		}

		connectionPool.closeConnection(conn);
	}

	public void removeComponent(String componendId) throws SQLException {

		Connection conn = Config.useConnectionPool() ? connectionPool.getConnection() : initialConnection;

		// Delete from SQL
		Statement statement = conn.createStatement();
		String query = "DELETE from templates where" + " template_id = '" + componendId + "';";
		statement.executeUpdate(query);
		statement.close();

		// Delete from filesystem
		PublishHandler.deleteDir(new File(Config.getComponentsPath() + componendId));

		connectionPool.closeConnection(conn);
	}

	/**
	 * Read file content as a string
	 * 
	 * @param filePath
	 * @return
	 * @throws java.io.IOException
	 */
	public static String readFileAsString(String filePath) throws java.io.IOException {

		StringBuffer fileData = new StringBuffer(1000);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		char[] buf = new char[1024];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			fileData.append(buf, 0, numRead);
		}
		reader.close();
		return fileData.toString();
	}

	/**
	 * Write string to the file with the given name
	 * 
	 * @param filePath
	 *            file name
	 * @param string
	 *            content to write
	 * @throws java.io.IOException
	 */
	public static void writeStringToFile(String filePath, String string) throws java.io.IOException {

		BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
		writer.write(string);
		writer.flush();
		writer.close();
	}

	public String getServerURL() {

		return Config.getServerURL();
	}

	public static void deleteComponentFromDatabase(String name) {

		Connection conn = null;

		try {

			conn = Config.useConnectionPool() ? connectionPool.getConnection() : initialConnection;
			conn = DriverManager.getConnection(Config.getConnectionString(), Config.getConnectionStringProperties());
			PreparedStatement statement = conn.prepareStatement("DELETE FROM templates WHERE template_id=?");
			statement.setString(1, name);

			int result = statement.executeUpdate();

			if (result == 1) {
				loggerInstance.log(Level.DEBUG, "Removed \"" + name + "\" from SQL database.");
			}

			statement.close();

		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			connectionPool.closeConnection(conn);
		}
	}
}
