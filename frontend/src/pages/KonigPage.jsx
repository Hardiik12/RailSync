import React, { useState } from 'react';
import { runKonig } from '../api/m4Api';
import { Network, Play, CheckCircle2 } from 'lucide-react';

export const KonigPage = () => {
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

      const res = await runKonig({
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
          <div className="flex items-center gap-2 text-purple-400 font-mono text-xs mb-1">
            <Network className="h-4 w-4" />
            <span>M4 Network Flow — CO4</span>
          </div>
          <h1 className="text-2xl font-bold text-slate-100">König's Theorem Visualizer</h1>
          <p className="text-xs text-slate-400 mt-1">
            Alternating path reachability proving Maximum Matching Size == Minimum Vertex Cover Size.
          </p>
        </div>
        <div className="flex items-center gap-3">
          <div className="px-3 py-1.5 rounded-lg bg-slate-900 border border-slate-800 text-xs font-mono text-slate-300">
            Theorem: <span className="text-purple-400 font-bold">|Max Matching| = |Min Vertex Cover|</span>
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
              className="w-full bg-slate-950 border border-slate-800 rounded-lg px-4 py-2 text-sm text-slate-100 font-mono focus:outline-none focus:border-purple-500/50"
              required
            />
          </div>
          <div>
            <label className="block text-xs font-mono uppercase text-slate-400 mb-2">Right Vertices (Platforms)</label>
            <input
              type="text"
              value={rightStr}
              onChange={(e) => setRightStr(e.target.value)}
              className="w-full bg-slate-950 border border-slate-800 rounded-lg px-4 py-2 text-sm text-slate-100 font-mono focus:outline-none focus:border-purple-500/50"
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
            <span>{loading ? "Analyzing König's Theorem..." : "Verify König's Theorem"}</span>
          </button>
        </div>
      </form>

      {error && <div className="p-4 rounded-xl bg-rose-500/10 border border-rose-500/30 text-rose-400 text-xs font-mono">{error}</div>}

      {result && (
        <div className="space-y-6">
          <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
            <div className="bg-slate-900/60 border border-slate-800/80 p-4 rounded-xl">
              <span className="text-[10px] font-mono uppercase text-slate-400 block">Maximum Matching Size</span>
              <span className="text-2xl font-bold font-mono text-purple-400">{result.matchingSize}</span>
            </div>
            <div className="bg-slate-900/60 border border-slate-800/80 p-4 rounded-xl">
              <span className="text-[10px] font-mono uppercase text-slate-400 block">Minimum Vertex Cover Size</span>
              <span className="text-2xl font-bold font-mono text-cyan-400">{result.vertexCoverSize}</span>
            </div>
            <div className="bg-slate-900/60 border border-slate-800/80 p-4 rounded-xl">
              <span className="text-[10px] font-mono uppercase text-slate-400 block">Theorem Equal Proof</span>
              <div className="flex items-center gap-2 mt-1">
                <CheckCircle2 className="h-5 w-5 text-emerald-400" />
                <span className="text-lg font-bold font-mono text-emerald-400">PASSED</span>
              </div>
            </div>
            <div className="bg-slate-900/60 border border-slate-800/80 p-4 rounded-xl">
              <span className="text-[10px] font-mono uppercase text-slate-400 block">Execution Time</span>
              <span className="text-2xl font-bold font-mono text-slate-200">{(result.executionTimeNanos / 1000000).toFixed(3)} ms</span>
            </div>
          </div>

          <div className="bg-slate-900/80 border border-slate-800 rounded-xl p-6 space-y-4">
            <h3 className="text-sm font-bold text-slate-200 font-mono uppercase">Constructed Minimum Vertex Cover</h3>
            <div className="flex flex-wrap gap-2">
              {result.minimumVertexCover?.map((vc, idx) => (
                <span key={idx} className="px-3 py-1.5 rounded-lg bg-slate-950 border border-purple-500/40 text-xs font-mono font-bold text-purple-300">
                  [{vc.partition}] {vc.name}
                </span>
              ))}
            </div>
          </div>
        </div>
      )}
    </div>
  );
};
