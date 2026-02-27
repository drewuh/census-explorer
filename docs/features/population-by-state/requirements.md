# Feature: Population by State — Requirements

**PM Agent output. Do not modify without updating the version note below.**
Last updated: 2026-02-26 (open questions resolved; see section 4)

---

## 1. User Story

As a municipal planner or researcher, I want to select a census year and see a table
of all US states with their total population figures, so that I can quickly compare
state-level population data without navigating the Census Bureau's raw API.

---

## 2. Acceptance Criteria

### Year Selection

1. The UI presents a dropdown (or equivalent selector) listing every valid ACS 1-Year
   Estimate year in descending order (most recent first).
2. Valid years are **2005 through 2024, inclusive, excluding 2020**. The year 2020 is
   excluded because the Census Bureau did not release standard ACS 1-Year estimates for
   that year due to COVID-19 data collection disruptions. Inclusion of 2024 is confirmed:
   the ACS 1-Year 2024 dataset is marked available at `api.census.gov` (`c_isAvailable:
   true`, `temporal: 2024/2024`). The valid year list is **hardcoded in the backend**;
   adding future years requires a code change. Dynamic discovery of available years is
   a future improvement, not in scope here.
3. The selector defaults to the most recent valid year (2024) on first load.
4. Selecting a year immediately triggers a data fetch; the user does not press a
   separate "Go" button.
5. The selector is disabled while a fetch is in progress so the user cannot submit
   overlapping requests.

### Data Display — Success State

6. On a successful response, the UI displays a table with exactly two columns:
   **State** and **Population**.
7. The table contains one row for each of the **52 geographic entities** returned by
   the API: 50 states, the District of Columbia, and Puerto Rico. No server-side
   filtering is applied; all entries the Census API returns are displayed.
8. State names are displayed exactly as returned by the Census Bureau `NAME` field
   (e.g., "California", "New York").
9. Population figures are displayed as whole numbers formatted with thousands separators
   (e.g., `39,538,223`). No decimal places, no currency symbol.
10. The table is sorted alphabetically by state name, ascending, by default.
11. The selected year is shown as a visible label near the table (e.g., "Showing ACS
    1-Year Estimates — 2022") so the user always knows which year they are viewing.

### Loading State

12. From the moment a year is selected (or the page first loads) until the response
    arrives, a loading indicator is displayed in place of the table.
13. The year selector remains visible but disabled during loading.

### Error State

14. If the backend returns a non-2xx HTTP response, the table is replaced with a
    user-facing error message: **"Unable to load population data. Please try again."**
15. The error message includes a retry action (button or link) that re-submits the
    same year request.
16. The year selector is re-enabled after an error so the user can try a different year.
17. The raw error detail (stack trace, HTTP status code, Census API message) is never
    shown to the user.

### Empty / No-Data State

18. If the backend returns a 200 response with an empty data array, the table is
    replaced with the message: **"No population data is available for the selected
    year."**
19. This state is distinct from the error state — no retry button is shown, but the
    year selector remains enabled so the user can choose another year.

### Census API Details (for backend implementation)

20. Population data is sourced from the **ACS 1-Year Estimates** dataset.
    - Dataset path: `acs/acs1`
    - Variable: `B01003_001E` (Estimate — Total Population)
    - Geography: `for=state:*`
    - Full example URL:
      `https://api.census.gov/data/{year}/acs/acs1?get=NAME,B01003_001E&for=state:*&key={API_KEY}`
21. The backend must never forward the raw Census API JSON array-of-arrays response
    to the frontend. It must parse and map the response to a list of our own DTOs
    before returning.
22. The Census API key must remain server-side only and must never appear in any
    frontend code, network response body, or browser-visible header.

---

## 3. Out of Scope

- Sortable columns — clicking a column header to sort by population (ascending/
  descending) is explicitly deferred to a follow-up feature.
- Filtering the table.
- Any data visualization (charts, maps, graphs).
- Comparing two or more years side by side.
- Sub-state geographies (counties, congressional districts, metro areas, etc.).
- Any Census variable other than `B01003_001E` (total population).
- Data export (CSV download, PDF, copy-to-clipboard, etc.).
- Pagination of the table (50–52 rows is acceptable without pagination).
- Caching of Census API responses.
- User accounts, saved queries, or history.

---

## 4. Resolved Decisions

All open questions from the initial draft have been answered. No open questions remain.

| # | Question | Decision |
|---|---|---|
| 1 | DC and Puerto Rico inclusion | **Include all 52** entries returned by `state:*` (50 states + DC + PR). No filtering. |
| 2 | 2024 data availability | **Include 2024.** Confirmed available at `api.census.gov`. Valid range is 2005–2024 (excluding 2020). |
| 3 | Year range strategy | **Hardcode** the valid year list in the backend. Dynamic discovery is a future improvement. |
| 4 | Sortable columns | **Explicitly out of scope.** Deferred to a follow-up feature. |
