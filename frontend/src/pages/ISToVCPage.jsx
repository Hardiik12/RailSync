import React, { useState } from 'react';
import { runIndependentSetToVertexCover } from '../api/m5Api';
import { ShieldCheck, Play, CheckCircle2 } from 'lucide-react';

export const ISToVCPage = () => {
  const [verticesInput, setVerticesInput] = useState('A, B, C, D, E');
  const [edgesInput, setEdgesInput] = useState('A-B\nB-C\nC-D\nD-E');
  const [independentSetInput, setIndependentSetInput] = useState('A, C, E');

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
      const independentSet = independentSetInput.split(',').map((s) => s.trim()).filter(Boolean);

      const payload = {
        vertices,
        edges,
        independentSetSize: independentSet.length,
        independentSet,
        traceEnabled: true,
      };

      const res = await runIndependentSetToVertexCover(payload);
      setResult(res.data);
    } catch (err) {
      setError(err.response?.data?.error?.message || err.message || 'Error running reduction');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="p-8 max-w-7xl mx-auto space-y-8">
      {/* Header */}
      <div className="space-y-3">
        <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-purple-500/10 border border-purple-500/20 text-purple-400 text-xs font-mono">
          <ShieldCheck className="h-3.5 w-3.5" />
          <span>Module 5 — Reduction (Independent Set → Vertex Cover)</span>
        </div>
        <h1 className="text-3xl font-bold text-slate-100 tracking-tight">
          Independent Set to Vertex Cover Reduction
        </h1>
        <p className="text-slate-400 max-w-3xl text-sm leading-relaxed">
          Complement subset theorem proving that S is an independent set in G if and only if V \ S is a vertex cover in G.
        </p>
      </div>

      {/* Input Section */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
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

        <div className="bg-slate-900/80 border border-slate-800 rounded-xl p-6 space-y-4">
          <h2 className="text-base font-bold text-slate-200">Independent Set S</h2>
          <input
            type="text"
            value={independentSetInput}
            onChange={(e) => setIndependentSetInput(e.target.value)}
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
          <span>{loading ? 'Computing Cover...' : 'Derive Complement Vertex Cover'}</span>
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
              <span>Vertex Cover Result (Size = {result.vertexCoverSize})</span>
              {result.verified && (
                <span className="inline-flex items-center gap-1 text-xs px-2.5 py-1 rounded-full bg-emerald-500/10 text-emerald-400 border border-emerald-500/20">
                  <CheckCircle2 className="h-3.5 w-3.5" /> |S| + |VC| = |V| ({result.independentSetSize} + {result.vertexCoverSize} = {result.vertexCount})
                </span>
              )}
            </h2>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div className="bg-slate-950 p-4 rounded-lg border border-slate-800 space-y-2">
                <h3 className="text-xs font-mono text-slate-400 uppercase">Independent Set S ({result.independentSetSize}):</h3>
                <div className="flex flex-wrap gap-2">
                  {result.independentSet?.map((v) => (
                    <span key={v} className="px-2.5 py-1 rounded bg-blue-500/10 text-blue-400 border border-blue-500/30 font-mono text-xs">
                      {v}
                    </span>
                  ))}
                </div>
              </div>

              <div className="bg-slate-950 p-4 rounded-lg border border-slate-800 space-y-2">
                <h3 className="text-xs font-mono text-slate-400 uppercase">Derived Vertex Cover C = V \ S ({result.vertexCoverSize}):</h3>
                <div className="flex flex-wrap gap-2">
                  {result.vertexCover?.map((v) => (
                    <span key={v} className="px-2.5 py-1 rounded bg-purple-500/20 text-purple-300 border border-purple-500/40 font-mono text-xs font-bold">
                      {v}
                    </span>
                  ))}
                </div>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};
