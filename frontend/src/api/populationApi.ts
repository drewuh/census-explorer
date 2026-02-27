export interface PopulationRow {
  stateName: string;
  population: number;
}

export async function fetchPopulationByYear(year: number): Promise<PopulationRow[]> {
  const response = await fetch(`/api/population/states?year=${year}`);
  if (!response.ok) {
    throw new Error(`HTTP error: ${response.status}`);
  }
  const data: PopulationRow[] = await response.json();
  return data;
}
