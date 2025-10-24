# LoginSystem

Simple Java login system demo.

## Files

- `LoginSystem.java` - Java program that provides a basic login flow (username/password). 
- `schema.sql` - SQL script to create the database and `users` table used by the application.

## Description

This small project demonstrates a minimal Java-based login system backed by a MySQL database. It includes a SQL script (`schema.sql`) that creates a `userdb` database and a `users` table with `id`, `username`, and `password` columns.

## Requirements

- Java JDK 8 or newer installed and available on PATH.
- MySQL server (or compatible) to create the database and table from `schema.sql`.
- (Optional) An IDE such as VS Code or IntelliJ IDEA.

## Database schema (from `schema.sql`)

- Database: `userdb`
- Table: `users`
  - `id` INT AUTO_INCREMENT PRIMARY KEY
  - `username` VARCHAR(100) UNIQUE
  - `password` VARCHAR(255)

## Setup

1. Create the database and table. From a MySQL client or terminal, run:

```sql
-- run the commands in schema.sql
CREATE DATABASE userdb;
USE userdb;
CREATE TABLE users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(100) UNIQUE,
  password VARCHAR(255)
);
```

Or import the file directly (example using `mysql` CLI):

```powershell
mysql -u <db_user> -p < schema.sql
```

Replace `<db_user>` with your MySQL username. You'll be prompted for your password.

2. (Optional) Insert a test user into the `users` table. Example:

```sql
INSERT INTO userdb.users (username, password) VALUES ('testuser', 'password123');
```

Note: The project likely stores raw passwords; for production use, always store salts and hashed passwords (e.g., bcrypt).

## Build & Run (Java)

From the project root (where `LoginSystem.java` is located):

```powershell
# Compile
javac LoginSystem.java

# Run
java LoginSystem
```

If `LoginSystem.java` expects database connection parameters (host, user, password), pass them as command-line arguments or configure them in the source as required. Check the top of `LoginSystem.java` to see how it reads DB connection details.

## Notes & Recommendations

- Passwords in `schema.sql` and any example inserts are plain text for demo only. Use a secure hashing algorithm in real applications.
- If `LoginSystem.java` uses JDBC, ensure the MySQL JDBC driver (Connector/J) is available on the classpath when running; for example, if running with an external JAR, add `-cp` accordingly.

Example run with connector jar:

```powershell
# Assuming mysql-connector-java.jar is in lib\
javac -cp lib\mysql-connector-java.jar LoginSystem.java
java -cp .;lib\mysql-connector-java.jar LoginSystem
```

- If you want me to inspect `LoginSystem.java` and add exact run instructions or improve security (hashing, prepared statements), tell me and I will update the README with precise steps and safe defaults.

## License

This project has no license specified. Add a `LICENSE` file if you want to share under a particular license.

## Contact

For questions or updates, open an issue or message the maintainer.
