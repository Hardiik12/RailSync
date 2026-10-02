import React, { useState } from 'react';
import { runMatrixChain } from '../api/m3Api';
import { Layers, Play } from 'lucide-react';

export const MatrixChainPage = () => {
  const [dimsStr, setDimsStr] = useState('30, 35, 15, 5, 10, 20, 25');
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleRun = async (e) => {
    e?.preventDefault();
    setLoading(true);
    setError(null);
    try {
      const dimensions = dimsStr.split(',').map((v) => parseInt(v.trim(), 10));
      const res = await runMatrixChain({ dimensions, traceEnabled: true, maxTraceSteps: 500 });
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
          <div className="flex items-center gap-2 text-amber-400 font-mono text-xs mb-1">
            <Layers className="h-4 w-4" />
            <span>M3 Advanced DP — CO3</span>
          </div>
          <h1 className="text-2xl font-bold text-slate-100">Matrix-Chain Multiplication Visualizer</h1>
          <p className="text-xs text-slate-400 mt-1">
            Interval DP algorithm for optimal matrix multiplication ordering and parenthesization.
          </p>
        </div>
        <div className="flex items-center gap-3">
          <div className="px-3 py-1.5 rounded-lg bg-slate-900 border border-slate-800 text-xs font-mono text-slate-300">
            Time: <span className="text-amber-400 font-bold">O(n³)</span>
          </div>
          <div className="px-3 py-1.5 rounded-lg bg-slate-900 border border-slate-800 text-xs font-mono text-slate-300">
            Space: <span className="text-amber-400 font-bold">O(n²)</span>
          </div>
        </div>
      </div>

      <form onSubmit={handleRun} className="bg-slate-900/80 border border-slate-800 rounded-xl p-6 space-y-4">
        <div>
          <label className="block text-xs font-mono uppercase text-slate-400 mb-2">Matrix Dimensions Array [p0, p1, ..., pn]</label>
          <input
            type="text"
            value={dimsStr}
            onChange={(e) => setDimsStr(e.target.value)}
            className="w-full bg-slate-950 border border-slate-800 rounded-lg px-4 py-2 text-sm text-slate-100 font-mono focus:outline-none focus:border-amber-500/50"
            required
          />
        </div>
        <div className="flex justify-end pt-2">
          <button
            type="submit"
            disabled={loading}
            className="flex items-center gap-2 px-5 py-2.5 rounded-lg bg-amber-500/10 border border-amber-500/30 text-amber-300 hover:bg-amber-500/20 text-xs font-semibold transition-all disabled:opacity-50"
          >
            <Play className="h-4 w-4" />
            <span>{loading ? 'Computing...' : 'Optimize Matrix Chain'}</span>
          </button>
        </div>
      </form>

      {error && <div className="p-4 rounded-xl bg-rose-500/10 border border-rose-500/30 text-rose-400 text-xs font-mono">{error}</div>}

      {result && (
        <div className="space-y-6">
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div className="bg-slate-900/60 border border-slate-800/80 p-4 rounded-xl">
              <span className="text-[10px] font-mono uppercase text-slate-400 block">Matrix Count</span>
              <span className="text-2xl font-bold font-mono text-amber-400">{result.matrixCount}</span>
            </div>
            <div className="bg-slate-900/60 border border-slate-800/80 p-4 rounded-xl">
              <span className="text-[10px] font-mono uppercase text-slate-400 block">Min Scalar Multiplications</span>
              <span className="text-2xl font-bold font-mono text-amber-300">{result.minScalarMultiplications}</span>
            </div>
            <div className="bg-slate-900/60 border border-slate-800/80 p-4 rounded-xl">
              <span className="text-[10px] font-mono uppercase text-slate-400 block">Execution Time</span>
              <span className="text-2xl font-bold font-mono text-slate-200">{(result.executionTimeNanos / 1000000).toFixed(3)} ms</span>
            </div>
          </div>

          <div className="bg-slate-900/80 border border-slate-800 rounded-xl p-6 space-y-4">
            <h3 className="text-sm font-bold text-slate-200 font-mono uppercase">Optimal Parenthesization</h3>
            <div className="p-4 rounded-xl bg-slate-950 border border-amber-500/30 font-mono text-lg font-bold text-amber-300">
              {result.optimalParenthesization}
            </div>
          </div>
        </div>
      )}
    </div>
  );
};
