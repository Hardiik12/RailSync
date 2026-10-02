import React, { useState } from 'react';
import { runBitmask } from '../api/m3Api';
import { Binary, Play } from 'lucide-react';

export const BitmaskPage = () => {
  const [nodeCount, setNodeCount] = useState(4);
  const [costMatrixStr, setCostMatrixStr] = useState(
    "0, 10, 15, 20\n10, 0, 35, 25\n15, 35, 0, 30\n20, 25, 30, 0"
  );
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleRun = async (e) => {
    e?.preventDefault();
    setLoading(true);
    setError(null);
    try {
      const rows = costMatrixStr.trim().split('\n');
      const matrix = rows.map((r) => r.split(',').map((v) => parseFloat(v.trim())));

      const res = await runBitmask({ nodeCount: Number(nodeCount), costMatrix: matrix, startNode: 0, traceEnabled: true, maxTraceSteps: 500 });
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
          <div className="flex items-center gap-2 text-purple-400 font-mono text-xs mb-1">
            <Binary className="h-4 w-4" />
            <span>M3 Advanced DP — CO3</span>
          </div>
          <h1 className="text-2xl font-bold text-slate-100">Bitmask DP — Minimum Hamiltonian Route</h1>
          <p className="text-xs text-slate-400 mt-1">
            Bounded exponential subset DP computing the minimum-cost Hamiltonian path starting at startNode and visiting all nodes exactly once (N ≤ 16, does not auto-return to startNode).
          </p>
        </div>
        <div className="flex items-center gap-3">
          <div className="px-3 py-1.5 rounded-lg bg-slate-900 border border-slate-800 text-xs font-mono text-slate-300">
            Time: <span className="text-purple-400 font-bold">O(2ⁿ · n²)</span>
          </div>
          <div className="px-3 py-1.5 rounded-lg bg-slate-900 border border-slate-800 text-xs font-mono text-slate-300">
            Max Limit: <span className="text-amber-400 font-bold">N ≤ 16</span>
          </div>
        </div>
      </div>

      <form onSubmit={handleRun} className="bg-slate-900/80 border border-slate-800 rounded-xl p-6 space-y-4">
        <div>
          <label className="block text-xs font-mono uppercase text-slate-400 mb-2">Node Count (N ≤ 16)</label>
          <input
            type="number"
            value={nodeCount}
            onChange={(e) => setNodeCount(e.target.value)}
            className="w-full bg-slate-950 border border-slate-800 rounded-lg px-4 py-2 text-sm text-slate-100 font-mono focus:outline-none focus:border-purple-500/50"
            required
            max={16}
            min={1}
          />
        </div>
        <div>
          <label className="block text-xs font-mono uppercase text-slate-400 mb-2">Adjacency Cost Matrix (comma-separated rows)</label>
          <textarea
            rows={4}
            value={costMatrixStr}
            onChange={(e) => setCostMatrixStr(e.target.value)}
            className="w-full bg-slate-950 border border-slate-800 rounded-lg p-3 text-sm text-slate-100 font-mono focus:outline-none focus:border-purple-500/50"
            required
          />
        </div>
        <div className="flex justify-end pt-2">
          <button
            type="submit"
            disabled={loading}
            className="flex items-center gap-2 px-5 py-2.5 rounded-lg bg-purple-500/10 border border-purple-500/30 text-purple-300 hover:bg-purple-500/20 text-xs font-semibold transition-all disabled:opacity-50"
          >
            <Play className="h-4 w-4" />
            <span>{loading ? 'Solving DP...' : 'Run Bitmask DP'}</span>
          </button>
        </div>
      </form>

      {error && <div className="p-4 rounded-xl bg-rose-500/10 border border-rose-500/30 text-rose-400 text-xs font-mono">{error}</div>}

      {result && (
        <div className="space-y-6">
          <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
            <div className="bg-slate-900/60 border border-slate-800/80 p-4 rounded-xl">
              <span className="text-[10px] font-mono uppercase text-slate-400 block">Optimal Route Cost</span>
              <span className="text-2xl font-bold font-mono text-purple-400">{result.optimalCost}</span>
            </div>
            <div className="bg-slate-900/60 border border-slate-800/80 p-4 rounded-xl">
              <span className="text-[10px] font-mono uppercase text-slate-400 block">DP States Computed</span>
              <span className="text-2xl font-bold font-mono text-slate-200">{result.stateCount}</span>
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
            <h3 className="text-sm font-bold text-slate-200 font-mono uppercase">Optimal Visited Path Sequence</h3>
            <div className="flex items-center gap-2 font-mono text-sm text-emerald-300">
              {result.pathSequence?.map((node, idx) => (
                <React.Fragment key={idx}>
                  <span className="px-3 py-1.5 rounded-lg bg-slate-950 border border-emerald-500/30 font-bold">Node #{node}</span>
                  {idx < result.pathSequence.length - 1 && <span className="text-slate-500">→</span>}
                </React.Fragment>
              ))}
            </div>
          </div>
        </div>
      )}
    </div>
  );
};
