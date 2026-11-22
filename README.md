# ParaLives Database

Java 25, Maven, Hibernate ORM 7.4.16.Final, and Microsoft SQL Server project.
Java 25 is the latest Long-Term Support release.

## Package Structure

- `com.paralives.db` - Hibernate session factory
- `com.paralives.db.model` - JPA entities
- `com.paralives.db.service` - Database operations
- `com.paralives.app` - Application entry point and main frame
- `com.paralives.app.presenter` - MVP presenters
- `com.paralives.app.navigation` - Screen navigation contracts
- `com.paralives.app.view.screens` - Individual Swing screen views
- `com.paralives.app.view.overlay` - Dialog and secondary-frame views
- `com.paralives.engine.input` - Dual-player keyboard input and command queues
- `com.paralives.engine.network` - LAN host, client, and serializable input DTO
- `com.paralives.engine.world` - Authoritative game world and network snapshots
- `com.paralives.app.view.components.TileRenderer` - Per-dimension tile-grid colored-shape renderer
- Settings are persisted in `%USERPROFILE%\.paralives\settings.properties`

## Database Setup

1. Start Microsoft SQL Server and ensure it accepts TCP connections on port `1433`.
2. Run `schema.sql` in SQL Server Management Studio or Azure Data Studio.
3. Update the URL, username, and password in `src/main/resources/hibernate.cfg.xml`.
4. Build the project:

   ```sh
   mvn test
   ```

## Run the Swing Interface

Run the application through an IDE main-class launcher, or compile and launch it with Maven dependencies on the runtime classpath.

```sh
mvn compile exec:java
```

Compile and run the Java code only through Maven. Do not use `javac` directly because Maven supplies Hibernate and Jakarta Persistence dependencies; raw `javac` does not.
