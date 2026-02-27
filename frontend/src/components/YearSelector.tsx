interface YearSelectorProps {
  years: number[];
  selectedYear: number;
  disabled: boolean;
  onChange: (year: number) => void;
}

function YearSelector({ years, selectedYear, disabled, onChange }: YearSelectorProps) {
  function handleChange(event: React.ChangeEvent<HTMLSelectElement>) {
    onChange(Number(event.target.value));
  }

  return (
    <div className="year-selector">
      <label htmlFor="year-select">Census Year</label>
      <select
        id="year-select"
        value={selectedYear}
        disabled={disabled}
        onChange={handleChange}
      >
        {years.map((year) => (
          <option key={year} value={year}>
            {year}
          </option>
        ))}
      </select>
    </div>
  );
}

export default YearSelector;
