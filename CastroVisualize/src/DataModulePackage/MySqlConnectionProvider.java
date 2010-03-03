package DataModulePackage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlConnectionProvider {
	private static MySqlConnectionProvider INST;

	private MySqlConnectionProvider() {
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
		}
		catch (ClassNotFoundException e)
		{
		
			e.printStackTrace();
		}

		//Class.forName("com.mysql.jdbc.Driver");
	}

	public static Connection getNewConnection(String host, String database, String user, String password) {
		if (INST == null) {
			INST = new MySqlConnectionProvider();
		}
		return INST.createConnection(host, database, user, password);
	}

	public Connection createConnection(String host, String database, String user, String password) {
		String url = new String("jdbc:mysql://" + host + "/" + database);
		try {
			return DriverManager.getConnection(url, user, password);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}

}