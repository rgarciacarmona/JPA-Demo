package sample.db.jdbc;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import sample.db.pojos.Department;

public class SQLUpdate {

	// Put connection here so it can be used in several methods
	private static Connection c;

	private static void printDepartments() throws SQLException {
		Statement stmt = c.createStatement();
		String sql = "SELECT * FROM departments";
		ResultSet rs = stmt.executeQuery(sql);
		while (rs.next()) {
			int id = rs.getInt("id");
			String name = rs.getString("name");
			String address = rs.getString("address");
			Department department = new Department(id, name, address);
			System.out.println(department);
		}
		rs.close();
		stmt.close();
	}
		
	public static void main(String args[]) {
		try {
			// Open database connection
			Class.forName("org.sqlite.JDBC");
			// Note that we are using the class' connection
			c = DriverManager.getConnection("jdbc:sqlite:./db/company.db");
			c.createStatement().execute("PRAGMA foreign_keys=ON");
			System.out.println("Database connection opened.");

			// Change a department's location: beginning
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Choose a department, type its ID: ");
			printDepartments();
			int dep_id = Integer.parseInt(reader.readLine());
			System.out.print("Type the new department's location: ");
			String newLocation = reader.readLine();
			String sql = "UPDATE departments SET address=? WHERE id=?";
			PreparedStatement prep = c.prepareStatement(sql);
			prep.setString(1, newLocation);
			prep.setInt(2, dep_id);
			prep.executeUpdate();
			System.out.println("Update finished.");
			// Change a department's location: end

			// Close database connection
			c.close();
			System.out.println("Database connection closed.");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}