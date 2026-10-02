import React, { useEffect, useState } from 'react';
import { fetchStations } from '../api/stationsApi';
import { Building2, Search, RefreshCw, ChevronLeft, ChevronRight, AlertCircle, Layers } from 'lucide-react';

export const StationsPage = () => {
  const [stations, setStations] = useState([]);
  const [pagination, setPagination] = useState({
    page: 0,
    size: 10,
    totalElements: 0,
    totalPages: 0,
    first: true,
    last: true,
  });
  const [search, setSearch] = useState('');
  const [debouncedSearch, setDebouncedSearch] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // Debounce search input
  useEffect(() => {
    const handler = setTimeout(() => {
      setDebouncedSearch(search);
      setPagination((prev) => ({ ...prev, page: 0 }));
    }, 300);
    return () => clearTimeout(handler);
  }, [search]);

  const loadStations = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await fetchStations({
        page: pagination.page,
        size: pagination.size,
        search: debouncedSearch,
      });

      if (response.success && response.data) {
        setStations(response.data.items || []);
        if (response.data.pagination) {
          setPagination((prev) => ({
            ...prev,
            ...response.data.pagination,
          }));
        }
      }
    } catch (err) {
      setError(err.message || 'Failed to fetch stations from backend server');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadStations();
  }, [pagination.page, debouncedSearch]);

  const handlePageChange = (newPage) => {
    if (newPage >= 0 && newPage < pagination.totalPages) {
      setPagination((prev) => ({ ...prev, page: newPage }));
    }
  };

  return (
    <div className="space-y-6 max-w-7xl mx-auto">
      {/* Header */}
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4 pb-6 border-b border-slate-800">
        <div>
          <div className="flex items-center gap-2 text-xs font-mono text-cyan-400 mb-1">
            <Building2 className="h-4 w-4" />
            <span>OPERATIONS DIRECTORY</span>
          </div>
          <h1 className="text-2xl font-bold text-white tracking-tight">Railway Station Master Directory</h1>
          <p className="text-xs text-slate-400 mt-1">
            Synthetic station infrastructure data powering RailSync domain services and algorithm input models.
          </p>
        </div>

        <div className="flex items-center gap-3">
          <button
            onClick={loadStations}
            disabled={loading}
            className="p-2.5 rounded-lg bg-slate-900 border border-slate-800 hover:border-slate-700 text-slate-300 transition flex items-center gap-2 text-xs font-medium"
          >
            <RefreshCw className={`h-4 w-4 ${loading ? 'animate-spin text-cyan-400' : ''}`} />
            Refresh
          </button>
        </div>
      </div>

      {/* Filter and Search Bar */}
      <div className="p-4 rounded-xl bg-slate-900/60 border border-slate-800 flex flex-col sm:flex-row items-center justify-between gap-4">
        <div className="relative w-full sm:w-80">
          <Search className="h-4 w-4 absolute left-3 top-1/2 -translate-y-1/2 text-slate-500" />
          <input
            type="text"
            placeholder="Search by code, name, city, state..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            className="w-full pl-9 pr-4 py-2 bg-slate-950 border border-slate-800 rounded-lg text-xs text-slate-200 placeholder-slate-500 focus:outline-none focus:border-cyan-500/50 font-mono transition"
          />
        </div>

        <div className="text-xs font-mono text-slate-400 flex items-center gap-2">
          <Layers className="h-4 w-4 text-cyan-400" />
          <span>Total Records:</span>
          <span className="text-white font-semibold">{pagination.totalElements}</span>
        </div>
      </div>

      {/* Error Alert */}
      {error && (
        <div className="p-4 rounded-xl bg-red-500/10 border border-red-500/30 text-red-400 text-xs flex items-center gap-3">
          <AlertCircle className="h-5 w-5 shrink-0" />
          <div>
            <span className="font-semibold">Backend Error:</span> {error}
          </div>
        </div>
      )}

      {/* Station Table */}
      <div className="bg-slate-900/40 border border-slate-800/80 rounded-xl overflow-hidden shadow-xl">
        <div className="overflow-x-auto">
          <table className="w-full text-left border-collapse">
            <thead>
              <tr className="bg-slate-900/90 border-b border-slate-800 text-[11px] font-mono uppercase tracking-wider text-slate-400">
                <th className="py-3.5 px-6 font-semibold">Station Code</th>
                <th className="py-3.5 px-6 font-semibold">Station Name</th>
                <th className="py-3.5 px-6 font-semibold">City</th>
                <th className="py-3.5 px-6 font-semibold">State</th>
                <th className="py-3.5 px-6 font-semibold text-center">Platforms</th>
                <th className="py-3.5 px-6 font-semibold text-right">Status</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-800/60 text-xs">
              {loading ? (
                Array.from({ length: 5 }).map((_, idx) => (
                  <tr key={idx} className="animate-pulse">
                    <td className="py-4 px-6"><div className="h-4 bg-slate-800/60 rounded w-16"></div></td>
                    <td className="py-4 px-6"><div className="h-4 bg-slate-800/60 rounded w-48"></div></td>
                    <td className="py-4 px-6"><div className="h-4 bg-slate-800/60 rounded w-28"></div></td>
                    <td className="py-4 px-6"><div className="h-4 bg-slate-800/60 rounded w-32"></div></td>
                    <td className="py-4 px-6 text-center"><div className="h-4 bg-slate-800/60 rounded w-8 mx-auto"></div></td>
                    <td className="py-4 px-6 text-right"><div className="h-4 bg-slate-800/60 rounded w-16 ml-auto"></div></td>
                  </tr>
                ))
              ) : stations.length === 0 ? (
                <tr>
                  <td colSpan="6" className="py-12 text-center text-slate-500 font-mono text-xs">
                    No station records match search query "{debouncedSearch}"
                  </td>
                </tr>
              ) : (
                stations.map((st) => (
                  <tr key={st.id} className="hover:bg-slate-800/30 transition">
                    <td className="py-4 px-6 font-mono font-bold text-cyan-400">{st.stationCode}</td>
                    <td className="py-4 px-6 font-medium text-slate-100">{st.name}</td>
                    <td className="py-4 px-6 text-slate-300">{st.city}</td>
                    <td className="py-4 px-6 text-slate-400">{st.state}</td>
                    <td className="py-4 px-6 text-center font-mono text-slate-200">
                      <span className="px-2 py-0.5 rounded bg-slate-800 border border-slate-700">
                        {st.platformCount}
                      </span>
                    </td>
                    <td className="py-4 px-6 text-right">
                      <span
                        className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-[10px] font-mono font-medium ${
                          st.status === 'ACTIVE'
                            ? 'bg-emerald-500/10 text-emerald-400 border border-emerald-500/30'
                            : 'bg-amber-500/10 text-amber-400 border border-amber-500/30'
                        }`}
                      >
                        {st.status}
                      </span>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>

        {/* Pagination Footer */}
        <div className="px-6 py-4 bg-slate-900/90 border-t border-slate-800 flex flex-col sm:flex-row items-center justify-between gap-4 text-xs font-mono">
          <span className="text-slate-400">
            Page <span className="text-white font-bold">{pagination.page + 1}</span> of{' '}
            <span className="text-white font-bold">{pagination.totalPages || 1}</span>
          </span>

          <div className="flex items-center gap-2">
            <button
              onClick={() => handlePageChange(pagination.page - 1)}
              disabled={pagination.first || loading}
              className="p-1.5 rounded-lg bg-slate-950 border border-slate-800 text-slate-300 disabled:opacity-40 disabled:cursor-not-allowed hover:border-slate-700 transition"
            >
              <ChevronLeft className="h-4 w-4" />
            </button>
            <button
              onClick={() => handlePageChange(pagination.page + 1)}
              disabled={pagination.last || loading}
              className="p-1.5 rounded-lg bg-slate-950 border border-slate-800 text-slate-300 disabled:opacity-40 disabled:cursor-not-allowed hover:border-slate-700 transition"
            >
              <ChevronRight className="h-4 w-4" />
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};
