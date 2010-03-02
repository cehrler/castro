import java.sql.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class DataModule {
	
	private static Connection connection;
	
	private static String connHost = "localhost";
	private static String connDB = "castro_db";
	private static String connUser = "root";
	private static String connPasswd = "root";
	
	public static void Init()
	{
		connection = MySqlConnectionProvider.getNewConnection(connHost, connDB, connUser, connPasswd);
	}
	
	
	
	public static Graph getGraph()
	{
		List<Node> ln = new ArrayList<Node>();
		try {
			java.sql.Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);			
			ResultSet srs = stmt.executeQuery("CALL getNodes(NULL, NULL);");
			
			while (srs.next()) 
			{
		        	ln.add(new Node(srs.getInt("SPEECH_ID"), srs.getString("AUTHOR_NAME"), 
		        			srs.getString("HEADLINE"), srs.getString("REPORT_DATE"), 
		        			srs.getString("SOURCE_NAME"), srs.getString("PLACE_NAME"), srs.getString("DOCTYPE_NAME"),
		        			"", srs.getString("SPEECH_DATE")));
			}
			
			System.err.println(ln.size());
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new Graph(ln, null);
		
	}
	
	
}
