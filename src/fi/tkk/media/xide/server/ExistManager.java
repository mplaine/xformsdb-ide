package fi.tkk.media.xide.server;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.CollectionManagementService;
import org.xmldb.api.modules.XMLResource;

import org.xmldb.api.base.Resource;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.exist.security.User;
import org.exist.xmldb.UserManagementService;

import fi.tkk.media.xide.client.Server.RPC.PublishException;

public final class ExistManager {

	private static Logger loggerInstance = Logger.getLogger(ExistManager.class);

	private static Logger getLogger() {
		if (loggerInstance == null) {
			loggerInstance = Logger.getLogger(ExistManager.class);
		}
		return loggerInstance;
	}

	private String appName;
	private String userName;
	private String appProductionPath;
	private String appDbRootPath;
	private int publishType;

	public ExistManager(String appName, String userName, String applicationProductionPath, int publishType)
			throws PublishException {

		this.appName = appName;
		this.userName = userName;
		this.appProductionPath = applicationProductionPath;
		this.appDbRootPath = applicationProductionPath + "WEB-INF/db/xide/" + appName + "/";
		this.publishType = publishType;

		try {
			createUser();
			ArrayList<String> appsXmlFilePaths = getAppXmlFilePaths();
			ArrayList<String> addedCollectionsPaths = addApplicationToExist(appsXmlFilePaths);
			configureConfFile(addedCollectionsPaths);

		} catch (Exception e) {
			throw new PublishException("Exist-db error. " + e.getMessage());
		}
	}

	public static void deleteApplicationCollection(String appName, String userName, int publishType) {

		// getLogger().log(Level.DEBUG, "Deleting \"" + appName +
		// "\" from Exist. Publish type: " + publishType);

		Collection userCollection = null;

		try {

			Properties credentials = Config.getExistCredentials();

			userCollection = DatabaseManager.getCollection(Config.getExistXideUri(publishType) + userName, credentials
					.getProperty("user"), credentials.getProperty("password"));

			if (userCollection != null) {
				CollectionManagementService mgtService = (CollectionManagementService) userCollection.getService(
						"CollectionManagementService", "1.0");
				mgtService.removeCollection(appName);
			}

		} catch (XMLDBException e) {
			e.printStackTrace();

		} finally {

			try {

				if (userCollection != null && userCollection.isOpen()) {
					userCollection.close();
				}
			} catch (XMLDBException e) {
				e.printStackTrace();
			}
		}
	}

	private ArrayList<String> addApplicationToExist(ArrayList<String> xmlFilePaths) throws PublishException {

		getLogger().log(Level.DEBUG, "Adding \"" + this.appName + "\" to Exist");

		ArrayList<String> addedCollectionsPaths = new ArrayList<String>();

		Collection appRootCollection = null;
		Collection newCollection = null;
		try {

			appRootCollection = getApplicationRootCollection();

			/** Loop through each xml file and add it to the exist-db */
			for (int i = 0; i < xmlFilePaths.size(); i++) {

				String absoluteFilePath = xmlFilePaths.get(i);

				// FOR WINDOWS
				absoluteFilePath = absoluteFilePath.replace("\\", "/");
				String relativeToDbRoot = absoluteFilePath.replace(this.appDbRootPath, "");

				// Get the collection name from the path
				String collectionPath = "";
				int lastSlash = relativeToDbRoot.lastIndexOf("/");
				System.out.println(collectionPath);

				if (lastSlash != -1) {
					collectionPath = relativeToDbRoot.substring(0, lastSlash);
				} else {
					throw new PublishException("Something is wrong with the collection path. \n");
				}

				CollectionManagementService mgtService = (CollectionManagementService) appRootCollection.getService(
						"CollectionManagementService", "1.0");
				newCollection = mgtService.createCollection(collectionPath);

				File f = new File(absoluteFilePath);
				if (!f.canRead()) {
					System.out.println("cannot read file " + f);
					throw new PublishException("Couldn't read a database XML file. \n");
				}

				// create new XMLResource; an id will be the file's name
				XMLResource document = (XMLResource) newCollection.createResource(f.getName(), "XMLResource");

				document.setContent(f);
				newCollection.storeResource(document);
				addedCollectionsPaths.add(collectionPath);
			}

			setApplicationPermissions(appRootCollection);

		} catch (XMLDBException e) {

			getLogger().log(Level.DEBUG, "XMLDBException occured when initializing exist-db. \n" + e.getMessage());
			throw new PublishException("XMLDBException occured when initializing exist-db. \n" + e.getMessage());

		} finally {

			try {

				if (appRootCollection != null && appRootCollection.isOpen()) {
					appRootCollection.close();
				}
				if (newCollection != null && newCollection.isOpen()) {
					newCollection.close();
				}

			} catch (XMLDBException e) {

				getLogger().log(Level.DEBUG, "XMLDBException occured when initializing exist-db. \n" + e.getMessage());
				e.printStackTrace();
			}
		}

		return addedCollectionsPaths;
	}

	private void createUser() throws PublishException {

		Properties credentials = Config.getExistCredentials();
		Collection rootCollection;
		try {
			rootCollection = DatabaseManager.getCollection(Config.getExistXideUri(this.publishType), credentials
					.getProperty("user"), credentials.getProperty("password"));

			UserManagementService ums = (UserManagementService) rootCollection.getService("UserManagementService",
					"1.0");
			User user = ums.getUser(this.userName);

			if (user == null) {
				User newUser = new User(this.userName, "password");
				newUser.addGroup("xide");
				ums.addUser(newUser);

			}
		} catch (XMLDBException e) {
			throw new PublishException("XMLDBException occured when creating/getting user \"" + this.userName
					+ "\" from exist-db. \n" + e.getMessage());
		} catch (Exception e) {
			throw new PublishException("Exist-db is probably not running!");
		}

	}

	private Collection getApplicationRootCollection() throws XMLDBException {

		getLogger().log(Level.DEBUG, "Getting or creating an application from Exist.");

		Properties credentials = Config.getExistCredentials();
		Collection appCollection = null;
		Collection userCollection = null;

		Collection rootCollection = DatabaseManager.getCollection(Config.getExistXideUri(this.publishType), credentials
				.getProperty("user"), credentials.getProperty("password"));

		userCollection = rootCollection.getChildCollection(this.userName);

		// Create a collection for the user if it doesn't exist
		if (userCollection == null) {

			CollectionManagementService mgtRootService = (CollectionManagementService) rootCollection.getService(
					"CollectionManagementService", "1.0");
			userCollection = mgtRootService.createCollection(this.userName);
			setApplicationPermissions(userCollection);
		}
		appCollection = userCollection.getChildCollection(this.appName);

		if (appCollection == null) {

			CollectionManagementService mgtUserService = (CollectionManagementService) userCollection.getService(
					"CollectionManagementService", "1.0");
			appCollection = mgtUserService.createCollection(this.appName);

			if (userCollection.isOpen()) {
				userCollection.close();
			}
		}
		return appCollection;
	}

	private ArrayList<String> getAppXmlFilePaths() {

		ArrayList<String> result = new ArrayList<String>();

		addXmlFilesToList(new File(this.appDbRootPath), result);

		return result;
	}

	private void addXmlFilesToList(File start, ArrayList<String> xmlFiles) {

		if (start.isDirectory()) {

			File fileList[] = start.listFiles();

			for (int index = 0; index < fileList.length; index++) {

				File file = fileList[index];

				addXmlFilesToList(file, xmlFiles);
			}
		}

		String fileName = start.getName();
		String ext = (fileName.lastIndexOf(".") == -1) ? "" : fileName.substring(fileName.lastIndexOf(".") + 1,
				fileName.length());

		if (ext.equals("xml")) {
			xmlFiles.add(start.getAbsolutePath());
		}
	}

	private void configureConfFile(ArrayList<String> addedCollectionsPaths) throws PublishException {

		getLogger().log(Level.DEBUG, "Configuring conf.xml");

		try {

			StringBuilder databaseInfoSb = new StringBuilder();
			/**
			 * Loop through each added collection and create a data-source
			 * element
			 */
			for (int i = 0; i < addedCollectionsPaths.size(); i++) {
				String collectionString = addedCollectionsPaths.get(i);
				String dataSourceElement = createDataSourceElementFromCollectionString(collectionString);

				databaseInfoSb.append(dataSourceElement);
			}

			// Add default db-info if the application didn't provide anything
			if (databaseInfoSb.length() == 0) {
				// databaseInfoSb = createDefaultDbInfo(databaseInfoSb,
				// appName);
			}

			String webXMLPath = this.appProductionPath + "WEB-INF/" + "conf.xml";

			String confContent = SearchServiceImpl.readFileAsString(webXMLPath);
			int start = confContent.indexOf("<data-source");
			int end = confContent.lastIndexOf("</data-source>");
			end += 14;
			String newContent = confContent.substring(0, start) + databaseInfoSb.toString()
					+ confContent.substring(end, confContent.length());
			SearchServiceImpl.writeStringToFile(webXMLPath, newContent);

		} catch (IOException e) {

			throw new PublishException("Error while configuring conf.xml " + e.getMessage());
		}
	}

	private String createDataSourceElementFromCollectionString(String collectionString) {

		String dataSourceId = collectionString.replace("/", "-");
		dataSourceId = dataSourceId + "-data-source";

		StringBuilder sb = new StringBuilder();

		sb.append("<data-source id=\"" + dataSourceId + "\">\n");
		sb.append("<type>2</type>\n");
		sb.append("<uri>" + Config.getExistUri() + "</uri>\n");
		sb.append("<username>" + this.userName + "</username>\n");
		sb.append("<password>password</password>\n");
		sb.append("<collection>" + Config.getExistDbRootPath(this.publishType) + this.userName + "/" + this.appName
				+ "/" + collectionString + "</collection>\n");
		sb.append("</data-source>\n");

		return sb.toString();
	}

	private void setApplicationPermissions(Collection collection) throws XMLDBException {

		String[] childCollections = collection.listChildCollections();

		for (int i = 0; i < childCollections.length; i++) {

			Collection childCollection = collection.getChildCollection(childCollections[i]);
			setApplicationPermissions(childCollection);
		}

		UserManagementService ums = (UserManagementService) collection.getService("UserManagementService", "1.0");
		User user = ums.getUser(this.userName);

		if (user != null) {

			String[] resources = collection.listResources();
			for (int i = 0; i < resources.length; i++) {
				Resource resource = collection.getResource(resources[i]);
				ums.chown(resource, user, "xide");
				ums.chmod(resource, "group=-read,-update,other=-read,-update");
			}

			ums.chown(user, "xide");
			ums.chmod("group=-read,-update,other=-read,-update");
		}

		if (collection != null && collection.isOpen()) {
			collection.close();
		}
	}

}