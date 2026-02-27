import { PopulationRow } from '../api/populationApi';

interface PopulationTableProps {
  rows: PopulationRow[];
  year: number;
}

function PopulationTable({ rows, year }: PopulationTableProps) {
  return (
    <div>
      <p className="contextual-label">Showing ACS 1-Year Estimates — {year}</p>
      <table className="population-table">
        <thead>
          <tr>
            <th>State</th>
            <th>Population</th>
          </tr>
        </thead>
        <tbody>
          {rows.map((row) => (
            <tr key={row.stateName}>
              <td>{row.stateName}</td>
              <td>{row.population.toLocaleString('en-US')}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default PopulationTable;
