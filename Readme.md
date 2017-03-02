Simple JPA Demo
===============

This is a simple JPA demo project in Eclipse that uses:

- Java 1.8 (or higher)
- SQLite-JDBC 3.8.7 (included)
- Eclipselink 2.6 (included)

It's designed to teach undergrads how to work with SQLite using JPA. Therefore, it's not a well designed project nor a sample of how a real JPA project should be structured. It's built on top of the [JDBC-Demo project](https://github.com/rgarciacarmona/JDBC-Demo), so everything that appeared in that project is still kept.

The project source code (*src* folder) is structured as follows:

- The *sample.db.pojos* package contains three POJOs that represent a sample database for the managing the employees, departments and reports of an imaginary company. Note that these POJOs have been annotated with JPA annotations.
- The *sample.db.graphics* package contains a class that can be used to open a new window and show an image inside it. It's used by other classes in this project.
- The *sample.db.jdbc* package contains several classes, each of them with a main method (and any helped method needed) that performs just one action over the database. Again, this is not how a real project should be built, but manages to isolate each action so they can be learned easily. These classes were designed to teach JDBC (not JPA) and are inherited from a previous project.
- The *sample.db.jpa* package contains several classes, each of them with a main method (and any helped method needed) that performs some actions over the database. Again, this is not how a real project should be built, but manages to clarify how JPA works so it can be learned easily. These classes were designed to teach JPA, and the order at which these classes should be studied is shown in the *Learning Order* file inside the *doc* folder.
- The *META-INF* folder contains the *persistence.xml* file that configures the persistence provider.

The database this project manages should be a file named *company.db* inside the *db* folder. This database can be created by running the *SQLCreate* class' main method.

The *lib* folder contains the SQLite-JDBC, JPA and Eclipselink libraries.

To understand how this demo works you can check the following cheatsheets (available in [this repository](https://github.com/rgarciacarmona/Java-Database-Cheatsheets)):

- [JDBC Cheatsheet](https://github.com/rgarciacarmona/Java-Database-Cheatsheets/blob/master/JDBC%20Cheatsheet.md)
- [JPA Cheatsheet]https://github.com/rgarciacarmona/Java-Database-Cheatsheets/blob/master/JPA%20Cheatsheet.md)
- [Date with Java-SQL-XML](https://github.com/rgarciacarmona/Java-Database-Cheatsheets/blob/master/Date%20with%20Java-SQL-XML.md)

JDBC and JPA in the same project
--------------------------------

This project is designed to work with JDBC and JPA at the same time, something that's not advisable but serves my teaching purposes (compare both approaches to database management). Therefore, some concessions had to be made:

- Tables must be created using the *SQLCreate* class before using JPA.
- The *SQLCreate* class also initializes the *sqlite_sequence* table.
- POJOs' primary keys use table generators configured to look in the *sqlite_sequence* table.
- JPA shouldn't be allowed to drop and create the database's tables. That's why the *eclipselink.ddl-generation* property in *META-INF/persistence.xml* is set to *none*.