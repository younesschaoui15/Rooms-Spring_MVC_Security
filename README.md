# Rooms - Spring MVC Security

**Rooms** is a role-based collaboration platform where every application role — Admin, Manager, Team Lead, and others — gets a dedicated space to communicate, coordinate work, and stay aligned on what matters to them.

Built with **Spring MVC**, **Spring Security**, and **Thymeleaf**, Rooms keeps each role's conversations, tasks, and discussions organized in one place instead of scattered across email, chat, and spreadsheets.

## About Rooms

In many organizations, different roles operate in silos. Admins configure the system, managers oversee teams, and individual contributors focus on execution — but they rarely share a single, purpose-built space for their work.

**Rooms** solves this by giving each role its own **Room**: a private, focused environment where members of that role can:

- **Communicate** with peers who share the same responsibilities
- **Manage tasks** relevant to their scope of work
- **Discuss topics** that matter to their role without noise from unrelated channels

Access to each Room is controlled by **role and permissions**. A user only sees and interacts with the Rooms they are authorized to enter. An Admin may have broad visibility across the platform, while a Manager sees their team's Room and any shared spaces they are granted access to.

## Roles & Permissions

Access to rooms, topics, and replies is governed by the user's role.

### Privileged roles (`ADMIN`, `SUPER_ADMIN`, `MODERATOR`)

Users with one of these roles can:

- **View** all rooms
- **Create** new rooms
- **Manage** rooms (update, configure, and remove)
- **Manage** topics and replies across all rooms (create, edit, and delete)

### Standard roles

All other roles (e.g. `PROJECT_MANAGER`, `DEVELOPER`, `TESTER`) have a narrower scope:

- **Create topics** only in rooms dedicated to their own role
- **Reply** only to topics posted by users who share the same role

This keeps each role's discussions focused within its dedicated space while giving privileged roles full platform oversight.

### Planned Features

| Feature | Description |
|---|---|
| **Role-based Rooms** | Dedicated spaces per role (Admin, Manager, etc.) with membership tied to user roles |
| **Permission-aware access** | Fine-grained control over who can read, post, assign tasks, or moderate a Room |
| **Task boards** | Create, assign, track, and complete tasks within each Room |
| **Topic threads** | Organized discussions grouped by subject, with replies and mentions |
| **Real-time messaging** | Live chat within Rooms for quick coordination |
| **Announcements** | Pin important updates at the top of a Room for all members |
| **Activity feed** | Timeline of recent messages, task changes, and new topics in each Room |
| **Cross-room visibility** | Optional shared Rooms for cross-functional collaboration (e.g. Admin + Manager) |
| **Notifications** | Alerts for mentions, task assignments, and unread activity |
| **Audit log** | Track who did what and when for compliance and accountability |

## Tech Stack

| Technology | Version |
|---|---|
| Java | 25 |
| Spring Boot | 4.1.1 |
| Spring MVC | (via `spring-boot-starter-webmvc`) |
| Spring Security | (via `spring-boot-starter-security`) |
| Thymeleaf | (via `spring-boot-starter-thymeleaf`) |
| Thymeleaf Spring Security extras | 6 |
| Build tool | Maven |

## Prerequisites

- **JDK 25** (or a compatible version configured in your environment)
- **Maven 3.9+** (or use the included Maven Wrapper)

## Getting Started

### Clone the repository

```bash
git clone <repository-url>
cd "Rooms - Spring MVC Security"
```

### Run the application

Using the Maven Wrapper (recommended):

```bash
# Windows
.\mvnw.cmd spring-boot:run

# macOS / Linux
./mvnw spring-boot:run
```

Or with a local Maven installation:

```bash
mvn spring-boot:run
```

The application starts on **http://localhost:8080** (see `application.yaml`).

### Build a JAR

```bash
./mvnw clean package
java -jar target/Rooms-0.0.1-SNAPSHOT.jar
```

## Configuration

Application settings are defined in `src/main/resources/application.yaml`:

```yaml
spring:
  application:
    name: Rooms - Spring MVC Security

server:
  port: 8080
```

## Project Structure

```
src/
├── main/
│   ├── java/com/chaoui/rooms/
│   │   └── RoomsSpringMvcSecurityApplication.java   # Application entry point
│   └── resources/
│       └── application.yaml                         # Application configuration
└── test/
    └── java/com/chaoui/rooms/
        └── RoomsSpringMvcSecurityApplicationTests.java
```

## Security

Spring Security is enabled via `SecurityConfig`, with method-level security (`@EnableMethodSecurity`) for role-based authorization on controllers and services.

Role checks enforce the permissions described in [Roles & Permissions](#roles--permissions): privileged roles (`ADMIN`, `SUPER_ADMIN`, `MODERATOR`) manage rooms, topics, and replies platform-wide; standard roles are restricted to their role-dedicated rooms and same-role conversations.

Authentication and authorization rules are defined in `src/main/java/com/chaoui/rooms/configurations/security/SecurityConfig.java`.

## Testing

Run the test suite with:

```bash
./mvnw test
```

The included `RoomsSpringMvcSecurityApplicationTests` verifies that the Spring application context loads successfully.

## Development

[Spring Boot DevTools](https://docs.spring.io/spring-boot/4.1.1/reference/using/devtools.html) is included for faster development cycles (automatic restarts on classpath changes).

## References

- [Securing a Web Application](https://spring.io/guides/gs/securing-web/) — Spring Security guide
- [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/) — MVC and Thymeleaf guide
- [Spring Boot Security Reference](https://docs.spring.io/spring-boot/4.1.1/reference/web/spring-security.html)
- [Thymeleaf with Spring](https://docs.spring.io/spring-boot/4.1.1/reference/web/servlet.html#web.servlet.spring-mvc.template-engines)

## License

This project is in early development. License terms have not been defined yet.
