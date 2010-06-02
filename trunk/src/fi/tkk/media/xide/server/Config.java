package fi.tkk.media.xide.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import fi.tkk.media.xide.server.transformation.filehandlers.PublishHandler;

public class Config extends HttpServlet {

	private static Properties properties = new Properties();

	// Load all the properties from the properties file
	public void init() throws ServletException {
		InputStream inPropFile;
		String relativePath = getInitParameter("properties-file");
		File propertiesFile = new File(getServletContext().getRealPath(relativePath));

		try {
			inPropFile = new FileInputStream(propertiesFile);
			System.out.println(propertiesFile.getAbsolutePath());
			properties.load(inPropFile);
			inPropFile.close();

			trimProperties();
			
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	private void trimProperties() {
		
		Enumeration<?> e = properties.propertyNames();

		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			properties.put(key, properties.getProperty(key).trim());
		}
	}

	public static String getFileSystemPath() {

		return properties.getProperty("xide.fileSystemPath");
	}

	public static String getTomcatWebappsPath() {

		return properties.getProperty("xide.tomcat.home") + "webapps/";
	}

	public static String getServerURL() {

		return properties.getProperty("xide.serverUrl");
	}

	public static String getAppsSourcePath() {

		String result = getFileSystemPath() + properties.getProperty("xide.appsSource");
		return result;
	}

	public static String getAppsPublishedPath() {

		String result = getFileSystemPath() + properties.getProperty("xide.appsPublished");
		return result;
	}

	public static String getSystemFilesPath() {

		String result = getFileSystemPath() + properties.getProperty("xide.systemFiles");
		return result;
	}

	public static String getTempFilesPath() {

		String result = getFileSystemPath() + properties.getProperty("xide.tempPath");
		return result;
	}

	public static String getPreviewSourcePath() {

		String result = getTempFilesPath() + properties.getProperty("xide.appsSource");
		return result;
	}

	public static String getPreviewPublishedPath() {

		String result = getTempFilesPath() + properties.getProperty("xide.appsPublished");
		return result;
	}

	public static String getComponentsPath() {

		String result = getFileSystemPath() + properties.getProperty("xide.componentsPath");
		return result;
	}

	public static String getTomcatContextFilesFolder() {

		String result = properties.getProperty("xide.tomcat.home") + properties.getProperty("xide.tomcat.contextFiles");
		return result;
	}

	public static String getExistXideUri(int publishType) {

		String result;

		if (publishType == PublishHandler.PUBLISH_TO_PREVIEW) {
			result = getExistUri() + properties.getProperty("xide.exist.dbTemp");
		} else {
			result = getExistUri() + properties.getProperty("xide.exist.dbPublished");
		}

		return result;
	}

	public static String getExistUri() {

		return properties.getProperty("xide.exist.uri");

	}

	public static String getExistDbRootPath(int publishType) {

		String result;

		if (publishType == PublishHandler.PUBLISH_TO_PREVIEW) {

			result = properties.getProperty("xide.exist.dbTemp");
		} else {

			result = properties.getProperty("xide.exist.dbPublished");
		}

		return result;
	}

	public static Properties getExistCredentials() {

		Properties props = new Properties();

		props.setProperty("user", properties.getProperty("xide.exist.user"));
		props.setProperty("password", properties.getProperty("xide.exist.password"));

		return props;

	}

	public static String getConnectionString() {

		return properties.getProperty("xide.db.connectionUrl");
	}

	public static Properties getConnectionStringProperties() {

		Properties props = new Properties();

		props.setProperty("user", properties.getProperty("xide.db.user"));
		props.setProperty("password", properties.getProperty("xide.db.password"));

		return props;

	}

	public static String getTomcatPath() {
		return properties.getProperty("xide.tomcat.home");
	}

	public static boolean useExistDb() {

		String value = properties.getProperty("xide.useExistDb");

		if (value.equals("true")) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean useConnectionPool() {

		String value = properties.getProperty("xide.useConnectionPool");

		if (value.equals("true")) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean sendCrsToAsi() {

		String value = properties.getProperty("xide.sendCrsToAsi");

		if (value.equals("true")) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean storeCredentialsInSession() {

		String value = properties.getProperty("xide.storeCredentialsInSession");

		if (value.equals("true")) {
			return true;
		} else {
			return false;
		}
	}

	public static String getPageToComponentPath() {
		return properties.getProperty("xide.xslt.pageToComponent");
	}

	public static String getXideToXFormsDBPath() {
		return properties.getProperty("xide.xslt.xideToXFormsDB");
	}

	public static String getIdentityPath() {
		return properties.getProperty("xide.xslt.identityPath");
	}
	
	public static String getPageSchema() {
		return properties.getProperty("xide.schemas.page");
	}
	
	public static String getTemplateSchema() {
		return properties.getProperty("xide.schemas.component");
	}
}
