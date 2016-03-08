package at.kabeg.utilities;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DBConnectionHandler {

	private static DBConnectionHandler instance = null;
	private static Connection con = null;
	private static InitialContext context = null;
	private static DataSource ds = null;
	//private static String debug = "";
	
	private DBConnectionHandler(){
		try {
			if (con == null) { con = ds.getConnection(); /*debug = "NEW CONNECTION MADE"; */}
		} catch (SQLException e) {
			System.err.println("ERROR WITH SQL OR CONNECTION TO DB " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection(boolean oracle) throws SQLException{
		//debug = "OLD CONNECTION USED";
		if (instance == null) {
			try {
				context = new InitialContext();
				ds = (DataSource) context.lookup("java:kluimpaxlaDS");
			} catch (NamingException e) {
				System.err.println("ERROR WITH PATH, CANNOT LOCATE THE DATASOURCE " + e.getMessage());
				e.printStackTrace();
			} 
			
			instance = new DBConnectionHandler();
		} 
		/*System.out.println("\n----con 0---->"+debug + " " +con.toString());
		System.out.println("----con 1---->"+con.getMetaData().getIdentifierQuoteString());
		System.out.println("----con 2---->"+con.getMetaData().getUserName());
		System.out.println("----con 3---->"+con.getMetaData().getURL());
		System.out.println("----con 4---->"+con.getMetaData().getDriverName());
		*/
		//if (oracle) return (OracleConnection) con;
		//else return con;
		return con;
	}
	
	public static void closeConnection(ResultSet rs, Statement stmt) throws SQLException{
		if(rs!=null)rs.close();
		if(stmt!=null)stmt.close();
		if(con!=null) { con.close(); con=null; instance=null; }
	}
	
}
