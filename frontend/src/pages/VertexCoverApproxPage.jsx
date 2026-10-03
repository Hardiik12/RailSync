import React, { useState } from 'react';
import { runVertexCover2Approx } from '../api/m5Api';
import { Cpu, Play, Clock, HelpCircle, Layers, CheckCircle2, ArrowRight } from 'lucide-react';

export const VertexCoverApproxPage = () => {
  const [verticesInput, setVerticesInput] = useState('A, B, C, D, E');
  const [edgesInput, setEdgesInput] = useState('A-B\nB-C\nC-D\nD-E');

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [result, setResult] = useState(null);

  const handleRun = async () => {
    setLoading(true);
    setError(null);
    setResult(null);

    try {
      const vertices = verticesInput.split(',').map((s) => s.trim()).filter(Boolean);
      const edgeLines = edgesInput.split('\n').map((l) => l.trim()).filter(Boolean);
      const edges = edgeLines.map((line) => {
        const parts = line.split('-').map((s) => s.trim()).filter(Boolean);
        return parts.length === 2 ? parts : line.split(',').map((s) => s.trim()).filter(Boolean);
      });

      const payload = {
        vertices,
        edges,
        traceEnabled: true,
        maxTraceSteps: 50,
      };

      const res = await runVertexCover2Approx(payload);
      setResult(res.data);
    } catch (err) {
      setError(err.response?.data?.error?.message || err.message || 'Error running 2-approximation');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="p-8 max-w-7xl mx-auto space-y-8">
      {/* Header */}
      <div className="space-y-3">
        <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-purple-500/10 border border-purple-500/20 text-purple-400 text-xs font-mono">
          <Cpu className="h-3.5 w-3.5" />
          <span>Module 5 — Approximation Algorithm (Vertex Cover 2-Approx)</span>
        </div>
        <h1 className="text-3xl font-bold text-slate-100 tracking-tight">
          Vertex Cover 2-Approximation Algorithm
        </h1>
        <p className="text-slate-400 max-w-3xl text-sm leading-relaxed">
          Maximal-matching based 2-approximation algorithm guaranteeing that the returned cover size |C| is at most twice the optimal minimum vertex cover size OPT (|C| ≤ 2 · OPT).
        </p>
      </div>

      {/* Input Section */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div className="bg-slate-900/80 border border-slate-800 rounded-xl p-6 space-y-4">
          <h2 className="text-base font-bold text-slate-200">Vertices</h2>
          <input
            type="text"
            value={verticesInput}
            onChange={(e) => setVerticesInput(e.target.value)}
            className="w-full bg-slate-950 border border-slate-800 rounded-lg px-4 py-2.5 text-sm text-slate-200 font-mono focus:outline-none focus:border-purple-500"
          />
        </div>

        <div className="bg-slate-900/80 border border-slate-800 rounded-xl p-6 space-y-4">
          <h2 className="text-base font-bold text-slate-200">Graph Edges (u-v)</h2>
          <textarea
            value={edgesInput}
            onChange={(e) => setEdgesInput(e.target.value)}
            rows={3}
            className="w-full bg-slate-950 border border-slate-800 rounded-lg px-4 py-2.5 text-sm text-slate-200 font-mono focus:outline-none focus:border-purple-500"
          />
        </div>
      </div>

      {/* Controls */}
      <div className="flex justify-end bg-slate-900/80 border border-slate-800 rounded-xl p-4">
        <button
          onClick={handleRun}
          disabled={loading}
          className="inline-flex items-center gap-2 px-6 py-2.5 text-xs font-semibold rounded-lg bg-purple-500 text-white hover:bg-purple-600 disabled:opacity-50 transition-all shadow-lg shadow-purple-500/20"
        >
          <Play className="h-4 w-4" />
          <span>{loading ? 'Approximating...' : 'Run 2-Approximation'}</span>
        </button>
      </div>

      {error && (
        <div className="bg-rose-500/10 border border-rose-500/20 rounded-xl p-4 text-rose-400 text-xs font-mono">
          {error}
        </div>
      )}

      {/* Result Section */}
      {result && (
        <div className="space-y-6">
          <div className="bg-slate-900/80 border border-slate-800 rounded-xl p-6 space-y-4">
            <h2 className="text-base font-bold text-slate-200 flex items-center gap-2">
              <span>Approximation Cover Set (Size = {result.coverSize})</span>
              {result.isVerifiedCover && (
                <span className="inline-flex items-center gap-1 text-xs px-2.5 py-1 rounded-full bg-emerald-500/10 text-emerald-400 border border-emerald-500/20">
                  <CheckCircle2 className="h-3.5 w-3.5" /> VERIFIED VERTEX COVER (|C| ≤ 2 · OPT)
                </span>
              )}
            </h2>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div className="bg-slate-950 p-4 rounded-lg border border-slate-800 space-y-2">
                <h3 className="text-xs font-mono text-slate-400 uppercase">Approximate Vertex Cover C:</h3>
                <div className="flex flex-wrap gap-2">
                  {result.cover?.map((v) => (
                    <span key={v} className="px-3 py-1.5 rounded bg-purple-500/20 text-purple-300 border border-purple-500/40 font-mono text-xs font-bold">
                      {v}
                    </span>
                  ))}
                </div>
              </div>

              <div className="bg-slate-950 p-4 rounded-lg border border-slate-800 space-y-2">
                <h3 className="text-xs font-mono text-slate-400 uppercase">Selected Matching Edges ({result.selectedEdges?.length}):</h3>
                <div className="font-mono text-xs text-slate-300 space-y-1">
                  {result.selectedEdges?.map((e, idx) => (
                    <div key={idx} className="flex items-center gap-2">
                      <span className="text-emerald-400">{e[0]}</span>
                      <ArrowRight className="h-3 w-3 text-slate-600" />
                      <span className="text-emerald-400">{e[1]}</span>
                    </div>
                  ))}
                </div>
              </div>
            </div>
          </div>

          {/* Complexity & Metrics */}
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div className="bg-slate-900/80 border border-slate-800 rounded-xl p-6 space-y-3">
              <h2 className="text-sm font-bold text-slate-200 flex items-center gap-2">
                <Cpu className="h-4 w-4 text-purple-400" />
                <span>Theoretical Complexity</span>
              </h2>
              <div className="grid grid-cols-2 gap-4 text-xs font-mono">
                <div className="bg-slate-950 p-3 rounded-lg border border-slate-800">
                  <span className="text-slate-400 block text-[10px]">TIME</span>
                  <span className="text-purple-400 text-sm font-bold">{result.complexity?.timeComplexity || 'O(V + E)'}</span>
                </div>
                <div className="bg-slate-950 p-3 rounded-lg border border-slate-800">
                  <span className="text-slate-400 block text-[10px]">SPACE</span>
                  <span className="text-purple-400 text-sm font-bold">{result.complexity?.spaceComplexity || 'O(V + E)'}</span>
                </div>
              </div>
            </div>

            <div className="bg-slate-900/80 border border-slate-800 rounded-xl p-6 space-y-3">
              <h2 className="text-sm font-bold text-slate-200 flex items-center gap-2">
                <Clock className="h-4 w-4 text-purple-400" />
                <span>Measured Benchmark</span>
              </h2>
              <div className="grid grid-cols-2 gap-4 text-xs font-mono">
                <div className="bg-slate-950 p-3 rounded-lg border border-slate-800">
                  <span className="text-slate-400 block text-[10px]">RUNTIME</span>
                  <span className="text-emerald-400 text-sm font-bold">
                    {(result.executionTimeNanos / 1000000).toFixed(3)} ms
                  </span>
                </div>
                <div className="bg-slate-950 p-3 rounded-lg border border-slate-800">
                  <span className="text-slate-400 block text-[10px]">OPERATIONS</span>
                  <span className="text-purple-400 text-sm font-bold">{result.operationCount} ops</span>
                </div>
              </div>
            </div>
          </div>

          {/* Trace Execution */}
          {result.trace && result.trace.length > 0 && (
            <div className="bg-slate-900/80 border border-slate-800 rounded-xl p-6 space-y-4">
              <h2 className="text-sm font-bold text-slate-200 flex items-center gap-2">
                <Layers className="h-4 w-4 text-purple-400" />
                <span>Maximal Matching Trace</span>
              </h2>
              <div className="overflow-x-auto">
                <table className="w-full text-left text-xs font-mono border-collapse">
                  <thead>
                    <tr className="border-b border-slate-800 text-slate-400">
                      <th className="py-2 px-3">Step</th>
                      <th className="py-2 px-3">Action</th>
                      <th className="py-2 px-3">Description</th>
                    </tr>
                  </thead>
                  <tbody className="divide-y divide-slate-800/50">
                    {result.trace.map((step) => (
                      <tr key={step.step} className="hover:bg-slate-800/30">
                        <td className="py-2.5 px-3 text-slate-400">{step.step}</td>
                        <td className="py-2.5 px-3">
                          <span className="px-2 py-0.5 rounded bg-purple-500/10 text-purple-400 border border-purple-500/20">
                            {step.action}
                          </span>
                        </td>
                        <td className="py-2.5 px-3 text-slate-300">{step.description}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          )}

          {/* Railway Use Case */}
          <div className="bg-purple-950/20 border border-purple-800/40 rounded-xl p-6 space-y-2">
            <h2 className="text-xs font-bold text-purple-300 uppercase tracking-wider flex items-center gap-2">
              <HelpCircle className="h-4 w-4 text-purple-400" />
              <span>Railway Domain Application & Approximation Guarantee</span>
            </h2>
            <p className="text-xs text-slate-300 leading-relaxed">
              Used for fast conflict-graph monitoring station placement. The maximal-matching 2-approximation algorithm guarantees that the selected vertex cover size is at most 2 times the exact minimum vertex cover size OPT.
              <strong className="text-purple-300"> Note:</strong> This is a polynomial-time approximation, not an exact minimum solver.
            </p>
          </div>
        </div>
      )}
    </div>
  );
};
