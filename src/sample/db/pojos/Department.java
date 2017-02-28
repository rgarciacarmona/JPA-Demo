package sample.db.pojos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity
@Table(name = "departments")
public class Department implements Serializable {

	private static final long serialVersionUID = -4281575077333973070L;
	
	@Id
	@GeneratedValue(generator="departments")
	@TableGenerator(name="departments", table="sqlite_sequence",
	    pkColumnName="name", valueColumnName="seq", pkColumnValue="departments")
	private Integer id;
	private String name;
	private String address;
	@OneToMany(mappedBy="department")
	private List<Employee> employees;
	
	// This constructor is mandatory
	public Department() {
		super();
		this.employees = new ArrayList<Employee>();
	}
	
	// You can create as many extra constructors as you wish
	public Department(String name, String address) {
		super();
		this.name = name;
		this.address = address;
		this.employees = new ArrayList<Employee>();
	}
	
	public Department(String name, String address, List<Employee> employees) {
		super();
		this.name = name;
		this.address = address;
		this.employees = employees;
	}
	
	public Department(int id, String name, String address) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.employees = new ArrayList<Employee>();
	}

	// Hashcode uses primary keys, since they are unique
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	// Equals uses primary keys, since they are unique
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Department other = (Department) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	// Be careful! If employee prints departments and vice-versa,
	// you can end with an infinite loop
	@Override
	public String toString() {
		return "Department [id=" + id + ", name=" + name + ", address=" + address + "]";
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}

	// Additional method to add to a list
	public void addEmployee(Employee employee) {
		if (!employees.contains(employee)) {
			this.employees.add(employee);
		}
	}
	
	// Additional method to remove from a list
	public void removeEmployee(Employee employee) {
		if (employees.contains(employee)) {
			this.employees.remove(employee);
		}
	}
}
