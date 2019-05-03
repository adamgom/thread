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
				System.out.println("DBCheck - List of databases on SQLServer");	
				while (result.next()) {
					System.out.println("DBCheck - " + result.getString(1));
					if (result.getString(1).equals(Config.DBNAME)) {
						this.create = false;
						System.out.println("... DBCheck - database " + Config.DBNAME + " found");
					}
				}
			}
			
			if (this.create) {
				System.out.println("DBCheck - create database " + Config.DBNAME);
				PreparedStatement preparedStatement = connection.prepareStatement(dbCreate);
				preparedStatement.executeUpdate();
				preparedStatement.close();					
			}
			
			System.out.println("");
			return true;

		} catch (SQLException e) {
			System.out.println("DBCheck error: ");
			e.printStackTrace();
			return false;
		}
	}
}
