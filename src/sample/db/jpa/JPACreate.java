package sample.db.jpa;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import sample.db.pojos.Department;

public class JPACreate {

	public static void main(String[] args) throws IOException {
		// Get the entity manager
		EntityManager em = Persistence.createEntityManagerFactory("company-provider").createEntityManager();
		em.getTransaction().begin();
		em.createNativeQuery("PRAGMA foreign_keys=ON").executeUpdate();
		em.getTransaction().commit();
		
		// Get the department info from the command prompt
		System.out.println("Please, input the department info:");
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Name: ");
		String name = reader.readLine();
		System.out.print("Address: ");
		String address = reader.readLine();
					
		// Create the object
		Department dep1 = new Department(name, address);
		
		// Begin transaction 
		em.getTransaction().begin();
		// Store the object
		em.persist(dep1);
		// End transaction
		em.getTransaction().commit();
		
		// Close the entity manager
		em.close();
	}

}
