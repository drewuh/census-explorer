# Census Explorer — CLAUDE.md

## Project Overview
A clean, well-designed UI wrapper over the US Census Bureau Data API, 
targeting municipal planners and researchers. Primary purpose is 
portfolio demonstration — code quality and clarity matter more than 
feature velocity.

## Repository Structure
```
census-explorer/
├── backend/          # Spring Boot 3, Java 21, Maven
├── frontend/         # React 18, Vite, TypeScript
├── docs/             # Architecture docs, feature specs
│   ├── architecture/
│   └── features/
├── CLAUDE.md
└── README.md
```

## Stack
- **Backend:** Java 21, Spring Boot 3.x, Maven
- **Frontend:** React 18, Vite, TypeScript
- **Testing:** JUnit 5, Mockito, MockMvc
- **API:** US Census Bureau Data API (free, key required)
- **Editor:** VS Code

## Java Style
- Traditional Java style — no records, no sealed classes, no pattern 
  matching. Keep it readable and familiar.
- No Lombok. Write getters, setters, and constructors explicitly.
- Follow Google Java Style Guide for formatting.
- Meaningful, descriptive variable and method names. Clarity over brevity.
- Every public class and public method gets a Javadoc comment.
- One class per file, always.

## Architecture Principles
- Controllers are thin. No business logic in controllers — ever.
- All business logic lives in the service layer.
- The Census API client is always behind an interface so it can be 
  mocked in tests.
- Never expose raw Census Bureau API response shapes to the frontend. 
  Always map to our own DTOs before returning.
- Never expose the Census API key to the frontend. All Census API calls 
  go through the backend.
- Package structure: com.censusexplorer.{controller, service, client, 
  dto, model, config, exception}

## Testing Philosophy
- Write tests before or alongside implementation — never after.
- Unit test the service layer with mocked dependencies (Mockito).
- Integration test controllers with MockMvc.
- The Census API client interface always has a mock implementation in 
  the test package.
- Target 80%+ line coverage on all service classes.
- Test method naming convention: methodName_scenario_expectedResult()

## Multi-Agent Workflow
When planning or implementing any non-trivial feature, follow this 
sequence. Each agent writes its output to a file before the next begins.

1. **PM Agent** → docs/features/{feature-name}/requirements.md
   - User story, acceptance criteria, edge cases, out of scope

2. **UX Agent** → docs/features/{feature-name}/ux-spec.md
   - User flow, component breakdown, states (loading/error/empty/success)
   - Reads requirements.md before starting

3. **Frontend Agent** → frontend/src/...
   - Implements React components against ux-spec.md
   - Reads both requirements.md and ux-spec.md before starting

4. **Backend Agent** → backend/src/...
   - Implements Spring Boot endpoints against requirements.md
   - Reads requirements.md before starting
   - Writes tests first, then implementation

## Handling Uncertainty
If you are unsure about any decision — architectural, naming, 
dependency, scope — STOP and ask before proceeding. Do not make 
assumptions and continue. Do not document a decision and move on. 
Ask first.

## What NOT To Do
- Do not add any dependency (Maven or npm) without asking first.
- Do not put logic in controllers.
- Do not return raw Census API JSON to the frontend.
- Do not skip writing tests to move faster.
- Do not create more than one public class per file.
- Do not use Lombok.
- Do not use modern Java features (records, sealed classes, var, 
  pattern matching) unless explicitly asked.
- Do not modify existing tests to make them pass — fix the 
  implementation instead.

## Known Issues / Tech Debt
- @MockBean is deprecated in Spring Boot 3.4. Use 
  @MockitoBean (org.springframework.test.context.bean.override
  .mockito.MockitoBean) in all new tests going forward. 
  Existing tests to be migrated when next touched.
