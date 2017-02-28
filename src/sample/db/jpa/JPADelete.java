package sample.db.jpa;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;

import sample.db.pojos.Employee;

public class JPADelete {

	// Put entity manager here so it can be used in several methods
	private static EntityManager em;

	private static void printEmployees() {
		Query q1 = em.createNativeQuery("SELECT * FROM Employees", Employee.class);
		List<Employee> emps = (List<Employee>) q1.getResultList();
		// Print the employees
		for (Employee emp : emps) {
			System.out.println(emp);
		}
	}

	public static void main(String[] args) throws Exception {
		// Get the entity manager
		// Note that we are using the class' entity manager
		em = Persistence.createEntityManagerFactory("company-provider").createEntityManager();
		em.getTransaction().begin();
		em.createNativeQuery("PRAGMA foreign_keys=ON").executeUpdate();
		em.getTransaction().commit();

		// Get the new employee to fire from the command prompt
		System.out.println("Company's employees:");
		printEmployees();
		System.out.print("Choose a employee to fire. Type it's ID:");
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		int emp_id = Integer.parseInt(reader.readLine());
		Query q2 = em.createNativeQuery("SELECT * FROM employees WHERE id = ?", Employee.class);
		q2.setParameter(1, emp_id);
		Employee poorGuy = (Employee) q2.getSingleResult();

		// Begin transaction
		em.getTransaction().begin();
		// Store the object
		em.remove(poorGuy);
		// End transaction
		em.getTransaction().commit();

		// Close the entity manager
		em.close();
	}

}
