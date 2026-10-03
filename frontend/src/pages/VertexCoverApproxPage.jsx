import React, { useState } from 'react';
import { runVertexCover2Approx } from '../api/m5Api';
import { AlgorithmPageLayout } from '../components/layout/AlgorithmPageLayout';
import { ArrowRight, CheckCircle2 } from 'lucide-react';

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

  const handleReset = () => {
    setVerticesInput('A, B, C, D, E');
    setEdgesInput('A-B\nB-C\nC-D\nD-E');
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
          rows={4}
          className="w-full bg-slate-950 border border-slate-800 rounded-lg px-3 py-2 text-xs text-slate-200 font-mono focus:outline-none focus:border-cyan-500"
        />
      </div>
    </>
  );

  const resultView = result ? (
    <div className="space-y-5">
      <div className="flex items-center justify-between bg-slate-950 p-4 rounded-lg border border-slate-800">
        <div>
          <span className="text-xs font-mono text-slate-400 uppercase block">Approximate Vertex Cover</span>
          <span className="text-xs font-mono text-cyan-400 font-bold">Cover Size = {result.coverSize}</span>
        </div>
        {result.isVerifiedCover && (
          <span className="inline-flex items-center gap-1.5 text-xs px-3 py-1 rounded-full bg-emerald-500/10 text-emerald-400 border border-emerald-500/30 font-mono font-bold">
            <CheckCircle2 className="h-4 w-4" /> VERIFIED VERTEX COVER (|C| ≤ 2 · OPT)
          </span>
        )}
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div className="bg-slate-950 p-4 rounded-lg border border-slate-800/80 space-y-2">
          <h3 className="text-xs font-mono text-slate-400 uppercase font-bold">Approximate Cover Set C:</h3>
          <div className="flex flex-wrap gap-2">
            {result.cover?.map((v) => (
              <span key={v} className="px-3 py-1 rounded bg-cyan-500/20 text-cyan-300 border border-cyan-500/40 font-mono text-xs font-bold">
                {v}
              </span>
            ))}
          </div>
        </div>

        <div className="bg-slate-950 p-4 rounded-lg border border-slate-800/80 space-y-2">
          <h3 className="text-xs font-mono text-slate-400 uppercase font-bold">Selected Matching Edges ({result.selectedEdges?.length}):</h3>
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
  ) : null;

  return (
    <AlgorithmPageLayout
      moduleTag="DSA MODULE 5 · CO5"
      title="Vertex Cover 2-Approximation Algorithm"
      description="Maximal-matching based 2-approximation algorithm guaranteeing that the returned cover size |C| is at most twice the optimal minimum vertex cover size OPT (|C| ≤ 2 · OPT)."
      inputForm={inputForm}
      onRun={handleRun}
      onReset={handleReset}
      loading={loading}
      error={error}
      result={result}
      resultView={resultView}
      railwayNote="Used for fast conflict-graph monitoring station placement. The maximal-matching 2-approximation guarantees |C| <= 2 * OPT in polynomial time."
    />
  );
};
