import React, { useState } from 'react';
import { runLevenshtein, correctStationName } from '../api/m3Api';
import { GitCommit, Play, Building2, Search, CheckCircle2 } from 'lucide-react';

export const LevenshteinPage = () => {
  const [source, setSource] = useState('NEW_DELI_JN');
  const [target, setTarget] = useState('NEW_DELHI_JN');
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  // M3 Station Correction Adapter State
  const [stQuery, setStQuery] = useState('Vijayawda');
  const [stAlgo, setStAlgo] = useState('LEVENSHTEIN');
  const [stOrigin, setStOrigin] = useState('ALL');
  const [maxCandidates, setMaxCandidates] = useState(5);
  const [stLoading, setStLoading] = useState(false);
  const [stResults, setStResults] = useState(null);
  const [stError, setStError] = useState(null);

  const handleStationCorrection = async (e) => {
    e?.preventDefault();
    if (!stQuery) return;
    setStLoading(true);
    setStError(null);
    try {
      const res = await correctStationName({
        query: stQuery,
        algorithm: stAlgo,
        dataOriginFilter: stOrigin,
        maxCandidates: parseInt(maxCandidates, 10),
        traceEnabled: false,
      });
      if (res.success && res.data) {
        setStResults(res.data);
      }
    } catch (err) {
      setStError(err.message || 'Station correction failed');
    } finally {
      setStLoading(false);
    }
  };

  const handleRun = async (e) => {
    e?.preventDefault();
    setLoading(true);
    setError(null);
    try {
      const res = await runLevenshtein({ source, target, traceEnabled: true, maxTraceSteps: 500 });
      setResult(res.data);
    } catch (err) {
      setError(err.message || 'Execution failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="p-8 max-w-7xl mx-auto space-y-8">
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4 border-b border-slate-800 pb-6">
        <div>
          <div className="flex items-center gap-2 text-cyan-400 font-mono text-xs mb-1">
            <GitCommit className="h-4 w-4" />
            <span>M3 Advanced DP — CO3</span>
          </div>
          <h1 className="text-2xl font-bold text-slate-100">Levenshtein Edit Distance Visualizer</h1>
          <p className="text-xs text-slate-400 mt-1">
            Manual DP matrix construction and edit operation backtracking for station name correction.
          </p>
        </div>
        <div className="flex items-center gap-3">
          <div className="px-3 py-1.5 rounded-lg bg-slate-900 border border-slate-800 text-xs font-mono text-slate-300">
            Time: <span className="text-cyan-400 font-bold">O(n · m)</span>
          </div>
          <div className="px-3 py-1.5 rounded-lg bg-slate-900 border border-slate-800 text-xs font-mono text-slate-300">
            Space: <span className="text-cyan-400 font-bold">O(n · m)</span>
          </div>
        </div>
      </div>

      <form onSubmit={handleRun} className="bg-slate-900/80 border border-slate-800 rounded-xl p-6 space-y-4">
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <label className="block text-xs font-mono uppercase text-slate-400 mb-2">Source Text (Misspelled)</label>
            <input
              type="text"
              value={source}
              onChange={(e) => setSource(e.target.value)}
              className="w-full bg-slate-950 border border-slate-800 rounded-lg px-4 py-2 text-sm text-slate-100 font-mono focus:outline-none focus:border-cyan-500/50"
              required
            />
          </div>
          <div>
            <label className="block text-xs font-mono uppercase text-slate-400 mb-2">Target Text (Candidate Station)</label>
            <input
              type="text"
              value={target}
              onChange={(e) => setTarget(e.target.value)}
              className="w-full bg-slate-950 border border-slate-800 rounded-lg px-4 py-2 text-sm text-slate-100 font-mono focus:outline-none focus:border-cyan-500/50"
              required
            />
          </div>
        </div>
        <div className="flex justify-end pt-2">
          <button
            type="submit"
            disabled={loading}
            className="flex items-center gap-2 px-5 py-2.5 rounded-lg bg-cyan-500/10 border border-cyan-500/30 text-cyan-300 hover:bg-cyan-500/20 text-xs font-semibold transition-all disabled:opacity-50"
          >
            <Play className="h-4 w-4" />
            <span>{loading ? 'Calculating...' : 'Compute Levenshtein Distance'}</span>
          </button>
        </div>
      </form>

      {error && <div className="p-4 rounded-xl bg-rose-500/10 border border-rose-500/30 text-rose-400 text-xs font-mono">{error}</div>}

      {result && (
        <div className="space-y-6">
          <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
            <div className="bg-slate-900/60 border border-slate-800/80 p-4 rounded-xl">
              <span className="text-[10px] font-mono uppercase text-slate-400 block">Edit Distance</span>
              <span className="text-2xl font-bold font-mono text-cyan-400">{result.distance}</span>
            </div>
            <div className="bg-slate-900/60 border border-slate-800/80 p-4 rounded-xl">
              <span className="text-[10px] font-mono uppercase text-slate-400 block">Operations Count</span>
              <span className="text-2xl font-bold font-mono text-emerald-300">{result.editOperations?.length || 0}</span>
            </div>
            <div className="bg-slate-900/60 border border-slate-800/80 p-4 rounded-xl">
              <span className="text-[10px] font-mono uppercase text-slate-400 block">Comparisons</span>
              <span className="text-2xl font-bold font-mono text-slate-200">{result.operationCount}</span>
            </div>
            <div className="bg-slate-900/60 border border-slate-800/80 p-4 rounded-xl">
              <span className="text-[10px] font-mono uppercase text-slate-400 block">Execution Time</span>
              <span className="text-2xl font-bold font-mono text-slate-200">{(result.executionTimeNanos / 1000000).toFixed(3)} ms</span>
            </div>
          </div>

          {/* Edit Operations Path */}
          <div className="bg-slate-900/80 border border-slate-800 rounded-xl p-6 space-y-4">
            <h3 className="text-sm font-bold text-slate-200 font-mono uppercase">Reconstructed Edit Sequence</h3>
            <div className="flex flex-wrap gap-2">
              {result.editOperations?.map((op, idx) => (
                <div key={idx} className="p-3 rounded-lg bg-slate-950 border border-slate-800 flex flex-col gap-1 min-w-[120px]">
                  <span className={`text-[10px] font-mono font-bold px-2 py-0.5 rounded w-fit ${
                    op.type === 'KEEP' ? 'bg-emerald-500/10 text-emerald-400' :
                    op.type === 'INSERT' ? 'bg-cyan-500/10 text-cyan-400' :
                    op.type === 'DELETE' ? 'bg-rose-500/10 text-rose-400' :
                    'bg-amber-500/10 text-amber-400'
                  }`}>
                    {op.type}
                  </span>
                  <span className="text-xs font-mono text-slate-300">{op.description}</span>
                </div>
              ))}
            </div>
          </div>
        </div>
      )}

      {/* M3 Station Name Fuzzy Correction Adapter Component */}
      <div className="bg-slate-900/80 border border-slate-800 rounded-xl p-6 space-y-6">
        <div className="flex flex-col md:flex-row md:items-center justify-between gap-3 pb-4 border-b border-slate-800">
          <div>
            <div className="flex items-center gap-2 text-xs font-mono text-cyan-400">
              <Building2 className="h-4 w-4" />
              <span>M3 RAILWAY DOMAIN ADAPTER</span>
            </div>
            <h2 className="text-lg font-bold text-slate-100 mt-1">Station Name Fuzzy Typo Correction</h2>
            <p className="text-xs text-slate-400">
              Ranks PostgreSQL station candidates using Levenshtein / Damerau-Levenshtein edit distance algorithms.
            </p>
          </div>
        </div>

        <form onSubmit={handleStationCorrection} className="grid grid-cols-1 md:grid-cols-4 gap-4 items-end">
          <div className="space-y-1.5">
            <label className="text-xs font-mono text-slate-300">Typed Station Query</label>
            <input
              type="text"
              value={stQuery}
              onChange={(e) => setStQuery(e.target.value)}
              placeholder="e.g. Vijayawda or Muambai"
              className="w-full bg-slate-950 border border-slate-800 rounded-lg px-3 py-2 text-xs text-slate-200 font-mono focus:outline-none focus:border-cyan-500"
              required
            />
          </div>

          <div className="space-y-1.5">
            <label className="text-xs font-mono text-slate-300">Edit Distance Algorithm</label>
            <select
              value={stAlgo}
              onChange={(e) => setStAlgo(e.target.value)}
              className="w-full bg-slate-950 border border-slate-800 rounded-lg px-3 py-2 text-xs text-slate-200 font-mono focus:outline-none focus:border-cyan-500"
            >
              <option value="LEVENSHTEIN">Levenshtein Distance</option>
              <option value="DAMERAU_LEVENSHTEIN">Damerau-Levenshtein (Transpositions)</option>
            </select>
          </div>

          <div className="space-y-1.5">
            <label className="text-xs font-mono text-slate-300">Data Origin Filter</label>
            <select
              value={stOrigin}
              onChange={(e) => setStOrigin(e.target.value)}
              className="w-full bg-slate-950 border border-slate-800 rounded-lg px-3 py-2 text-xs text-slate-200 font-mono focus:outline-none focus:border-cyan-500"
            >
              <option value="ALL">ALL (Public + Synthetic)</option>
              <option value="PUBLIC_DATA">PUBLIC_DATA Only</option>
              <option value="SYNTHETIC">SYNTHETIC Only</option>
            </select>
          </div>

          <button
            type="submit"
            disabled={stLoading}
            className="flex items-center justify-center gap-2 px-4 py-2 rounded-lg bg-cyan-500/10 border border-cyan-500/30 text-cyan-300 hover:bg-cyan-500/20 text-xs font-semibold font-mono transition-all disabled:opacity-50"
          >
            <Search className="h-4 w-4" />
            <span>{stLoading ? 'Finding Candidates...' : 'Find Candidates'}</span>
          </button>
        </form>

        {stError && <div className="p-3 rounded-lg bg-rose-500/10 border border-rose-500/30 text-rose-400 text-xs font-mono">{stError}</div>}

        {stResults && (
          <div className="space-y-4 pt-2">
            <div className="flex items-center justify-between text-xs font-mono text-slate-400">
              <span>Evaluated {stResults.totalStationsEvaluated} station records:</span>
              <span className="text-cyan-400 font-bold">
                Algorithm: {stResults.algorithm} · Time: {(stResults.executionTimeNanos / 1e6).toFixed(2)} ms
              </span>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
              {stResults.candidates?.map((cand, idx) => (
                <div key={idx} className="p-4 rounded-lg bg-slate-950 border border-slate-800/80 space-y-2">
                  <div className="flex items-center justify-between">
                    <div className="flex items-center gap-2">
                      <span className="text-xs font-mono font-bold text-cyan-400">{cand.station?.stationCode}</span>
                      <span className="text-xs font-semibold text-slate-100">{cand.station?.name}</span>
                    </div>
                    <span className="px-2 py-0.5 rounded bg-cyan-500/10 text-cyan-400 border border-cyan-500/30 text-xs font-mono font-bold">
                      Dist: {cand.distance}
                    </span>
                  </div>

                  <div className="flex items-center justify-between text-xs font-mono text-slate-400">
                    <span>Matched on: <span className="text-slate-200 font-semibold">{cand.matchedField}</span></span>
                    <span
                      className={`text-[10px] px-2 py-0.5 rounded border ${
                        cand.dataOrigin === 'PUBLIC_DATA'
                          ? 'bg-purple-500/10 text-purple-400 border-purple-500/30'
                          : 'bg-emerald-500/10 text-emerald-400 border-emerald-500/30'
                      }`}
                    >
                      {cand.dataOrigin || 'SYNTHETIC'}
                    </span>
                  </div>

                  <div className="text-[11px] text-slate-500 font-mono">
                    City: {cand.station?.city} · State: {cand.station?.state}
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

