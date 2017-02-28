package sample.db.jpa;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import sample.db.pojos.Department;
import sample.db.pojos.Employee;
import sample.db.pojos.Report;
 
 
public class FullJPAExample {
 
	private static final String PERSISTENCE_PROVIDER = "company-provider";
	private static EntityManagerFactory factory;
	// Used for parsing dates
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	public static void main(String[] args) {
 
		// Create entity manager
		// The connection to the database (see persistence.xml) is performed here
		factory = Persistence.createEntityManagerFactory(PERSISTENCE_PROVIDER);
		EntityManager em = factory.createEntityManager();
		em.getTransaction().begin();
		em.createNativeQuery("PRAGMA foreign_keys=ON").executeUpdate();
		em.getTransaction().commit();
		
		// We create three transactions
		EntityTransaction tx1 = em.getTransaction();
		EntityTransaction tx2 = em.getTransaction();
		EntityTransaction tx3 = em.getTransaction();
 
		// Start transaction with method begin()
		tx1.begin();
 
		// Create 2 departments with dummy data
		Department dep1 = new Department("Marketing", "Burgos");
		Department dep2 = new Department("Technology", "Arganda");
		em.persist(dep1);
		em.persist(dep2);
		
		// Commit transaction
		tx1.commit();
		
		// Query for existing data in SQL syntax
		Query q1 = em.createNativeQuery("SELECT * FROM departments", Department.class);
		List<Department> departmentList = (List<Department>) q1.getResultList();
		// Loop trough departmentList and print out departments
		for (Department department : departmentList) {
			System.out.println(department);
		}
				
		// Start transaction with method begin()
		tx2.begin();
				
		// Create 10 employees with dummy data
		for (int i = 0; i < 10; i++) {
			// Create the employee
			Employee employee = new Employee();
			employee.setName("Employee-" + i);
			employee.setDob(Date.valueOf(LocalDate.parse("2016-12-" + (20+i), formatter)));
			employee.setAddress("Address-" + i);
			employee.setSalary((double) i+100*i);
			// Insert employee into the DB
			em.persist(employee);
			// Assign the employee to a department
			employee.setDepartment(dep1);
			dep1.addEmployee(employee);
		}
		
		// Commit transaction
		tx2.commit();
		
		// Query for existing data in JPQL syntax
		Query q2 = em.createQuery("SELECT emp FROM Employee emp", Employee.class);
		List<Employee> employeeList = q2.getResultList();
		// Loop trough employeeList and print out employees
		for (Employee employee : employeeList) {
			System.out.println(employee);
		}
				
		// Start transaction with method begin()
		tx3.begin();
								
		// Create 2 reports with dummy data
		Report rep1 = new Report("Sales Report 1", "Nothing Sold", LocalDate.parse("2016-12-15", formatter));
		Report rep2 = new Report("Sales Report 2", "Everything Sold", LocalDate.parse("2016-12-30", formatter));
		em.persist(rep1);
		em.persist(rep2);
		
		// Get 4 employees from the database
		Query q3 = em.createNativeQuery("SELECT * FROM employees", Employee.class);
		List<Employee> employeeList2 = (List<Employee>) q3.getResultList();
		Employee emp1 = employeeList2.get(0);
		Employee emp2 = employeeList2.get(1);
		Employee emp3 = employeeList2.get(2);
		Employee emp4 = employeeList2.get(3);
		// Set authors
		rep1.addAuthor(emp1);
		rep1.addAuthor(emp2);
		rep2.addAuthor(emp3);
		rep2.addAuthor(emp4);
		emp1.addReport(rep1);
		emp2.addReport(rep2);
		emp3.addReport(rep1);
		emp4.addReport(rep2);
		// Print the reports
		System.out.println(rep1);
		System.out.println(rep2);
		
		// Commit transaction
		tx3.commit();
		
		// Close the connection and factory
		em.close();
		factory.close();
 
	}
 
}