package fi.tkk.media.xide.server;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServlet;
import javax.sql.DataSource;

public class ConnectionPool {

	// private DataSource pool;
	private Context envContext;

	public ConnectionPool() {

		if (Config.useConnectionPool()) {

			Context initContext;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				initContext = new InitialContext();
				this.envContext = (Context) initContext.lookup("java:/comp/env");
			} catch (NamingException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				System.err.println("Exception: " + e.getMessage());
			}
		}

	}

	public Connection getConnection() throws SQLException {

		DataSource ds;
		Connection conn = null;

		try {
			ds = (DataSource) this.envContext.lookup("jdbc/xidedb");
			if (ds != null) {
				conn = ds.getConnection();
			} else {
				throw new SQLException("Could not get the datasource!");
			}
		} catch (NamingException e) {
			e.printStackTrace();
			throw new SQLException("Data pool has some problems!");
		} catch (Exception e) {
			System.err.println("Exception: " + e.getMessage());
		}

		return conn;

	}

	public void closeConnection(java.sql.Connection conn) {

		// Close ONLY if we are using the pool
		if (Config.useConnectionPool() && conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
