package producerConsumerDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import main.Config;

public class DBCheck {
	private String dbCreate = "CREATE DATABASE " + Config.DBNAME;
	private boolean create;
//	private String dbCreate = "CREATE DATABASE IF EXISTS " + DBLoginInfo.DBNAME.getText();
	
	public DBCheck() {
		this.create = true;
	}
	
    public boolean dbCheck() {
		try (Connection connection = DriverManager.getConnection(Config.DBURL, Config.USER, Config.PASSWORD);) {
//			PreparedStatement preparedStatement = connection.prepareStatement(dbCreate);
//			preparedStatement.executeUpdate();
//			preparedStatement.close();
			
			try (ResultSet result = connection.getMetaData().getCatalogs();) {
				while (result.next()) {
					if (result.getString(1).equals(Config.DBNAME)) {
						this.create = false;
					}
				}
			}
			
			if (this.create) {
				PreparedStatement preparedStatement = connection.prepareStatement(dbCreate);
				preparedStatement.executeUpdate();
				preparedStatement.close();					
			}

			return true;

		} catch (SQLException e) {
			System.out.print("DBCheck error: ");
			e.printStackTrace();
			return false;
		}
	}
}
