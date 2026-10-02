import React, { useState } from 'react';
import { runEdmondsKarp } from '../api/m4Api';
import { Share2, Play } from 'lucide-react';

export const EdmondsKarpPage = () => {
  const [vertexCount, setVertexCount] = useState(6);
  const [source, setSource] = useState(0);
  const [sink, setSink] = useState(5);
  const [edgesStr, setEdgesStr] = useState(
    "0, 1, 10, S, A\n0, 2, 10, S, B\n1, 3, 4, A, C\n1, 4, 8, A, D\n2, 4, 9, B, D\n3, 5, 10, C, T\n4, 5, 10, D, T"
  );
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleRun = async (e) => {
    e?.preventDefault();
    setLoading(true);
    setError(null);
    try {
      const rows = edgesStr.trim().split('\n');
      const edges = rows.map((r) => {
        const parts = r.split(',').map((p) => p.trim());
        return {
          u: parseInt(parts[0], 10),
          v: parseInt(parts[1], 10),
          capacity: parseFloat(parts[2]),
          uName: parts[3] || `Node ${parts[0]}`,
          vName: parts[4] || `Node ${parts[1]}`,
        };
      });

      const res = await runEdmondsKarp({
        vertexCount: Number(vertexCount),
        source: Number(source),
        sink: Number(sink),
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
          <div className="flex items-center gap-2 text-cyan-400 font-mono text-xs mb-1">
            <Share2 className="h-4 w-4" />
            <span>M4 Network Flow — CO4</span>
          </div>
          <h1 className="text-2xl font-bold text-slate-100">Edmonds-Karp Algorithm Visualizer</h1>
          <p className="text-xs text-slate-400 mt-1">
            BFS shortest augmenting path traversal for polynomial-time railway capacity optimization.
          </p>
        </div>
        <div className="flex items-center gap-3">
          <div className="px-3 py-1.5 rounded-lg bg-slate-900 border border-slate-800 text-xs font-mono text-slate-300">
            Time: <span className="text-cyan-400 font-bold">O(V · E²)</span>
          </div>
        </div>
      </div>

      <form onSubmit={handleRun} className="bg-slate-900/80 border border-slate-800 rounded-xl p-6 space-y-4">
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div>
            <label className="block text-xs font-mono uppercase text-slate-400 mb-2">Vertex Count (V)</label>
            <input
              type="number"
              value={vertexCount}
              onChange={(e) => setVertexCount(e.target.value)}
              className="w-full bg-slate-950 border border-slate-800 rounded-lg px-4 py-2 text-sm text-slate-100 font-mono focus:outline-none focus:border-cyan-500/50"
              required
              min={2}
              max={500}
            />
          </div>
          <div>
            <label className="block text-xs font-mono uppercase text-slate-400 mb-2">Source Node Index</label>
            <input
              type="number"
              value={source}
              onChange={(e) => setSource(e.target.value)}
              className="w-full bg-slate-950 border border-slate-800 rounded-lg px-4 py-2 text-sm text-slate-100 font-mono focus:outline-none focus:border-cyan-500/50"
              required
              min={0}
            />
          </div>
          <div>
            <label className="block text-xs font-mono uppercase text-slate-400 mb-2">Sink Node Index</label>
            <input
              type="number"
              value={sink}
              onChange={(e) => setSink(e.target.value)}
              className="w-full bg-slate-950 border border-slate-800 rounded-lg px-4 py-2 text-sm text-slate-100 font-mono focus:outline-none focus:border-cyan-500/50"
              required
              min={0}
            />
          </div>
        </div>

        <div>
          <label className="block text-xs font-mono uppercase text-slate-400 mb-2">Directed Flow Edges (u, v, capacity, uName, vName)</label>
          <textarea
            rows={5}
            value={edgesStr}
            onChange={(e) => setEdgesStr(e.target.value)}
            className="w-full bg-slate-950 border border-slate-800 rounded-lg p-3 text-sm text-slate-100 font-mono focus:outline-none focus:border-cyan-500/50"
            required
          />
        </div>

        <div className="flex justify-end pt-2">
          <button
            type="submit"
            disabled={loading}
            className="flex items-center gap-2 px-5 py-2.5 rounded-lg bg-cyan-500/10 border border-cyan-500/30 text-cyan-300 hover:bg-cyan-500/20 text-xs font-semibold transition-all disabled:opacity-50"
          >
            <Play className="h-4 w-4" />
            <span>{loading ? 'Computing Edmonds-Karp...' : 'Run Edmonds-Karp'}</span>
          </button>
        </div>
      </form>

      {error && <div className="p-4 rounded-xl bg-rose-500/10 border border-rose-500/30 text-rose-400 text-xs font-mono">{error}</div>}

      {result && (
        <div className="space-y-6">
          <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
            <div className="bg-slate-900/60 border border-slate-800/80 p-4 rounded-xl">
              <span className="text-[10px] font-mono uppercase text-slate-400 block">Maximum Flow Value</span>
              <span className="text-2xl font-bold font-mono text-cyan-400">{result.maxFlow}</span>
            </div>
            <div className="bg-slate-900/60 border border-slate-800/80 p-4 rounded-xl">
              <span className="text-[10px] font-mono uppercase text-slate-400 block">BFS Augmenting Paths</span>
              <span className="text-2xl font-bold font-mono text-slate-200">{result.augmentingPaths?.length || 0}</span>
            </div>
            <div className="bg-slate-900/60 border border-slate-800/80 p-4 rounded-xl">
              <span className="text-[10px] font-mono uppercase text-slate-400 block">Comparisons / Queue Pops</span>
              <span className="text-2xl font-bold font-mono text-slate-200">{result.operationCount}</span>
            </div>
            <div className="bg-slate-900/60 border border-slate-800/80 p-4 rounded-xl">
              <span className="text-[10px] font-mono uppercase text-slate-400 block">Execution Time</span>
              <span className="text-2xl font-bold font-mono text-slate-200">{(result.executionTimeNanos / 1000000).toFixed(3)} ms</span>
            </div>
          </div>

          <div className="bg-slate-900/80 border border-slate-800 rounded-xl p-6 space-y-4">
            <h3 className="text-sm font-bold text-slate-200 font-mono uppercase">BFS Augmenting Paths (Shortest Edge Count)</h3>
            <div className="space-y-3">
              {result.augmentingPaths?.map((path, idx) => (
                <div key={idx} className="p-3 bg-slate-950 border border-slate-800 rounded-lg flex flex-col md:flex-row md:items-center justify-between gap-2 text-xs font-mono">
                  <div className="flex items-center gap-2 text-slate-300">
                    <span className="text-cyan-400 font-bold">BFS Path #{idx + 1}:</span>
                    <span>{path.nodeNamePath?.join(' → ')}</span>
                  </div>
                  <div className="flex items-center gap-4 text-slate-400">
                    <span>Bottleneck: <strong className="text-amber-400">{path.bottleneckCapacity}</strong></span>
                    <span>Total Flow: <strong className="text-emerald-400">{path.updatedMaxFlow}</strong></span>
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>
      )}
    </div>
  );
};
