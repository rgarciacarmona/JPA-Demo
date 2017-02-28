package sample.db.jpa;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;

import sample.db.pojos.Department;

public class JPAUpdate {

	// Put entity manager here so it can be used in several methods
	private static EntityManager em;
	
	private static void printDepartments() {
		Query q1 = em.createNativeQuery("SELECT * FROM departments", Department.class);
		List<Department> deps = (List<Department>) q1.getResultList();
		// Print the departments
		for (Department department : deps) {
			System.out.println(department);
		}
	}
	
	public static void main(String[] args) throws Exception {
		// Get the entity manager
		// Note that we are using the class' entity manager
		em = Persistence.createEntityManagerFactory("company-provider").createEntityManager();
		em.getTransaction().begin();
		em.createNativeQuery("PRAGMA foreign_keys=ON").executeUpdate();
		em.getTransaction().commit();

		// Get the new department's location from the command prompt
		System.out.println("Company's departments:");
		Query q1 = em.createNativeQuery("SELECT * FROM departments WHERE name LIKE ?", Department.class);
		printDepartments();
		System.out.print("Choose a department to change its location. Type it's ID:");
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		int dep_id = Integer.parseInt(reader.readLine());
		Query q2 = em.createNativeQuery("SELECT * FROM departments WHERE id = ?", Department.class);
		q2.setParameter(1, dep_id);
		Department dep = (Department) q2.getSingleResult();
		System.out.print("Type it's new location:");
		String newLocation = reader.readLine();
		
		// Begin transaction
		em.getTransaction().begin();
		// Make changes
		dep.setAddress(newLocation);
		// End transaction
		em.getTransaction().commit();
		
		// Close the entity manager
		em.close();
	}
}
