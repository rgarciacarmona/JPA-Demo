package sample.db.jpa;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;

import sample.db.pojos.Department;

public class JPARead {

	public static void main(String[] args) throws Exception {
		// Get the entity manager
		EntityManager em = Persistence.createEntityManagerFactory("company-provider").createEntityManager();
		em.getTransaction().begin();
		em.createNativeQuery("PRAGMA foreign_keys=ON").executeUpdate();
		em.getTransaction().commit();

		// Search in departments by name
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Write the department's name: ");
		String name = reader.readLine();
		System.out.println("Matching departments:");
		Query q1 = em.createNativeQuery("SELECT * FROM departments WHERE name LIKE ?", Department.class);
		q1.setParameter(1, "%" + name + "%");
		List<Department> deps = (List<Department>) q1.getResultList();
		// Print the departments
		for (Department department : deps) {
			System.out.println(department);
		}
		
		// Get just one department
		// Only use this while looking by unique fields, if not,
		// you could get duplicate results
		System.out.print("Write the department's ID: ");
		int dep_id = Integer.parseInt(reader.readLine());
		Query q2 = em.createNativeQuery("SELECT * FROM departments WHERE id = ?", Department.class);
		q2.setParameter(1, dep_id);
		Department dep = (Department) q2.getSingleResult();
		// Print the department
		System.out.println(dep);
		
		// Close the entity manager
		em.close();
	}

}
