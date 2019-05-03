package producerConsumerDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import main.Config;

public class DBCreateTable {
	
	private String createTable = "CREATE TABLE IF NOT EXISTS " + Config.tableName + " (" + 
			Config.col1name + Config.col1type + ", " + Config.col2name + Config.col2type + ", " + 
			Config.col3name + Config.col3type + ", " + Config.col4name + Config.col4type + 
			", PRIMARY KEY (" + Config.primaryKey + "))";
	
    public boolean createTable() {
    	try (Connection connection = DriverManager.getConnection(Config.DBURL, Config.USER, Config.PASSWORD);) {
    		connection.setCatalog(Config.DBNAME);
			Statement statement = connection.createStatement();
			statement.executeUpdate(createTable);
    		statement.close();
    		return true;
		} catch (SQLException e) {
			System.out.println("create table error: ");
			e.printStackTrace();
			return false;
		}
    }
}
