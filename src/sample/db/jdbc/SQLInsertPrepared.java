package sample.db.jdbc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import sample.db.pojos.Department;

public class SQLInsertPrepared {

	// Used for parsing dates
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
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

			// Get the employee info from the command prompt
			System.out.println("Please, input the employee info:");
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			System.out.print("Name: ");
			String name = reader.readLine();
			System.out.print("Date of Birth (yyyy-MM-dd): ");
			String dob = reader.readLine();
			LocalDate dobDate = LocalDate.parse(dob, formatter);
			System.out.print("Address: ");
			String address = reader.readLine();
			System.out.print("Salary: ");
			double salary = Double.parseDouble(reader.readLine());
			System.out.print("Choose a department, type its ID: ");
			printDepartments();
			int dep_id = Integer.parseInt(reader.readLine());
			System.out.print("Do you want to add a photo? (Y/N): ");
			String yesNo = reader.readLine();
					
			// Insert new record: begin
			// Without photo
			if (yesNo.equalsIgnoreCase("N")) {
				String sql = "INSERT INTO employees (name, dob , address , salary, dep_id) "
						+ "VALUES (?,?,?,?,?);";
				PreparedStatement prep = c.prepareStatement(sql);
				prep.setString(1, name);
				prep.setDate(2, Date.valueOf(dobDate));
				prep.setString(3,  address);
				prep.setDouble(4, salary);
				prep.setInt(5, dep_id);
				prep.executeUpdate();
				prep.close();
			}
			// With photo
			else {
				System.out.print("Type the file name as it appears in folder /photos, including extension: ");
				String fileName = reader.readLine();
				String sql = "INSERT INTO employees (name, dob , address , salary, dep_id, photo) "
						+ "VALUES (?,?,?,?,?,?);";
				PreparedStatement prep = c.prepareStatement(sql);
				prep.setString(1, name);
				prep.setDate(2, Date.valueOf(dobDate));
				prep.setString(3,  address);
				prep.setDouble(4, salary);
				prep.setInt(5, dep_id);
				File photo = new File("./photos/" + fileName);
				InputStream streamBlob = new FileInputStream(photo);
				byte[] bytesBlob = new byte[streamBlob.available()];
				streamBlob.read(bytesBlob);
				streamBlob.close();
				prep.setBytes(6, bytesBlob);
				prep.executeUpdate();
				prep.close();
			}
			System.out.println("Employee info processed");
			System.out.println("Record inserted.");
			// Insert new record: end

			// Close database connection
			c.close();
			System.out.println("Database connection closed.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
