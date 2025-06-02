# Receptoria

Receptoria is a social network for culinary content creators to share their favorite recipes, inspire others, and connect with like-minded food enthusiasts. Easily upload dish photos, add step-by-step instructions, and receive feedback from the community. Create your own cooking profile, grow your brand, and find new fans of flavor!

## Technology Stack

- **Language & Framework**  
  - Java 17  
  - Spring Boot 3.4.4 (Starters: Web, Data JPA, Security, Validation, Mail)  
  - Spring Security (JWT integration via `io.jsonwebtoken:jjwt`)  

- **ORM & Database**  
  - Spring Data JPA (Hibernate 6.x)  
  - PostgreSQL 14+ (Driver: `org.postgresql:postgresql`)  

- **Mapping & Boilerplate Reduction**  
  - MapStruct 1.5.5.Final (`org.mapstruct:mapstruct`, `mapstruct-processor`)  
  - Lombok 1.18.38 (`org.projectlombok:lombok`)  

- **Build & Dependency Management**  
  - Maven 3.8+  
  - `spring-boot-maven-plugin` for packaging/execution  

- **Validation & Testing**  
  - Bean Validation (JSR-380) via `spring-boot-starter-validation`  
  - JUnit 5, Mockito, Spring Test (`spring-boot-starter-test`, `spring-security-test`)  

## Design and Programming Patterns Used

- **Monolithic Architecture**  
  The application is implemented as a monolithic Spring Boot application, with all modules (controllers, services, repositories, etc.) packaged and deployed as a single unit.

- **Result Pattern**  
  Instead of returning a raw value, methods return a `Result<T>` object that encapsulates success, failure, and related data/cases.

- **Builder Pattern (via Lombok)**  
  Entities and DTOs are constructed using Lombok’s `@Builder` annotation for a clean, immutable–style instantiation.

- **DTO Pattern**  
  Data Transfer Objects (DTOs) are used to decouple internal JPA entities from API schemas. DTOs encapsulate only the necessary fields for requests/responses, and mapping is handled (e.g., via MapStruct or manual converters).

- **Adapter Pattern**  
  An adapter layer abstracts over different delivery channels (e.g., email, SMS), allowing the system to select and switch channels without modifying core logic.

- **Repository Pattern**  
  Data access is separated into repository interfaces and implementations, with a distinct service layer handling business logic.

- **MVC (Model–View–Controller)**  
  The application follows an MVC structure:  
  - **Model**: JPA entities and DTOs  
  - **View**: REST controllers (JSON input/output)  
  - **Controller**: Service layer orchestrating flows between repositories and controllers

- **SOLID Principles**  
  The codebase adheres to SOLID principles:  
  1. **Single Responsibility**: Each class or service has one clear responsibility.  
  2. **Open/Closed**: Components are extensible via interfaces/abstractions, without modifying existing code.  
  3. **Liskov Substitution**: Subtypes (e.g., different delivery-channel adapters) replace their abstractions without breaking behavior.  
  4. **Interface Segregation**: Fine-grained interfaces (e.g., separate `NotificationSender` and `DeliveryChannelSelector`) avoid forcing clients to depend on unused methods.  
  5. **Dependency Inversion**: High-level modules depend on abstractions (interfaces), not concrete implementations, with Spring injecting required beans.


## Features

- **User Authentication & Authorization**  
  - Sign up / Log in (username, email, password)  
  - Role-based access control (e.g., ROLE_USER, ROLE_ADMIN)  
  - JWT-based stateless sessions (`io.jsonwebtoken:jjwt-api/impl/jackson`)  

- **Recipe Management (CRUD)**  
  - Create, read, update, and delete recipes  
  - Each recipe includes:  
    - Title, description, tags (e.g., “vegan,” “dessert”)  
    - Ingredients list (quantity, unit, ingredient)  
    - Step-by-step instructions  
    - Photo uploads (stored on filesystem or cloud storage)  

- **Social Interactions**  
  - Follow/unfollow other users  
  - Like and bookmark (“save”) recipes  
  - Comment on recipes (with `@mentions`)  
  - “Trending” feed for most-liked or most-viewed recipes  

- **Search & Discovery**  
  - Search by keyword, tag, or ingredient  
  - Filter by category or dietary preference  
  - Paginated endpoints for scalable browsing  

- **Notifications & Email**  
  - In-app notifications (Spring WebSocket optional)  
  - Email notifications via `spring-boot-starter-mail`  

- **Data Mapping & Validation**  
  - DTO-to-entity mapping via MapStruct (`org.mapstruct:mapstruct`)  
  - Bean validation using `spring-boot-starter-validation` (Hibernate Validator)  

- **Security & Testing**  
  - Password hashing and authentication via `spring-boot-starter-security`  
  - Integration/unit tests with `spring-boot-starter-test` and `spring-security-test`  

## Authors

- [@keshaParrot](https://github.com/keshaParrot)
- [@yarekza](https://github.com/yarekza)
- [@Dewarsss](https://github.com/Dewarsss)

## License

[MIT](https://choosealicense.com/licenses/mit/)
