package sample.db.jdbc;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import sample.db.pojos.Department;
import sample.db.pojos.Employee;

public class SQLDelete {
	// Put connection here so it can be used in several methods
	private static Connection c;

	private static void printEmployees() throws SQLException {
		Statement stmt = c.createStatement();
		String sql = "SELECT * FROM employees";
		ResultSet rs = stmt.executeQuery(sql);
		while (rs.next()) {
			int id = rs.getInt("id");
			String name = rs.getString("name");
			Date dob = rs.getDate("dob");
			String address = rs.getString("address");
			Double salary = rs.getDouble("salary");
			byte[] photo = rs.getBytes("photo");
			Department dep = getDepartments(rs.getInt("dep_id"));
			Employee employee = new Employee(id, name, dob, address, salary, photo, dep);
			System.out.println(employee);
		}
		rs.close();
		stmt.close();
	}
	
	private static Department getDepartments(int depId) throws SQLException {
		Statement stmt = c.createStatement();
		String sql = "SELECT * FROM departments WHERE id = "+ depId;
		ResultSet rs = stmt.executeQuery(sql);
		rs.next();
		int id = rs.getInt("id");
		String name = rs.getString("name");
		String address = rs.getString("address");
		Department department = new Department(id, name, address);
		rs.close();
		stmt.close();
		return department;
	}

	public static void main(String args[]) {
		try {
			// Open database connection
			Class.forName("org.sqlite.JDBC");
			// Note that we are using the class' connection
			c = DriverManager.getConnection("jdbc:sqlite:./db/company.db");
			c.createStatement().execute("PRAGMA foreign_keys=ON");
			System.out.println("Database connection opened.");

			// Remove an employee: beginning
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Choose an employee to delete, type its ID: ");
			printEmployees();
			int dep_id = Integer.parseInt(reader.readLine());
			String sql = "DELETE FROM employees WHERE id=?";
			PreparedStatement prep = c.prepareStatement(sql);
			prep.setInt(1, dep_id);
			prep.executeUpdate();
			System.out.println("Deletion finished.");
			// Remove an employee: end

			// Close database connection
			c.close();
			System.out.println("Database connection closed.");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}