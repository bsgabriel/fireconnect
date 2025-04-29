# Spring Firebase Integration Framework

_A simple framework to help developers in integrating Firebase Firestore with Spring Boot._

---

## How Does It Work?

This framework simplifies the interaction between your Spring Boot application and Google Firebase Firestore by providing:

- **Configuration Management**: It initializes the Firebase connection automatically on application startup using a service account JSON file.
- **Abstract Firestore Service**: A ready-to-extend abstract service class providing basic CRUD operations (`add`, `getAll`, `findById`, `update`, and `delete`) with Firestore.
- **Abstract Firestore Entity**: A base entity class that ensures every Firestore document has a managed `id` field.

All you need to do is provide your entity classes and extend the `AbstractFirestoreService` to define your collections and use the out-of-the-box methods.

---

## How to Use

### 1. Add your `serviceAccountKey.json`

Place your Firebase service account credentials file inside the `resources` directory:

```
src/main/resources/serviceAccountKey.json
```

Make sure it contains the required credentials to access your Firestore database.

---

### 2. Enable Firebase Configuration

Ensure the `FirebaseConfig` class is included in your project. It will initialize Firebase automatically when the application starts:

```java
@SpringBootApplication
@Import(FirebaseConfig.class)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
```

---

### 3. Create Your Entity Class

Extend the `AbstractFirestoreEntity` to create your entities:

```java
public class Book extends AbstractFirestoreEntity {
    private String title;
    private String author;
    private String publisher;
    private Integer year;
    private String imageBook;
    private Integer stockQuantity;
}
```

The `id` field will be automatically managed based on the Firestore document ID.

---

### 4. Create Your Service

Extend the `AbstractFirestoreService<T>` and implement `getCollectionName()`:

```java
@Service
public class BookFirestoreService extends AbstractFirestoreService<Book> {

    @Override
    protected String getCollectionName() {
        return "books"; // Your Firestore collection name
    }
}
```

---

### 5. Use the Service

Now you can inject and use your service in your controllers or components:

```java
@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookFirestoreService bookFirestoreService;

    @PostMapping
    public ResponseEntity<Book> saveBook(@RequestBody Book book) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.bookFirestoreService.save(book));
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(this.bookFirestoreService.getAll());
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<Void> deleteBook(@PathVariable String bookId) {
        this.bookFirestoreService.delete(bookId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{bookId}")
    public ResponseEntity<Void> updateBook(@PathVariable String bookId, @RequestBody Book book) {
        this.bookFirestoreService.update(bookId, book);
        return ResponseEntity.status(HttpStatus.OK ).build();
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<Book> searchBook(@PathVariable String bookId) {
        return ResponseEntity.status(HttpStatus.OK).body(this.bookFirestoreService.search(bookId));
    }
}
```

## Example Project

You can check out a full working example of a Spring Boot application using this framework:

- **Without the framework**: [`main` branch](https://github.com/bsgabriel/pds-biblioteca/tree/main)
- **With the framework integrated**: [`fireconnect` branch](https://github.com/bsgabriel/pds-biblioteca/tree/fireconnect)

---

## Requirements

- Java 17+ (recommended)
- Spring Boot 3.x
- Firebase Admin SDK dependency
- A valid `serviceAccountKey.json` for Firebase access

---

## Dependencies

Make sure to include the following Maven dependencies in your `pom.xml`:

```xml
<dependency>
    <groupId>com.google.firebase</groupId>
    <artifactId>firebase-admin</artifactId>
    <version>9.2.0</version>
</dependency>
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
</dependency>
```

---

## Important Notes

- **Exception Handling**: Custom `FirestoreExecuteException` is thrown for any Firestore operation failures.
- **Object Mapping**: All Firestore documents are serialized/deserialized using Jackson's `ObjectMapper`.

