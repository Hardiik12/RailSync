import React, { useEffect, useState } from 'react';
import { fetchStations } from '../api/stationsApi';
import { Building2, Search, RefreshCw, ChevronLeft, ChevronRight, AlertCircle, Layers } from 'lucide-react';
import { DataOriginBadge } from '../components/common/DataOriginBadge';

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
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4 pb-5 border-b border-[#263449]">
        <div>
          <div className="flex items-center gap-2 text-xs font-mono text-teal-400 mb-1">
            <Building2 className="h-4 w-4" />
            <span>RAILWAY OPERATIONS</span>
          </div>
          <h1 className="text-2xl font-bold text-[#F1F5F9] tracking-tight">Station Directory</h1>
          <p className="text-xs text-[#94A3B8] mt-1">
            Public and synthetic railway station records powering domain services and algorithm inputs.
          </p>
        </div>

        <div className="flex items-center gap-3">
          <button
            onClick={loadStations}
            disabled={loading}
            className="p-2.5 rounded-lg bg-[#111827] border border-[#263449] hover:bg-[#172033] text-[#F1F5F9] transition flex items-center gap-2 text-xs font-medium"
          >
            <RefreshCw className={`h-4 w-4 ${loading ? 'animate-spin text-teal-400' : ''}`} />
            Refresh
          </button>
        </div>
      </div>

      {/* Filter and Search Bar */}
      <div className="p-4 rounded-xl bg-[#111827] border border-[#263449] flex flex-col sm:flex-row items-center justify-between gap-4">
        <div className="relative w-full sm:w-80">
          <Search className="h-4 w-4 absolute left-3 top-1/2 -translate-y-1/2 text-[#94A3B8]" />
          <input
            type="text"
            placeholder="Search by code, name, city, state..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            className="w-full pl-9 pr-4 py-2 bg-[#0B1220] border border-[#263449] rounded-lg text-xs text-[#F1F5F9] placeholder-[#94A3B8] focus:outline-none focus:border-teal-500/50 font-mono transition"
          />
        </div>

        <div className="text-xs font-mono text-[#94A3B8] flex items-center gap-2">
          <Layers className="h-4 w-4 text-teal-400" />
          <span>Total Records:</span>
          <span className="text-[#F1F5F9] font-semibold">{pagination.totalElements}</span>
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
      <div className="bg-[#111827] border border-[#263449] rounded-xl overflow-hidden shadow-sm">
        <div className="overflow-x-auto">
          <table className="w-full text-left border-collapse">
            <thead>
              <tr className="bg-[#172033] border-b border-[#263449] text-[11px] font-mono uppercase tracking-wider text-[#94A3B8]">
                <th className="py-3.5 px-6 font-semibold">Station Code</th>
                <th className="py-3.5 px-6 font-semibold">Station Name</th>
                <th className="py-3.5 px-6 font-semibold">City</th>
                <th className="py-3.5 px-6 font-semibold">State</th>
                <th className="py-3.5 px-6 font-semibold text-center">Platforms</th>
                <th className="py-3.5 px-6 font-semibold text-center">Data Origin</th>
                <th className="py-3.5 px-6 font-semibold text-right">Status</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-[#263449] text-xs">
              {loading ? (
                Array.from({ length: 5 }).map((_, idx) => (
                  <tr key={idx} className="animate-pulse">
                    <td className="py-4 px-6"><div className="h-4 bg-[#172033] rounded w-16"></div></td>
                    <td className="py-4 px-6"><div className="h-4 bg-[#172033] rounded w-48"></div></td>
                    <td className="py-4 px-6"><div className="h-4 bg-[#172033] rounded w-28"></div></td>
                    <td className="py-4 px-6"><div className="h-4 bg-[#172033] rounded w-32"></div></td>
                    <td className="py-4 px-6 text-center"><div className="h-4 bg-[#172033] rounded w-8 mx-auto"></div></td>
                    <td className="py-4 px-6 text-center"><div className="h-4 bg-[#172033] rounded w-20 mx-auto"></div></td>
                    <td className="py-4 px-6 text-right"><div className="h-4 bg-[#172033] rounded w-16 ml-auto"></div></td>
                  </tr>
                ))
              ) : stations.length === 0 ? (
                <tr>
                  <td colSpan="7" className="py-12 text-center text-[#94A3B8] font-mono text-xs">
                    No station records match search query "{debouncedSearch}"
                  </td>
                </tr>
              ) : (
                stations.map((st) => (
                  <tr key={st.id} className="hover:bg-[#172033]/50 transition">
                    <td className="py-4 px-6 font-mono font-bold text-teal-400">{st.stationCode}</td>
                    <td className="py-4 px-6 font-medium text-[#F1F5F9]">{st.name}</td>
                    <td className="py-4 px-6 text-[#94A3B8]">{st.city}</td>
                    <td className="py-4 px-6 text-[#94A3B8]">{st.state}</td>
                    <td className="py-4 px-6 text-center font-mono text-[#F1F5F9]">
                      <span className="px-2 py-0.5 rounded bg-[#172033] border border-[#263449]">
                        {st.platformCount}
                      </span>
                    </td>
                    <td className="py-4 px-6 text-center">
                      <DataOriginBadge origin={st.dataOrigin || 'SYNTHETIC'} />
                    </td>
                    <td className="py-4 px-6 text-right">
                      <span
                        className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-[10px] font-mono font-medium ${
                          st.status === 'ACTIVE'
                            ? 'bg-emerald-500/10 text-emerald-400 border border-emerald-500/20'
                            : 'bg-amber-500/10 text-amber-400 border border-amber-500/20'
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
        <div className="px-6 py-4 bg-[#172033] border-t border-[#263449] flex flex-col sm:flex-row items-center justify-between gap-4 text-xs font-mono">
          <span className="text-[#94A3B8]">
            Page <span className="text-[#F1F5F9] font-bold">{pagination.page + 1}</span> of{' '}
            <span className="text-[#F1F5F9] font-bold">{pagination.totalPages || 1}</span>
          </span>

          <div className="flex items-center gap-2">
            <button
              onClick={() => handlePageChange(pagination.page - 1)}
              disabled={pagination.first || loading}
              className="p-1.5 rounded-lg bg-[#0B1220] border border-[#263449] text-[#F1F5F9] disabled:opacity-40 disabled:cursor-not-allowed hover:bg-slate-800 transition"
            >
              <ChevronLeft className="h-4 w-4" />
            </button>
            <button
              onClick={() => handlePageChange(pagination.page + 1)}
              disabled={pagination.last || loading}
              className="p-1.5 rounded-lg bg-[#0B1220] border border-[#263449] text-[#F1F5F9] disabled:opacity-40 disabled:cursor-not-allowed hover:bg-slate-800 transition"
            >
              <ChevronRight className="h-4 w-4" />
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

