package sample.db.jdbc;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import sample.db.pojos.Employee;
import sample.db.graphics.ImageWindow;

public class SQLSearch {

	// Boolean to choose between showing the image or storing it as a file:
	//	true: show
	//	false: store
	private static boolean showImage = true;
	
	public static void main(String args[]) {
		try {
			// Open database connection
			Class.forName("org.sqlite.JDBC");
			Connection c = DriverManager.getConnection("jdbc:sqlite:./db/company.db");
			c.createStatement().execute("PRAGMA foreign_keys=ON");
			System.out.println("Database connection opened.");
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			System.out.print("Name of the employee to be shown: ");
			String searchName = reader.readLine();

			// Retrieve data: begin
			String sql = "SELECT * FROM employees WHERE name LIKE ?";
			PreparedStatement prep = c.prepareStatement(sql);
			prep.setString(1, searchName);
			ResultSet rs = prep.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				Date dob = rs.getDate("dob");
				String address = rs.getString("address");
				Double salary = rs.getDouble("salary");
				byte[] photo = rs.getBytes("photo");
				// Note that department is going to show null, even if the
				// employee is assigned to one, that's because we didn't
				// retrieve the department from the database. We should!!
				Employee employee = new Employee(id, name, dob, address, salary, photo);
				System.out.println(employee);
				// Process the photo
				if (photo!=null) {
					ByteArrayInputStream blobIn = new ByteArrayInputStream(employee.getPhoto());
					// Show the photo
					if (showImage) {
						ImageWindow window = new ImageWindow();
						window.showBlob(blobIn);
					}
					// Write the photo in a file
					else {
						File outFile = new File("./photos/Output.png");
						OutputStream blobOut = new FileOutputStream(outFile);
						byte[] buffer = new byte[blobIn.available()];
						blobIn.read(buffer);
						blobOut.write(buffer);
						blobOut.close();
					}
				}
			}
			rs.close();
			prep.close();
			System.out.println("Search finished.");
			// Retrieve data: end
		    
			// Close database connection
			c.close();
			System.out.println("Database connection closed.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
