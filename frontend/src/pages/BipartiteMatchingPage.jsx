import React, { useState } from 'react';
import { runBipartiteMatching } from '../api/m4Api';
import { Shuffle, Play } from 'lucide-react';

export const BipartiteMatchingPage = () => {
  const [leftStr, setLeftStr] = useState('Train_101, Train_102, Train_103, Train_104');
  const [rightStr, setRightStr] = useState('Platform_1, Platform_2, Platform_3, Platform_4');
  const [edgesStr, setEdgesStr] = useState(
    "Train_101, Platform_1\nTrain_101, Platform_2\nTrain_102, Platform_2\nTrain_103, Platform_3\nTrain_104, Platform_3\nTrain_104, Platform_4"
  );
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleRun = async (e) => {
    e?.preventDefault();
    setLoading(true);
    setError(null);
    try {
      const leftVertices = leftStr.split(',').map((s) => s.trim()).filter(Boolean);
      const rightVertices = rightStr.split(',').map((s) => s.trim()).filter(Boolean);
      const rows = edgesStr.trim().split('\n');
      const edges = rows.map((r) => {
        const parts = r.split(',').map((p) => p.trim());
        return { left: parts[0], right: parts[1] };
      });

      const res = await runBipartiteMatching({
        leftVertices,
        rightVertices,
        edges,
        traceEnabled: true,
        maxTraceSteps: 500,
      });
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
            <Shuffle className="h-4 w-4" />
            <span>M4 Network Flow — CO4</span>
          </div>
          <h1 className="text-2xl font-bold text-slate-100">Bipartite Maximum Matching Visualizer</h1>
          <p className="text-xs text-slate-400 mt-1">
            Train service to station platform assignment optimization using Kuhn's augmenting paths.
          </p>
        </div>
        <div className="flex items-center gap-3">
          <div className="px-3 py-1.5 rounded-lg bg-slate-900 border border-slate-800 text-xs font-mono text-slate-300">
            Time: <span className="text-emerald-400 font-bold">O(V · E)</span>
          </div>
        </div>
      </div>

      <form onSubmit={handleRun} className="bg-slate-900/80 border border-slate-800 rounded-xl p-6 space-y-4">
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <label className="block text-xs font-mono uppercase text-slate-400 mb-2">Left Vertices (Train Services)</label>
            <input
              type="text"
              value={leftStr}
              onChange={(e) => setLeftStr(e.target.value)}
              className="w-full bg-slate-950 border border-slate-800 rounded-lg px-4 py-2 text-sm text-slate-100 font-mono focus:outline-none focus:border-emerald-500/50"
              required
            />
          </div>
          <div>
            <label className="block text-xs font-mono uppercase text-slate-400 mb-2">Right Vertices (Platforms)</label>
            <input
              type="text"
              value={rightStr}
              onChange={(e) => setRightStr(e.target.value)}
              className="w-full bg-slate-950 border border-slate-800 rounded-lg px-4 py-2 text-sm text-slate-100 font-mono focus:outline-none focus:border-emerald-500/50"
              required
            />
          </div>
        </div>

        <div>
          <label className="block text-xs font-mono uppercase text-slate-400 mb-2">Bipartite Edges (LeftVertex, RightVertex)</label>
          <textarea
            rows={5}
            value={edgesStr}
            onChange={(e) => setEdgesStr(e.target.value)}
            className="w-full bg-slate-950 border border-slate-800 rounded-lg p-3 text-sm text-slate-100 font-mono focus:outline-none focus:border-emerald-500/50"
            required
          />
        </div>

        <div className="flex justify-end pt-2">
          <button
            type="submit"
            disabled={loading}
            className="flex items-center gap-2 px-5 py-2.5 rounded-lg bg-emerald-500/10 border border-emerald-500/30 text-emerald-300 hover:bg-emerald-500/20 text-xs font-semibold transition-all disabled:opacity-50"
          >
            <Play className="h-4 w-4" />
            <span>{loading ? 'Finding Max Matching...' : 'Run Bipartite Matching'}</span>
          </button>
        </div>
      </form>

      {error && <div className="p-4 rounded-xl bg-rose-500/10 border border-rose-500/30 text-rose-400 text-xs font-mono">{error}</div>}

      {result && (
        <div className="space-y-6">
          <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
            <div className="bg-slate-900/60 border border-slate-800/80 p-4 rounded-xl">
              <span className="text-[10px] font-mono uppercase text-slate-400 block">Maximum Matching Size</span>
              <span className="text-2xl font-bold font-mono text-emerald-400">{result.matchingSize}</span>
            </div>
            <div className="bg-slate-900/60 border border-slate-800/80 p-4 rounded-xl">
              <span className="text-[10px] font-mono uppercase text-slate-400 block">Unmatched Left (Trains)</span>
              <span className="text-2xl font-bold font-mono text-slate-200">{result.unmatchedLeftVertices?.length || 0}</span>
            </div>
            <div className="bg-slate-900/60 border border-slate-800/80 p-4 rounded-xl">
              <span className="text-[10px] font-mono uppercase text-slate-400 block">Unmatched Right (Platforms)</span>
              <span className="text-2xl font-bold font-mono text-slate-200">{result.unmatchedRightVertices?.length || 0}</span>
            </div>
            <div className="bg-slate-900/60 border border-slate-800/80 p-4 rounded-xl">
              <span className="text-[10px] font-mono uppercase text-slate-400 block">Execution Time</span>
              <span className="text-2xl font-bold font-mono text-slate-200">{(result.executionTimeNanos / 1000000).toFixed(3)} ms</span>
            </div>
          </div>

          <div className="bg-slate-900/80 border border-slate-800 rounded-xl p-6 space-y-4">
            <h3 className="text-sm font-bold text-slate-200 font-mono uppercase">Optimal Train → Platform Assignments</h3>
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-3">
              {result.matchedPairs?.map((pair, idx) => (
                <div key={idx} className="p-3 bg-slate-950 border border-emerald-500/30 rounded-lg flex items-center justify-between text-xs font-mono">
                  <span className="font-bold text-blue-300">{pair.left}</span>
                  <span className="text-slate-500">→</span>
                  <span className="font-bold text-emerald-400">{pair.right}</span>
                </div>
              ))}
            </div>
          </div>
        </div>
      )}
    </div>
  );
};
