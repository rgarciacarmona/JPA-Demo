package sample.db.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class SQLInsertMultiple {

	public static void main(String args[]) {
		try {
			// Open database connection
			Class.forName("org.sqlite.JDBC");
			Connection c = DriverManager.getConnection("jdbc:sqlite:./db/company.db");
			c.createStatement().execute("PRAGMA foreign_keys=ON");
			// Manual commit
			c.setAutoCommit(false);
			System.out.println("Database connection opened.");

			// Insert new records: begin
			// Several insertions with just one transaction
			Statement stmt = c.createStatement();
			String sql;
			sql = "INSERT INTO departments (name, address) "
					+ "VALUES ('Security', 'Madrid');";
			stmt.executeUpdate(sql);

			sql = "INSERT INTO departments (name, address) "
					+ "VALUES ('Human Resources', 'Burgos');";
			stmt.executeUpdate(sql);

			sql = "INSERT INTO departments (name, address) "
					+ "VALUES ('Research', 'Madrid');";
			stmt.executeUpdate(sql);
			stmt.close();
			// End of transaction
			c.commit();
			System.out.println("Records inserted.");
			// Insert new records: end

			// Close database connection
			c.close();
			System.out.println("Database connection closed.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
