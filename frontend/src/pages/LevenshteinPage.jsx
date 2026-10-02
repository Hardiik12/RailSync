import React, { useState } from 'react';
import { runLevenshtein } from '../api/m3Api';
import { GitCommit, Play } from 'lucide-react';

export const LevenshteinPage = () => {
  const [source, setSource] = useState('NEW_DELI_JN');
  const [target, setTarget] = useState('NEW_DELHI_JN');
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

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
    </div>
  );
};
