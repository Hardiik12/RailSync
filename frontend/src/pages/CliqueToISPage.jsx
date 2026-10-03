import React, { useState } from 'react';
import { runCliqueToIndependentSet } from '../api/m5Api';
import { Layers, Play, Clock, Cpu, ArrowRight, CheckCircle2 } from 'lucide-react';

export const CliqueToISPage = () => {
  const [verticesInput, setVerticesInput] = useState('A, B, C, D');
  const [edgesInput, setEdgesInput] = useState('A-B\nB-C\nA-C');
  const [cliqueSizeInput, setCliqueSizeInput] = useState('3');

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
        cliqueSize: parseInt(cliqueSizeInput, 10) || 3,
        traceEnabled: true,
        maxTraceSteps: 50,
      };

      const res = await runCliqueToIndependentSet(payload);
      setResult(res.data);
    } catch (err) {
      setError(err.response?.data?.error?.message || err.message || 'Error running CLIQUE to Independent Set reduction');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="p-8 max-w-7xl mx-auto space-y-8">
      {/* Header */}
      <div className="space-y-3">
        <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-purple-500/10 border border-purple-500/20 text-purple-400 text-xs font-mono">
          <Layers className="h-3.5 w-3.5" />
          <span>Module 5 — Reduction (CLIQUE → Independent Set)</span>
        </div>
        <h1 className="text-3xl font-bold text-slate-100 tracking-tight">
          CLIQUE to Independent Set Reduction
        </h1>
        <p className="text-slate-400 max-w-3xl text-sm leading-relaxed">
          Graph complement transformation showing that a graph G contains a clique of size k if and only if its complement graph Ḡ contains an independent set of size k.
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
          <h2 className="text-base font-bold text-slate-200">Original Edges (u-v)</h2>
          <textarea
            value={edgesInput}
            onChange={(e) => setEdgesInput(e.target.value)}
            rows={3}
            className="w-full bg-slate-950 border border-slate-800 rounded-lg px-4 py-2.5 text-sm text-slate-200 font-mono focus:outline-none focus:border-purple-500"
          />
        </div>

        <div className="bg-slate-900/80 border border-slate-800 rounded-xl p-6 space-y-4">
          <h2 className="text-base font-bold text-slate-200">Target Size k</h2>
          <input
            type="number"
            value={cliqueSizeInput}
            onChange={(e) => setCliqueSizeInput(e.target.value)}
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
          <span>{loading ? 'Transforming...' : 'Construct Complement Graph'}</span>
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
              <span>Complement Graph Ḡ (Independent Set Size k = {result.independentSetSize})</span>
              <span className="inline-flex items-center gap-1 text-xs px-2.5 py-1 rounded-full bg-emerald-500/10 text-emerald-400 border border-emerald-500/20">
                <CheckCircle2 className="h-3.5 w-3.5" /> EQUIVALENCE PROVED
              </span>
            </h2>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div className="space-y-2">
                <h3 className="text-xs font-mono text-slate-400 uppercase">Transformed Vertices:</h3>
                <div className="bg-slate-950 p-4 rounded-lg border border-slate-800 font-mono text-xs text-slate-300">
                  {result.transformedVertices?.join(', ')}
                </div>
              </div>

              <div className="space-y-2">
                <h3 className="text-xs font-mono text-slate-400 uppercase">Complement Edges Ē ({result.transformedEdges?.length}):</h3>
                <div className="bg-slate-950 p-4 rounded-lg border border-slate-800 max-h-48 overflow-y-auto font-mono text-xs text-slate-400 space-y-1">
                  {result.transformedEdges?.map((e, idx) => (
                    <div key={idx} className="flex items-center gap-2">
                      <span className="text-purple-400">{e[0]}</span>
                      <ArrowRight className="h-3 w-3 text-slate-600" />
                      <span className="text-purple-400">{e[1]}</span>
                    </div>
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
