import React, { useState } from 'react';
import { runDamerau } from '../api/m3Api';
import { Activity, Play } from 'lucide-react';

export const DamerauPage = () => {
  const [source, setSource] = useState('DLEHI');
  const [target, setTarget] = useState('DELHI');
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleRun = async (e) => {
    e?.preventDefault();
    setLoading(true);
    setError(null);
    try {
      const res = await runDamerau({ source, target, traceEnabled: true, maxTraceSteps: 500 });
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
          <div className="flex items-center gap-2 text-emerald-400 font-mono text-xs mb-1">
            <Activity className="h-4 w-4" />
            <span>M3 Advanced DP — CO3</span>
          </div>
          <h1 className="text-2xl font-bold text-slate-100">Damerau-Levenshtein Distance Visualizer</h1>
          <p className="text-xs text-slate-400 mt-1">
            Restricted adjacent transposition distance for station name spell correction.
          </p>
        </div>
        <div className="flex items-center gap-3">
          <div className="px-3 py-1.5 rounded-lg bg-slate-900 border border-slate-800 text-xs font-mono text-slate-300">
            Time: <span className="text-emerald-400 font-bold">O(n · m)</span>
          </div>
          <div className="px-3 py-1.5 rounded-lg bg-slate-900 border border-slate-800 text-xs font-mono text-slate-300">
            Space: <span className="text-emerald-400 font-bold">O(n · m)</span>
          </div>
        </div>
      </div>

      <form onSubmit={handleRun} className="bg-slate-900/80 border border-slate-800 rounded-xl p-6 space-y-4">
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <label className="block text-xs font-mono uppercase text-slate-400 mb-2">Source Text (e.g. DLEHI)</label>
            <input
              type="text"
              value={source}
              onChange={(e) => setSource(e.target.value)}
              className="w-full bg-slate-950 border border-slate-800 rounded-lg px-4 py-2 text-sm text-slate-100 font-mono focus:outline-none focus:border-emerald-500/50"
              required
            />
          </div>
          <div>
            <label className="block text-xs font-mono uppercase text-slate-400 mb-2">Target Text (e.g. DELHI)</label>
            <input
              type="text"
              value={target}
              onChange={(e) => setTarget(e.target.value)}
              className="w-full bg-slate-950 border border-slate-800 rounded-lg px-4 py-2 text-sm text-slate-100 font-mono focus:outline-none focus:border-emerald-500/50"
              required
            />
          </div>
        </div>
        <div className="flex justify-end pt-2">
          <button
            type="submit"
            disabled={loading}
            className="flex items-center gap-2 px-5 py-2.5 rounded-lg bg-emerald-500/10 border border-emerald-500/30 text-emerald-300 hover:bg-emerald-500/20 text-xs font-semibold transition-all disabled:opacity-50"
          >
            <Play className="h-4 w-4" />
            <span>{loading ? 'Calculating...' : 'Compute Damerau Distance'}</span>
          </button>
        </div>
      </form>

      {error && <div className="p-4 rounded-xl bg-rose-500/10 border border-rose-500/30 text-rose-400 text-xs font-mono">{error}</div>}

      {result && (
        <div className="space-y-6">
          <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
            <div className="bg-slate-900/60 border border-slate-800/80 p-4 rounded-xl">
              <span className="text-[10px] font-mono uppercase text-slate-400 block">Damerau Distance</span>
              <span className="text-2xl font-bold font-mono text-emerald-400">{result.distance}</span>
            </div>
            <div className="bg-slate-900/60 border border-slate-800/80 p-4 rounded-xl">
              <span className="text-[10px] font-mono uppercase text-slate-400 block">Transposition Count</span>
              <span className="text-2xl font-bold font-mono text-amber-400">{result.transpositionCount}</span>
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

          <div className="bg-slate-900/80 border border-slate-800 rounded-xl p-6 space-y-4">
            <h3 className="text-sm font-bold text-slate-200 font-mono uppercase">Reconstructed Edit Path</h3>
            <div className="flex flex-wrap gap-2">
              {result.editOperations?.map((op, idx) => (
                <div key={idx} className={`p-3 rounded-lg bg-slate-950 border flex flex-col gap-1 min-w-[140px] ${
                  op.type === 'TRANSPOSITION' ? 'border-amber-500/60 bg-amber-500/10 text-amber-300' : 'border-slate-800'
                }`}>
                  <span className={`text-[10px] font-mono font-bold px-2 py-0.5 rounded w-fit ${
                    op.type === 'TRANSPOSITION' ? 'bg-amber-500/20 text-amber-300' :
                    op.type === 'KEEP' ? 'bg-emerald-500/10 text-emerald-400' :
                    'bg-slate-800 text-slate-300'
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
