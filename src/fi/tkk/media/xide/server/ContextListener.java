package fi.tkk.media.xide.server;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


public class ContextListener implements ServletContextListener {

	public void contextDestroyed(ServletContextEvent arg0) {

		try {
			Enumeration<Driver> drivers = DriverManager.getDrivers();
			while (drivers.hasMoreElements()) {

				Driver driver = drivers.nextElement();
				DriverManager.deregisterDriver(driver);
				
				System.out.println("Deregistered driver " + driver.toString());
			}

		} catch (SQLException sqle) {
			System.out.println("Unable to deregister driver: " + sqle.getMessage());
		}
	}

	public void contextInitialized(ServletContextEvent arg0) {

	}

}