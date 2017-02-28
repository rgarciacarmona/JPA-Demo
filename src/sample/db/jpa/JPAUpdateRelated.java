package sample.db.jpa;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;

import sample.db.pojos.Department;
import sample.db.pojos.Employee;

public class JPAUpdateRelated {

	// Put entity manager and buffered reader here so it can be used
	// in several methods
	private static EntityManager em;
	private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	// Used for parsing dates
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	private static void printDepartments() {
		Query q1 = em.createNativeQuery("SELECT * FROM departments", Department.class);
		List<Department> deps = (List<Department>) q1.getResultList();
		// Print the departments
		for (Department department : deps) {
			System.out.println(department);
		}
	}

	private static Employee createEmployee() throws Exception {
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
		System.out.print("Do you want to add a photo? (Y/N): ");
		String yesNo = reader.readLine();
		byte[] bytesBlob = null;
		// Insert new record: begin
		if (yesNo.equalsIgnoreCase("Y")) {
			// With photo
			System.out.print("Type the file name as it appears in folder /photos, including extension: ");
			String fileName = reader.readLine();
			File photo = new File("./photos/" + fileName);
			InputStream streamBlob = new FileInputStream(photo);
			bytesBlob = new byte[streamBlob.available()];
		}
		// Create the object
		Employee emp = new Employee(name, dobDate, address, salary, bytesBlob, null);
		
		// Begin transaction
		em.getTransaction().begin();
		// Store the object
		em.persist(emp);
		// End transaction
		em.getTransaction().commit();
		
		// Return the Employee
		return emp;
	}

	public static void main(String[] args) throws Exception {
		// Get the entity manager
		// Note that we are using the class' entity manager
		em = Persistence.createEntityManagerFactory("company-provider").createEntityManager();
		em.getTransaction().begin();
		em.createNativeQuery("PRAGMA foreign_keys=ON").executeUpdate();
		em.getTransaction().commit();

		// Create an employee
		Employee emp = createEmployee();
		
		// Choose his new department
		printDepartments();
		System.out.print("Choose a department to assign to the employee:");
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		int dep_id = Integer.parseInt(reader.readLine());
		Query q2 = em.createNativeQuery("SELECT * FROM departments WHERE id = ?", Department.class);
		q2.setParameter(1, dep_id);
		Department dep = (Department) q2.getSingleResult();

		// Assign employee to department
		// Begin transaction
		em.getTransaction().begin();
		// Make changes
		// Notice the double link
		emp.setDepartment(dep);
		dep.addEmployee(emp);
		// End transaction
		em.getTransaction().commit();

		// Close the entity manager
		em.close();
	}
}
