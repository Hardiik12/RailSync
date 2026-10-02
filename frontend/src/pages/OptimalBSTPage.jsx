import React, { useState } from 'react';
import { runOptimalBST } from '../api/m3Api';
import { Network, Play } from 'lucide-react';

export const OptimalBSTPage = () => {
  const [keysStr, setKeysStr] = useState('NDLS, PUNE, SBC');
  const [freqsStr, setFreqsStr] = useState('0.2, 0.5, 0.3');
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleRun = async (e) => {
    e?.preventDefault();
    setLoading(true);
    setError(null);
    try {
      const keys = keysStr.split(',').map((k) => k.trim());
      const frequencies = freqsStr.split(',').map((f) => parseFloat(f.trim()));

      const res = await runOptimalBST({ keys, frequencies, traceEnabled: true, maxTraceSteps: 500 });
      setResult(res.data);
    } catch (err) {
      setError(err.message || 'Execution failed');
    } finally {
      setLoading(false);
    }
  };

  const renderTree = (node) => {
    if (!node) return null;
    return (
      <div className="flex flex-col items-center space-y-2 border border-slate-800 p-3 rounded-lg bg-slate-950">
        <div className="px-3 py-1 bg-cyan-500/10 border border-cyan-500/30 rounded text-xs font-mono font-bold text-cyan-300">
          Key: {node.key} (freq: {node.frequency})
        </div>
        {(node.left || node.right) && (
          <div className="flex gap-4 pt-2">
            <div className="flex flex-col items-center">
              <span className="text-[10px] text-slate-500 font-mono">L</span>
              {node.left ? renderTree(node.left) : <span className="text-xs text-slate-600">-</span>}
            </div>
            <div className="flex flex-col items-center">
              <span className="text-[10px] text-slate-500 font-mono">R</span>
              {node.right ? renderTree(node.right) : <span className="text-xs text-slate-600">-</span>}
            </div>
          </div>
        )}
      </div>
    );
  };

  return (
    <div className="p-8 max-w-7xl mx-auto space-y-8">
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4 border-b border-slate-800 pb-6">
        <div>
          <div className="flex items-center gap-2 text-cyan-400 font-mono text-xs mb-1">
            <Network className="h-4 w-4" />
            <span>M3 Advanced DP — CO3</span>
          </div>
          <h1 className="text-2xl font-bold text-slate-100">Optimal Binary Search Tree Visualizer</h1>
          <p className="text-xs text-slate-400 mt-1">
            Interval DP algorithm for minimum expected search cost tree construction.
          </p>
        </div>
        <div className="flex items-center gap-3">
          <div className="px-3 py-1.5 rounded-lg bg-slate-900 border border-slate-800 text-xs font-mono text-slate-300">
            Time: <span className="text-cyan-400 font-bold">O(n³)</span>
          </div>
          <div className="px-3 py-1.5 rounded-lg bg-slate-900 border border-slate-800 text-xs font-mono text-slate-300">
            Space: <span className="text-cyan-400 font-bold">O(n²)</span>
          </div>
        </div>
      </div>

      <form onSubmit={handleRun} className="bg-slate-900/80 border border-slate-800 rounded-xl p-6 space-y-4">
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <label className="block text-xs font-mono uppercase text-slate-400 mb-2">Ordered Keys (comma-separated)</label>
            <input
              type="text"
              value={keysStr}
              onChange={(e) => setKeysStr(e.target.value)}
              className="w-full bg-slate-950 border border-slate-800 rounded-lg px-4 py-2 text-sm text-slate-100 font-mono focus:outline-none focus:border-cyan-500/50"
              required
            />
          </div>
          <div>
            <label className="block text-xs font-mono uppercase text-slate-400 mb-2">Access Frequencies (comma-separated)</label>
            <input
              type="text"
              value={freqsStr}
              onChange={(e) => setFreqsStr(e.target.value)}
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
            <span>{loading ? 'Constructing OBST...' : 'Construct Optimal BST'}</span>
          </button>
        </div>
      </form>

      {error && <div className="p-4 rounded-xl bg-rose-500/10 border border-rose-500/30 text-rose-400 text-xs font-mono">{error}</div>}

      {result && (
        <div className="space-y-6">
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div className="bg-slate-900/60 border border-slate-800/80 p-4 rounded-xl">
              <span className="text-[10px] font-mono uppercase text-slate-400 block">Optimal Expected Search Cost</span>
              <span className="text-2xl font-bold font-mono text-cyan-400">{result.minCost.toFixed(2)}</span>
            </div>
            <div className="bg-slate-900/60 border border-slate-800/80 p-4 rounded-xl">
              <span className="text-[10px] font-mono uppercase text-slate-400 block">Key Count</span>
              <span className="text-2xl font-bold font-mono text-slate-200">{result.keyCount}</span>
            </div>
            <div className="bg-slate-900/60 border border-slate-800/80 p-4 rounded-xl">
              <span className="text-[10px] font-mono uppercase text-slate-400 block">Execution Time</span>
              <span className="text-2xl font-bold font-mono text-slate-200">{(result.executionTimeNanos / 1000000).toFixed(3)} ms</span>
            </div>
          </div>

          <div className="bg-slate-900/80 border border-slate-800 rounded-xl p-6 space-y-4">
            <h3 className="text-sm font-bold text-slate-200 font-mono uppercase">Reconstructed Optimal BST Tree</h3>
            <div className="overflow-x-auto flex justify-center p-4">
              {renderTree(result.rootNode)}
            </div>
          </div>
        </div>
      )}
    </div>
  );
};
