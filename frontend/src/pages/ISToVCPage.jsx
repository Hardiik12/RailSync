import React, { useState } from 'react';
import { runIndependentSetToVertexCover } from '../api/m5Api';
import { AlgorithmPageLayout } from '../components/layout/AlgorithmPageLayout';
import { CheckCircle2 } from 'lucide-react';

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

  const handleReset = () => {
    setVerticesInput('A, B, C, D, E');
    setEdgesInput('A-B\nB-C\nC-D\nD-E');
    setIndependentSetInput('A, C, E');
    setResult(null);
    setError(null);
  };

  const inputForm = (
    <>
      <div className="space-y-1.5">
        <label className="text-xs font-mono text-slate-300 font-medium">Vertices</label>
        <input
          type="text"
          value={verticesInput}
          onChange={(e) => setVerticesInput(e.target.value)}
          className="w-full bg-slate-950 border border-slate-800 rounded-lg px-3 py-2 text-xs text-slate-200 font-mono focus:outline-none focus:border-cyan-500"
        />
      </div>

      <div className="space-y-1.5">
        <label className="text-xs font-mono text-slate-300 font-medium">Graph Edges (u-v)</label>
        <textarea
          value={edgesInput}
          onChange={(e) => setEdgesInput(e.target.value)}
          rows={3}
          className="w-full bg-slate-950 border border-slate-800 rounded-lg px-3 py-2 text-xs text-slate-200 font-mono focus:outline-none focus:border-cyan-500"
        />
      </div>

      <div className="space-y-1.5">
        <label className="text-xs font-mono text-slate-300 font-medium">Independent Set S</label>
        <input
          type="text"
          value={independentSetInput}
          onChange={(e) => setIndependentSetInput(e.target.value)}
          className="w-full bg-slate-950 border border-slate-800 rounded-lg px-3 py-2 text-xs text-slate-200 font-mono focus:outline-none focus:border-cyan-500"
        />
      </div>
    </>
  );

  const resultView = result ? (
    <div className="space-y-5">
      <div className="flex items-center justify-between bg-slate-950 p-4 rounded-lg border border-slate-800">
        <div>
          <span className="text-xs font-mono text-slate-400 uppercase block">Derived Vertex Cover</span>
          <span className="text-xs font-mono text-cyan-400 font-bold">Size = {result.vertexCoverSize}</span>
        </div>
        {result.verified && (
          <span className="inline-flex items-center gap-1.5 text-xs px-3 py-1 rounded-full bg-emerald-500/10 text-emerald-400 border border-emerald-500/30 font-mono font-bold">
            <CheckCircle2 className="h-4 w-4" /> |S| + |VC| = |V| ({result.independentSetSize} + {result.vertexCoverSize} = {result.vertexCount})
          </span>
        )}
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div className="bg-slate-950 p-4 rounded-lg border border-slate-800/80 space-y-2">
          <h3 className="text-xs font-mono text-slate-400 uppercase font-bold">Independent Set S ({result.independentSetSize}):</h3>
          <div className="flex flex-wrap gap-2">
            {result.independentSet?.map((v) => (
              <span key={v} className="px-2.5 py-1 rounded bg-slate-800 text-slate-300 border border-slate-700 font-mono text-xs">
                {v}
              </span>
            ))}
          </div>
        </div>

        <div className="bg-slate-950 p-4 rounded-lg border border-slate-800/80 space-y-2">
          <h3 className="text-xs font-mono text-slate-400 uppercase font-bold">Derived Cover C = V \ S ({result.vertexCoverSize}):</h3>
          <div className="flex flex-wrap gap-2">
            {result.vertexCover?.map((v) => (
              <span key={v} className="px-2.5 py-1 rounded bg-cyan-500/20 text-cyan-300 border border-cyan-500/40 font-mono text-xs font-bold">
                {v}
              </span>
            ))}
          </div>
        </div>
      </div>
    </div>
  ) : null;

  return (
    <AlgorithmPageLayout
      moduleTag="DSA MODULE 5 · CO5"
      title="Independent Set to Vertex Cover Reduction"
      description="Complement subset theorem proving that S is an independent set in G if and only if V \ S is a vertex cover in G."
      inputForm={inputForm}
      onRun={handleRun}
      onReset={handleReset}
      loading={loading}
      error={error}
      result={result}
      resultView={resultView}
      railwayNote="Verifies complement theorem: S is an independent set in G if and only if V \ S is a vertex cover in G."
    />
  );
};
