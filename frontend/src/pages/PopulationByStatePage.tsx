import { useState, useEffect } from 'react';
import { PopulationRow, fetchPopulationByYear } from '../api/populationApi';
import YearSelector from '../components/YearSelector';
import PopulationTable from '../components/PopulationTable';
import LoadingIndicator from '../components/LoadingIndicator';
import ErrorMessage from '../components/ErrorMessage';
import EmptyMessage from '../components/EmptyMessage';

const VALID_YEARS: number[] = [
  2024, 2023, 2022, 2021, 2019, 2018, 2017, 2016, 2015,
  2014, 2013, 2012, 2011, 2010, 2009, 2008, 2007, 2006, 2005,
];

type Status = 'loading' | 'success' | 'error' | 'empty';

function PopulationByStatePage() {
  const [selectedYear, setSelectedYear] = useState<number>(2024);
  const [status, setStatus] = useState<Status>('loading');
  const [rows, setRows] = useState<PopulationRow[]>([]);
  const [retryCount, setRetryCount] = useState<number>(0);

  useEffect(() => {
    let cancelled = false;

    setStatus('loading');

    fetchPopulationByYear(selectedYear)
      .then((data) => {
        if (cancelled) {
          return;
        }
        if (data.length === 0) {
          setStatus('empty');
        } else {
          setRows(data);
          setStatus('success');
        }
      })
      .catch(() => {
        if (!cancelled) {
          setStatus('error');
        }
      });

    return () => {
      cancelled = true;
    };
  }, [selectedYear, retryCount]);

  function handleYearChange(year: number) {
    setSelectedYear(year);
  }

  function handleRetry() {
    setRetryCount((prev) => prev + 1);
  }

  return (
    <main className="population-page">
      <h1>Population by State</h1>
      <div className="controls-row">
        <YearSelector
          years={VALID_YEARS}
          selectedYear={selectedYear}
          disabled={status === 'loading'}
          onChange={handleYearChange}
        />
      </div>
      {status === 'loading' && <LoadingIndicator />}
      {status === 'success' && <PopulationTable rows={rows} year={selectedYear} />}
      {status === 'error' && <ErrorMessage onRetry={handleRetry} />}
      {status === 'empty' && <EmptyMessage />}
    </main>
  );
}

export default PopulationByStatePage;
