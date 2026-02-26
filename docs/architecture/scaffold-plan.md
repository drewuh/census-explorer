# Scaffold Plan — Census Explorer

## Context

The repository currently contains only `CLAUDE.md`, `README.md`, and `.gitignore` — no
backend, frontend, or docs directories exist. This plan creates the complete project
scaffold as specified in `CLAUDE.md`: a Spring Boot 3 Maven backend, a React + Vite +
TypeScript frontend, a docs directory, and `.gitignore` updates.

---

## 1. Backend — `backend/`

### 1.1 Directory Layout

```
backend/
├── pom.xml
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/censusexplorer/
    │   │       ├── CensusExplorerApplication.java
    │   │       ├── client/
    │   │       │   ├── CensusApiClient.java        (interface)
    │   │       │   ├── CensusApiResponse.java      (placeholder POJO)
    │   │       │   └── CensusApiClientImpl.java    (stub @Component)
    │   │       ├── config/                          (empty — placeholder package)
    │   │       ├── controller/                      (empty — placeholder package)
    │   │       ├── dto/
    │   │       │   └── PopulationDto.java           (placeholder POJO)
    │   │       ├── exception/                       (empty — placeholder package)
    │   │       ├── model/                           (empty — placeholder package)
    │   │       └── service/
    │   │           └── PopulationService.java       (@Service with one stub method)
    │   └── resources/
    │       └── application.properties
    └── test/
        └── java/
            └── com/censusexplorer/
                ├── CensusExplorerApplicationTests.java
                └── client/
                    └── MockCensusApiClient.java
```

Empty placeholder packages (`config/`, `controller/`, `exception/`, `model/`) are
represented in Git via a `.gitkeep` file in each directory.

### 1.2 `pom.xml`

- **Parent:** `spring-boot-starter-parent` version **3.4.13**
- **Java source/target:** 21
- **groupId:** `com.censusexplorer`
- **artifactId:** `census-explorer`
- **version:** `0.1.0-SNAPSHOT`

| Dependency | Scope | Rationale |
|---|---|---|
| `spring-boot-starter-web` | compile | REST controllers + embedded Tomcat |
| `spring-boot-starter-webflux` | compile | `WebClient` for outbound Census API HTTP calls |
| `spring-boot-starter-test` | test | JUnit 5, Mockito, MockMvc (bundled by Spring Boot) |

Build plugin: `spring-boot-maven-plugin` (standard Spring Boot packaging).

### 1.3 Source Files

**`CensusExplorerApplication.java`** — `com.censusexplorer`
Standard `@SpringBootApplication` entry point. Javadoc on the class.

**`CensusApiClient.java`** — `com.censusexplorer.client`
Interface with one stub method signature:
```java
List<CensusApiResponse> fetchPopulationData(String year, String geography);
```

**`CensusApiResponse.java`** — `com.censusexplorer.client`
Placeholder POJO representing a single row returned by the Census Bureau API. No fields
yet — just the class skeleton with a Javadoc comment. Fields are added when the first
feature is implemented.

**`CensusApiClientImpl.java`** — `com.censusexplorer.client`
`@Component` implementation of `CensusApiClient`. Has a `WebClient` field (injected via
constructor). The stub method body throws `UnsupportedOperationException("Not yet
implemented")`. Javadoc on class and method.

**`PopulationService.java`** — `com.censusexplorer.service`
`@Service`. Constructor-injects `CensusApiClient`. Single stub method:
```java
public List<PopulationDto> getPopulationByState(String year)
```
Body throws `UnsupportedOperationException("Not yet implemented")`. Javadoc on class
and method.

**`PopulationDto.java`** — `com.censusexplorer.dto`
Placeholder POJO with a Javadoc comment. No fields yet — just the class skeleton with
a no-arg constructor. (Fields are added when the first feature populates it.)

**`application.properties`** — `src/main/resources`
```
census.api.key=REPLACE_ME
```

### 1.4 Test Files

**`CensusExplorerApplicationTests.java`** — `com.censusexplorer`
Standard Spring Boot context-loads smoke test: one `@SpringBootTest` test that verifies
the application context starts without errors.

**`MockCensusApiClient.java`** — `com.censusexplorer.client` (test source tree)
Implements `CensusApiClient`. All methods return empty stubs (`Collections.emptyList()`).
Used by service unit tests to avoid needing the real HTTP client.

### 1.5 Style Constraints (from CLAUDE.md)

- No Lombok — explicit constructors, getters, setters where fields exist
- No modern Java features (`var`, records, sealed, pattern matching)
- Every public class and public method gets a Javadoc comment
- Google Java Style Guide formatting
- One class per file

---

## 2. Frontend — `frontend/`

### 2.1 Directory Layout

Standard Vite React-TypeScript template, plus three additional source folders:

```
frontend/
├── index.html
├── package.json
├── tsconfig.json
├── tsconfig.node.json
├── vite.config.ts
└── src/
    ├── main.tsx
    ├── App.tsx
    ├── App.css
    ├── index.css
    ├── vite-env.d.ts
    ├── api/            <- backend API call wrappers (.gitkeep placeholder)
    ├── components/     <- reusable UI components (.gitkeep placeholder)
    └── pages/          <- page-level components (.gitkeep placeholder)
```

### 2.2 `package.json` Dependencies

Exactly the packages from the standard Vite `react-ts` template — no additional packages
without prior approval per CLAUDE.md.

| Package | Role |
|---|---|
| `react` | Core library |
| `react-dom` | DOM renderer |
| `@types/react` | TypeScript types |
| `@types/react-dom` | TypeScript types |
| `@vitejs/plugin-react` | Vite plugin for React JSX transform |
| `typescript` | TypeScript compiler |
| `vite` | Build tool / dev server |

---

## 3. `.gitignore` Additions

Appended to the existing `.gitignore` (which covers Java/Maven only):

```
# Frontend
node_modules/
dist/
.env.local
```

---

## 4. Verification

1. `cd backend && mvn compile` — exits 0
2. `cd backend && mvn test` — context-loads test passes
3. `cd frontend && npm install` — no errors
4. `cd frontend && npm run build` — Vite build succeeds
5. `git status` after frontend build — `node_modules/` and `dist/` are ignored
