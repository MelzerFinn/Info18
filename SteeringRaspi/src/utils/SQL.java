package utils;

import java.awt.Color;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Utility class to connect to a database, create taskspecific tables and insert data to those
 * @author finn
 *
 */
public class SQL {

	private static Connection conn;

	public final static int ERROR_EXIT = 0;
	public final static int ERROR_PRINT = 1;
	public final static int ERROR_NONE = 2;

	private static int ErrorReporting = ERROR_NONE;
	
	private static Exception last = null;

	/**
	 * Used to connect to a database-server.
	 * 
	 * @param hostname
	 *            The ip of the DB-Server to connect to. Might be "localhost".
	 * @param port
	 *            The port, the DB-Server runs on. Typically 3306 for MySQL and
	 *            probably MariaDB.
	 * @param dbname
	 *            The name of the database on which queries should run. In this case
	 *            "data".
	 * @param user
	 *            The username of the DB-Server. Standard is "root".
	 * @param password
	 *            The password of the user. Standard is "".
	 * @return Boolean to indicate error (false) or success (true).
	 */
	public static boolean connect(String hostname, int port, String dbname, String user, String password) {
		try {
			Class.forName("org.gjt.mm.mysql.Driver").newInstance();
			String url = "jdbc:mysql://" + hostname + ":" + port + "/" + dbname;
			conn = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			handleError(e);
			return false;
		}
		return true;
	}

	/**
	 * Used to disconnect from the database-server.
	 * 
	 * @return Boolean to indicate error (false) or success (true).
	 */
	public static boolean disconnect() {
		try {
			conn.close();
		} catch (Exception e) {
			handleError(e);
			return false;
		}
		return true;
	}

	/**
	 * Saves the coordinates of an obstacle to table `current`.
	 * 
	 * @param X
	 * @param Y
	 * @return Boolean to indicate error (false) or success (true).
	 */
	public static boolean save(double X, double Y) {
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(
					"INSERT INTO `current` (`ID`, `TYPE`, `X`, `Y`, `TIMESTAMP`, `fetched`) VALUES (NULL, 'Sensor', '"
							+ X + "', '" + Y + "', CURRENT_TIMESTAMP, '0');");
		} catch (Exception e) {
			handleError(e);
			return false;
		}
		return true;
	}

	/**
	 * Saves the coordinates of an 3D obstacle to table `current3D`.
	 * 
	 * @param X
	 * @param Y
	 * @param Z
	 * @return Boolean to indicate error (false) or success (true).
	 */
	public static boolean save(double X, double Y, double Z) {
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("INSERT INTO `current3D` (`ID`, `X`, `Y`, `Z`, `TIMESTAMP`, `fetched`) VALUES (NULL, '"
					+ X + "', '" + Y + "', '" + Z + "', CURRENT_TIMESTAMP, '0');");
		} catch (Exception e) {
			handleError(e);
			return false;
		}
		return true;
	}

	/**
	 * Saves the coordinates of a colored 3D obstacle to table `current3DColor`.
	 * 
	 * @param X
	 * @param Y
	 * @param Z
	 * @param c
	 *            awt.java object representing the color.
	 * @return Boolean to indicate error (false) or success (true).
	 */
	public static boolean save(double X, double Y, double Z, Color c) {
		try {
			int alpha = c.getAlpha();
			int red = c.getRed();
			int green = c.getGreen();
			int blue = c.getBlue();
			int color = (alpha << 24) | (red << 16) | (green << 8) | blue;
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(
					"INSERT INTO `current3DColor` (`ID`, `X`, `Y`, `Z`, `TIMESTAMP`, `FETCHED`, `COLOR`) VALUES (NULL, '"
							+ X + "', '" + Y + "', '" + Z + "', CURRENT_TIMESTAMP, '0', '" + color + "');");
		} catch (Exception e) {
			handleError(e);
			return false;
		}
		return true;
	}

	/**
	 * Saves the new position of the Robot to table `current`
	 * 
	 * @param X
	 * @param Y
	 * @return Boolean to indicate error (false) or success (true).
	 */
	public static boolean updatePosition(int X, int Y) {
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(
					"INSERT INTO `current` (`ID`, `TYPE`, `X`, `Y`, `TIMESTAMP`, `FETCHED`) VALUES (NULL, 'Position', '"
							+ X + "', '" + Y + "', CURRENT_TIMESTAMP, '0');");
		} catch (Exception e) {
			handleError(e);
			return false;
		}
		return true;
	}

	/**
	 * Init/Reset all three tables. Deletes all previously saved data.
	 * 
	 * @return Boolean to indicate error (false) or success (true).
	 */
	public static boolean init() {
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("DROP TABLE IF EXISTS `current`;");
			stmt = conn.createStatement();
			stmt.executeUpdate("DROP TABLE IF EXISTS `current3D`;");
			stmt = conn.createStatement();
			stmt.executeUpdate("DROP TABLE IF EXISTS `current3DColor`;");

			stmt = conn.createStatement();
			stmt.executeUpdate(
					"CREATE TABLE `data`.`current` ( `ID` INT NOT NULL AUTO_INCREMENT , `TYPE` ENUM('Sensor','Position') NOT NULL , `X` DOUBLE NOT NULL , `Y` DOUBLE NOT NULL , `TIMESTAMP` DATETIME NULL DEFAULT CURRENT_TIMESTAMP , `FETCHED` BOOLEAN NOT NULL DEFAULT FALSE , PRIMARY KEY (`ID`)) ENGINE = InnoDB;");
			stmt = conn.createStatement();
			stmt.executeUpdate(
					"CREATE TABLE `data`.`current3D` ( `ID` INT NOT NULL AUTO_INCREMENT , `X` DOUBLE NOT NULL , `Y` DOUBLE NOT NULL , `Z` DOUBLE NOT NULL , `TIMESTAMP` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP , `FETCHED` BOOLEAN NOT NULL DEFAULT FALSE , PRIMARY KEY (`ID`)) ENGINE = InnoDB;");
			stmt = conn.createStatement();
			stmt.executeUpdate(
					"CREATE TABLE `data`.`current3DColor` ( `ID` INT NOT NULL AUTO_INCREMENT ,  `X` DOUBLE NOT NULL ,  `Y` DOUBLE NOT NULL ,  `Z` DOUBLE NOT NULL ,  `TIMESTAMP` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ,  `FETCHED` BOOLEAN NOT NULL DEFAULT FALSE ,  `COLOR` INT NOT NULL ,    PRIMARY KEY  (`ID`)) ENGINE = InnoDB;");

			stmt = conn.createStatement();
			stmt.executeUpdate("TRUNCATE TABLE `current`;");
			stmt = conn.createStatement();
			stmt.executeUpdate("TRUNCATE TABLE `current3D`;");
			stmt = conn.createStatement();
			stmt.executeUpdate("TRUNCATE TABLE `current3DColor`;");

		} catch (Exception e) {
			handleError(e);
			return false;
		}
		return true;
	}

	/**
	 * Set what to do if an error occurs.
	 * @param state Integer. Either SQL.ERROR_EXIT or SQL.ERROR_PRINT or SQL.ERROR_NONE
	 */
	public static void setErrorReporting(int state) {
		if (state >= 0 && state <= 4)
			ErrorReporting = state;
		else
			throw new Error("State must be between 0 and 3. Use the constants!");
	}

	private static void handleError(Exception e) {
		last = e;
		switch (ErrorReporting) {
		case ERROR_EXIT:
			e.printStackTrace();
			System.exit(1);
			break;
		case ERROR_PRINT:
			e.printStackTrace();
			break;
		case ERROR_NONE:
			break;
		default:
			break;
		}
	}
	
	/**
	 * Used to get the last exception created by a SQL call.
	 * @return Excetption
	 */
	public static Exception getLastException() {
		return last;
	}

}