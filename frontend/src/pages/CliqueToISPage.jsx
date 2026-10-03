import React, { useState } from 'react';
import { runCliqueToIndependentSet } from '../api/m5Api';
import { AlgorithmPageLayout } from '../components/layout/AlgorithmPageLayout';
import { ArrowRight, CheckCircle2 } from 'lucide-react';

export const CliqueToISPage = () => {
  const [verticesInput, setVerticesInput] = useState('V1, V2, V3, V4');
  const [edgesInput, setEdgesInput] = useState('V1-V2\nV2-V3\nV3-V4');
  const [cliqueSizeInput, setCliqueSizeInput] = useState('2');

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
        cliqueSize: Number(cliqueSizeInput),
        traceEnabled: true,
      };

      const res = await runCliqueToIndependentSet(payload);
      setResult(res.data);
    } catch (err) {
      setError(err.response?.data?.error?.message || err.message || 'Error running reduction');
    } finally {
      setLoading(false);
    }
  };

  const handleReset = () => {
    setVerticesInput('V1, V2, V3, V4');
    setEdgesInput('V1-V2\nV2-V3\nV3-V4');
    setCliqueSizeInput('2');
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
        <label className="text-xs font-mono text-slate-300 font-medium">Target Clique Size k</label>
        <input
          type="number"
          value={cliqueSizeInput}
          onChange={(e) => setCliqueSizeInput(e.target.value)}
          className="w-full bg-slate-950 border border-slate-800 rounded-lg px-3 py-2 text-xs text-slate-200 font-mono focus:outline-none focus:border-cyan-500"
        />
      </div>
    </>
  );

  const resultView = result ? (
    <div className="space-y-5">
      <div className="flex items-center justify-between bg-slate-950 p-4 rounded-lg border border-slate-800">
        <div>
          <span className="text-xs font-mono text-slate-400 uppercase block">Transformation Result</span>
          <span className="text-xs font-mono text-cyan-400 font-bold">Target Independent Set Size = {result.independentSetSize}</span>
        </div>
        <span className="inline-flex items-center gap-1.5 text-xs px-3 py-1 rounded-full bg-emerald-500/10 text-emerald-400 border border-emerald-500/30 font-mono font-bold">
          <CheckCircle2 className="h-4 w-4" /> Graph Complement Constructed
        </span>
      </div>

      <div className="space-y-2">
        <h3 className="text-xs font-mono text-slate-400 uppercase font-bold">Transformed Complement Edges ({result.transformedEdges?.length}):</h3>
        <div className="bg-slate-950 p-3 rounded-lg border border-slate-800/80 max-h-48 overflow-y-auto font-mono text-xs text-slate-400 space-y-1">
          {result.transformedEdges?.map((e, idx) => (
            <div key={idx} className="flex items-center gap-2">
              <span className="text-cyan-400">{e[0]}</span>
              <ArrowRight className="h-3 w-3 text-slate-600" />
              <span className="text-cyan-400">{e[1]}</span>
            </div>
          ))}
        </div>
      </div>
    </div>
  ) : null;

  return (
    <AlgorithmPageLayout
      moduleTag="DSA MODULE 5 · CO5"
      title="CLIQUE to Independent Set Transformation"
      description="Graph complementation mapping a Clique instance (G, k) to an Independent Set instance (G_complement, k)."
      inputForm={inputForm}
      onRun={handleRun}
      onReset={handleReset}
      loading={loading}
      error={error}
      result={result}
      resultView={resultView}
      railwayNote="Graph complementation transforms clique discovery into non-adjacent node independent set identification."
    />
  );
};
