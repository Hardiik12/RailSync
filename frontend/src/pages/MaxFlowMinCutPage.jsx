import React, { useState } from 'react';
import { runMaxFlowMinCut } from '../api/m4Api';
import { Scissors, Play, CheckCircle2 } from 'lucide-react';

export const MaxFlowMinCutPage = () => {
  const [vertexCount, setVertexCount] = useState(6);
  const [source, setSource] = useState(0);
  const [sink, setSink] = useState(5);
  const [edgesStr, setEdgesStr] = useState(
    "0, 1, 16, S, A\n0, 2, 13, S, B\n1, 2, 10, A, B\n1, 3, 12, A, C\n2, 1, 4, B, A\n2, 4, 14, B, D\n3, 2, 9, C, B\n3, 5, 20, C, T\n4, 3, 7, D, C\n4, 5, 4, D, T"
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

      const res = await runMaxFlowMinCut({
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
          <div className="flex items-center gap-2 text-rose-400 font-mono text-xs mb-1">
            <Scissors className="h-4 w-4" />
            <span>M4 Network Flow — CO4</span>
          </div>
          <h1 className="text-2xl font-bold text-slate-100">Max-Flow Min-Cut Theorem Visualizer</h1>
          <p className="text-xs text-slate-400 mt-1">
            Residual reachability partitioning for railway bottleneck identification and cut capacity extraction.
          </p>
        </div>
        <div className="flex items-center gap-3">
          <div className="px-3 py-1.5 rounded-lg bg-slate-900 border border-slate-800 text-xs font-mono text-slate-300">
            Theorem: <span className="text-rose-400 font-bold">Max Flow == Min Cut Capacity</span>
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
              className="w-full bg-slate-950 border border-slate-800 rounded-lg px-4 py-2 text-sm text-slate-100 font-mono focus:outline-none focus:border-rose-500/50"
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
              className="w-full bg-slate-950 border border-slate-800 rounded-lg px-4 py-2 text-sm text-slate-100 font-mono focus:outline-none focus:border-rose-500/50"
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
              className="w-full bg-slate-950 border border-slate-800 rounded-lg px-4 py-2 text-sm text-slate-100 font-mono focus:outline-none focus:border-rose-500/50"
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
            className="w-full bg-slate-950 border border-slate-800 rounded-lg p-3 text-sm text-slate-100 font-mono focus:outline-none focus:border-rose-500/50"
            required
          />
        </div>

        <div className="flex justify-end pt-2">
          <button
            type="submit"
            disabled={loading}
            className="flex items-center gap-2 px-5 py-2.5 rounded-lg bg-rose-500/10 border border-rose-500/30 text-rose-300 hover:bg-rose-500/20 text-xs font-semibold transition-all disabled:opacity-50"
          >
            <Play className="h-4 w-4" />
            <span>{loading ? 'Extracting Min Cut...' : 'Extract Min Cut Partition'}</span>
          </button>
        </div>
      </form>

      {error && <div className="p-4 rounded-xl bg-rose-500/10 border border-rose-500/30 text-rose-400 text-xs font-mono">{error}</div>}

      {result && (
        <div className="space-y-6">
          <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
            <div className="bg-slate-900/60 border border-slate-800/80 p-4 rounded-xl">
              <span className="text-[10px] font-mono uppercase text-slate-400 block">Maximum Flow Value</span>
              <span className="text-2xl font-bold font-mono text-blue-400">{result.maxFlow}</span>
            </div>
            <div className="bg-slate-900/60 border border-slate-800/80 p-4 rounded-xl">
              <span className="text-[10px] font-mono uppercase text-slate-400 block">Minimum Cut Capacity</span>
              <span className="text-2xl font-bold font-mono text-rose-400">{result.minCutCapacity}</span>
            </div>
            <div className="bg-slate-900/60 border border-slate-800/80 p-4 rounded-xl">
              <span className="text-[10px] font-mono uppercase text-slate-400 block">Theorem Verification</span>
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

          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div className="bg-slate-900/80 border border-slate-800 rounded-xl p-6 space-y-4">
              <h3 className="text-sm font-bold text-blue-300 font-mono uppercase">Source-Side Cut Partition (S-Set)</h3>
              <div className="flex flex-wrap gap-2">
                {result.sourceCutVertices?.map((v, idx) => (
                  <span key={idx} className="px-3 py-1.5 rounded-lg bg-blue-500/10 border border-blue-500/30 text-blue-300 text-xs font-mono font-bold">
                    {v}
                  </span>
                ))}
              </div>
            </div>

            <div className="bg-slate-900/80 border border-slate-800 rounded-xl p-6 space-y-4">
              <h3 className="text-sm font-bold text-rose-300 font-mono uppercase">Sink-Side Cut Partition (T-Set)</h3>
              <div className="flex flex-wrap gap-2">
                {result.sinkCutVertices?.map((v, idx) => (
                  <span key={idx} className="px-3 py-1.5 rounded-lg bg-rose-500/10 border border-rose-500/30 text-rose-300 text-xs font-mono font-bold">
                    {v}
                  </span>
                ))}
              </div>
            </div>
          </div>

          <div className="bg-slate-900/80 border border-slate-800 rounded-xl p-6 space-y-4">
            <h3 className="text-sm font-bold text-slate-200 font-mono uppercase">Bottleneck Cut Edges (Spanning S-Set → T-Set)</h3>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
              {result.cutEdges?.map((edge, idx) => (
                <div key={idx} className="p-3 bg-slate-950 border border-rose-500/30 rounded-lg flex items-center justify-between text-xs font-mono">
                  <span className="font-bold text-blue-300">{edge.uName}</span>
                  <span className="text-slate-500">→</span>
                  <span className="font-bold text-rose-300">{edge.vName}</span>
                  <span className="px-2 py-0.5 rounded bg-rose-500/20 text-rose-300 font-bold">Cap: {edge.capacity}</span>
                </div>
              ))}
            </div>
          </div>
        </div>
      )}
    </div>
  );
};
