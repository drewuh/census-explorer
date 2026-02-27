# Feature: Population by State — UX Specification

**UX Agent output. Reads requirements.md — do not modify without re-reading it.**
Last updated: 2026-02-26

---

## 1. Page Layout

The page is a single vertical column, centered on the viewport, with comfortable
horizontal padding on either side.

From top to bottom:

1. **Page heading** — a prominent title: "Population by State".

2. **Controls row** — sits directly below the heading. Contains one element: the year
   selector (label + dropdown). The controls row is always visible regardless of the
   content state below it.

3. **Content area** — occupies the remainder of the page below the controls row.
   Displays exactly one of four states at any time: loading, success, error, or empty.
   The content area never shows more than one state simultaneously.

   - **Loading state:** A brief loading message replaces the table.
   - **Success state:** A contextual label ("Showing ACS 1-Year Estimates — {year}")
     appears immediately above the data table. The label and table together form the
     success view.
   - **Error state:** An error message with a retry button replaces the table.
   - **Empty state:** A no-data message replaces the table. No retry button.

The page has no navigation bar, sidebar, or footer — this is the sole feature of the
application at this stage.

---

## 2. Component Breakdown

### Shared type

```ts
interface PopulationRow {
  stateName: string;   // e.g. "California"
  population: number;  // e.g. 39538223
}
```

This interface is defined in `src/api/` alongside the fetch function and imported by
components that need it.

---

### `PopulationByStatePage`

**File:** `src/pages/PopulationByStatePage.tsx`

**Responsibility:** Top-level page container. Owns all data-fetching state, triggers
fetches on mount and on year change, and decides which content state to render.

**Props:** none — this is the page root.

**State it owns:**

| State field    | Type                                          | Initial value |
|----------------|-----------------------------------------------|---------------|
| `selectedYear` | `number`                                      | `2024`        |
| `status`       | `'loading' \| 'success' \| 'error' \| 'empty'` | `'loading'`   |
| `rows`         | `PopulationRow[]`                             | `[]`          |

**Behavior:**
- On mount, immediately begins fetching data for `selectedYear` (2024). `status` is
  `'loading'` from the initial render — there is no separate idle state.
- When the user changes the year via `YearSelector`, updates `selectedYear`, sets
  `status` back to `'loading'`, and begins a new fetch.
- On a successful non-empty response: sets `rows` and `status = 'success'`.
- On a successful but empty response: sets `status = 'empty'`.
- On a non-2xx or network error: sets `status = 'error'`.
- Passes a `handleRetry` callback to `ErrorMessage` that re-triggers the current
  `selectedYear` fetch without changing the year.
- All API calls are made through a function in `src/api/` (not defined here — backend
  agent defines the endpoint; frontend agent defines the wrapper).

**Renders:**
```
<main>
  <h1>Population by State</h1>
  <YearSelector ... />
  {status === 'loading' && <LoadingIndicator />}
  {status === 'success' && <PopulationTable rows={rows} year={selectedYear} />}
  {status === 'error'   && <ErrorMessage onRetry={handleRetry} />}
  {status === 'empty'   && <EmptyMessage />}
</main>
```

---

### `YearSelector`

**File:** `src/components/YearSelector.tsx`

**Responsibility:** A controlled dropdown that displays the hardcoded list of valid
ACS 1-Year years in descending order and notifies the parent when the user picks a
new year.

**Props:**

| Prop            | Type                       | Description                                    |
|-----------------|----------------------------|------------------------------------------------|
| `years`         | `number[]`                 | Ordered list of valid years (descending)       |
| `selectedYear`  | `number`                   | The currently selected year                    |
| `disabled`      | `boolean`                  | `true` while a fetch is in progress            |
| `onChange`      | `(year: number) => void`   | Called with the newly selected year            |

**State it owns:** none — fully controlled by `PopulationByStatePage`.

**Notes:**
- The `years` array is passed in from the page; the page (or a constants file) holds
  the hardcoded valid-year list `[2024, 2023, 2022, ..., 2006, 2005]` (2020 omitted).
- The `<select>` element's `disabled` attribute is set from the `disabled` prop
  (AC #5, AC #13).
- The label "Census Year" is rendered inside this component, associated with the
  `<select>` via `<label>` and `htmlFor`.

---

### `PopulationTable`

**File:** `src/components/PopulationTable.tsx`

**Responsibility:** Renders the contextual year label and the two-column data table
of 52 geographic entities.

**Props:**

| Prop    | Type              | Description                                |
|---------|-------------------|--------------------------------------------|
| `rows`  | `PopulationRow[]` | The 52 rows to display, sorted by backend  |
| `year`  | `number`          | The year to display in the contextual label|

**State it owns:** none.

**Notes:**
- Renders the label "Showing ACS 1-Year Estimates — {year}" above the table (AC #11).
- Table has two columns: **State** (left) and **Population** (right) (AC #6).
- State names rendered as-is from `row.stateName` (AC #8).
- Population rendered as a locale-formatted integer: `row.population.toLocaleString('en-US')`
  produces `"39,538,223"` (AC #9).
- The data arrives pre-sorted alphabetically from the backend (AC #10). This component
  does not sort — it renders in the order received.
- Column headers are plain text, not interactive (sortable columns are out of scope).

---

### `LoadingIndicator`

**File:** `src/components/LoadingIndicator.tsx`

**Responsibility:** Displayed in the content area while a fetch is in progress.

**Props:** none.

**State it owns:** none.

**Notes:**
- Renders the text "Loading…" (AC #12).
- No spinner or animation is specified — implementation detail left to styling.

---

### `ErrorMessage`

**File:** `src/components/ErrorMessage.tsx`

**Responsibility:** Displayed when the backend returns a non-2xx response. Shows the
error copy and a retry button.

**Props:**

| Prop       | Type         | Description                                              |
|------------|--------------|----------------------------------------------------------|
| `onRetry`  | `() => void` | Called when the user activates the retry button (AC #15) |

**State it owns:** none.

**Notes:**
- Renders the message "Unable to load population data. Please try again." (AC #14).
- Renders a "Try again" button that calls `onRetry` when clicked (AC #15).
- Does not display any raw error detail — no status codes, no stack traces (AC #17).

---

### `EmptyMessage`

**File:** `src/components/EmptyMessage.tsx`

**Responsibility:** Displayed when the backend returns a 200 with an empty data array.

**Props:** none.

**State it owns:** none.

**Notes:**
- Renders the message "No population data is available for the selected year." (AC #18).
- No retry button (AC #19).

---

## 3. User Flow

### Flow A — Page load → successful data fetch

1. The user navigates to the Population by State page.
2. The page renders immediately with:
   - Heading: "Population by State"
   - `YearSelector` showing "2024", **disabled**
   - Content area: "Loading…"
3. The fetch completes successfully with 52 rows.
4. The page updates:
   - `YearSelector` **re-enabled**
   - Content area: label "Showing ACS 1-Year Estimates — 2024" + table of 52 rows
     sorted alphabetically, with population figures formatted with commas.

### Flow B — User changes year

1. From the success state (Flow A step 4), the user opens the dropdown and selects
   "2018".
2. Immediately:
   - `YearSelector` shows "2018", becomes **disabled**
   - Table and label are replaced by "Loading…"
3. The fetch completes successfully with 52 rows for 2018.
4. The page updates:
   - `YearSelector` **re-enabled**
   - Label changes to "Showing ACS 1-Year Estimates — 2018"
   - Table shows 2018 data

### Flow C — Error on fetch

1. From any state where a fetch is triggered, the backend returns a non-2xx response
   (or the network request fails).
2. The page updates:
   - `YearSelector` **re-enabled**
   - Content area: "Unable to load population data. Please try again." + "Try again"
     button
3. The user clicks "Try again".
4. Page behaves as if the same year were re-selected: `YearSelector` disabled,
   "Loading…" shown, fetch re-submitted for the same year.
5. On success, Flow A step 4 applies.

### Flow D — Empty response

1. A fetch completes with a 200 response containing an empty data array.
2. The page updates:
   - `YearSelector` **re-enabled**
   - Content area: "No population data is available for the selected year."
   - No retry button
3. The user may select a different year from the dropdown, which triggers a new fetch
   (Flow A step 2 onward).

---

## 4. UI States per Component

### `YearSelector`

| State    | Condition                        | Behavior                            |
|----------|----------------------------------|-------------------------------------|
| Enabled  | `status` is `success`, `error`, or `empty` | User can open and change the year |
| Disabled | `status` is `loading`            | Dropdown is non-interactive; selected year still visible |

### `PopulationByStatePage` content area

| State   | Condition                              | Component rendered  |
|---------|----------------------------------------|---------------------|
| Loading | `status === 'loading'`                 | `<LoadingIndicator>` |
| Success | `status === 'success'`                 | `<PopulationTable>` |
| Error   | `status === 'error'`                   | `<ErrorMessage>`    |
| Empty   | `status === 'empty'`                   | `<EmptyMessage>`    |

### `ErrorMessage`

| State   | Condition   | Behavior                                    |
|---------|-------------|---------------------------------------------|
| Default | Always      | Displays message and "Try again" button     |

### `LoadingIndicator`, `EmptyMessage`

Single state only — rendered or not rendered. No internal state variation.

### `PopulationTable`

Single state only — always renders the label and table. Empty-array protection is
handled upstream by `PopulationByStatePage` (empty response routes to `EmptyMessage`,
not `PopulationTable`).

---

## 5. Copy (Exact Strings)

All user-facing strings, exactly as they must appear in the UI. These are locked to
the acceptance criteria in requirements.md and must not be changed without updating
requirements.md first.

| Location                        | String                                                        | AC ref |
|---------------------------------|---------------------------------------------------------------|--------|
| Page heading                    | `Population by State`                                         | —      |
| Year selector label             | `Census Year`                                                 | AC #1  |
| Content area — loading          | `Loading…`                                                    | AC #12 |
| Table contextual label          | `Showing ACS 1-Year Estimates — {year}` (year substituted in)| AC #11 |
| Table column header — left      | `State`                                                       | AC #6  |
| Table column header — right     | `Population`                                                  | AC #6  |
| Error message                   | `Unable to load population data. Please try again.`           | AC #14 |
| Retry button                    | `Try again`                                                   | AC #15 |
| Empty message                   | `No population data is available for the selected year.`      | AC #18 |

**Note on the loading string:** The requirements specify "a loading indicator" (AC #12)
but do not prescribe the exact text. `Loading…` (with a typographic ellipsis `…`, not
three periods `...`) is the chosen copy. If this changes, only this spec needs updating.
