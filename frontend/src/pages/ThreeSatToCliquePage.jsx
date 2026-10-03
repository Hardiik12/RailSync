import React, { useState } from 'react';
import { runThreeSatToClique } from '../api/m5Api';
import { GitCommit, Play, Clock, Cpu, HelpCircle, Layers, CheckCircle2, ArrowRight } from 'lucide-react';

export const ThreeSatToCliquePage = () => {
  const [variablesInput, setVariablesInput] = useState('A, B, C');
  const [clausesInput, setClausesInput] = useState('A, !B, C\n!A, B, C');

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [result, setResult] = useState(null);

  const handleRun = async () => {
    setLoading(true);
    setError(null);
    setResult(null);

    try {
      const vars = variablesInput.split(',').map((s) => s.trim()).filter(Boolean);
      const clauseLines = clausesInput.split('\n').map((l) => l.trim()).filter(Boolean);
      const stringClauses = clauseLines.map((line) => line.split(',').map((s) => s.trim()).filter(Boolean));

      const payload = {
        variables: vars,
        stringClauses: stringClauses,
        traceEnabled: true,
        maxTraceSteps: 50,
      };

      const res = await runThreeSatToClique(payload);
      setResult(res.data);
    } catch (err) {
      setError(err.response?.data?.error?.message || err.message || 'Error running 3-SAT to CLIQUE reduction');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="p-8 max-w-7xl mx-auto space-y-8">
      {/* Header */}
      <div className="space-y-3">
        <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-purple-500/10 border border-purple-500/20 text-purple-400 text-xs font-mono">
          <GitCommit className="h-3.5 w-3.5" />
          <span>Module 5 — Polynomial Reduction (3-SAT → CLIQUE)</span>
        </div>
        <h1 className="text-3xl font-bold text-slate-100 tracking-tight">
          3-SAT to CLIQUE Reduction
        </h1>
        <p className="text-slate-400 max-w-3xl text-sm leading-relaxed">
          Polynomial transformation mapping a 3-SAT formula with m clauses to a graph G where a clique of size k = m exists if and only if the formula is satisfiable.
        </p>
      </div>

      {/* Input Section */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div className="bg-slate-900/80 border border-slate-800 rounded-xl p-6 space-y-4">
          <h2 className="text-base font-bold text-slate-200">3-SAT Variables</h2>
          <input
            type="text"
            value={variablesInput}
            onChange={(e) => setVariablesInput(e.target.value)}
            className="w-full bg-slate-950 border border-slate-800 rounded-lg px-4 py-2.5 text-sm text-slate-200 font-mono focus:outline-none focus:border-purple-500"
          />
        </div>

        <div className="bg-slate-900/80 border border-slate-800 rounded-xl p-6 space-y-4">
          <h2 className="text-base font-bold text-slate-200">3-Literal Clauses (m clauses)</h2>
          <textarea
            value={clausesInput}
            onChange={(e) => setClausesInput(e.target.value)}
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
          <span>{loading ? 'Reducing...' : 'Run 3-SAT → CLIQUE Reduction'}</span>
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
            <div className="flex items-center justify-between">
              <h2 className="text-base font-bold text-slate-200 flex items-center gap-2">
                <span>Reduction Mapping Output</span>
                <span className="text-xs font-mono text-purple-400">Target Clique Size k = {result.targetCliqueSize}</span>
              </h2>
              {result.satisfiable ? (
                <span className="inline-flex items-center gap-1 text-xs px-2.5 py-1 rounded-full bg-emerald-500/10 text-emerald-400 border border-emerald-500/20">
                  <CheckCircle2 className="h-3.5 w-3.5" /> SATISFIABLE (Clique Found)
                </span>
              ) : (
                <span className="inline-flex items-center gap-1 text-xs px-2.5 py-1 rounded-full bg-rose-500/10 text-rose-400 border border-rose-500/20">
                  UNSATISFIABLE (No Clique of Size {result.targetCliqueSize})
                </span>
              )}
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div className="space-y-2">
                <h3 className="text-xs font-mono text-slate-400 uppercase">Constructed Vertices ({result.vertices?.length}):</h3>
                <div className="bg-slate-950 p-4 rounded-lg border border-slate-800 max-h-48 overflow-y-auto space-y-1 text-xs font-mono text-slate-300">
                  {result.vertices?.map((v) => (
                    <div
                      key={v}
                      className={`px-2 py-1 rounded border flex justify-between items-center ${
                        result.cliqueFound?.includes(v)
                          ? 'bg-purple-500/20 text-purple-300 border-purple-500/40 font-bold'
                          : 'bg-slate-900/60 border-slate-800 text-slate-400'
                      }`}
                    >
                      <span>{v}</span>
                      <span className="text-[10px] text-slate-500">{result.mapping?.[v]}</span>
                    </div>
                  ))}
                </div>
              </div>

              <div className="space-y-2">
                <h3 className="text-xs font-mono text-slate-400 uppercase">Compatibility Edges ({result.edges?.length}):</h3>
                <div className="bg-slate-950 p-4 rounded-lg border border-slate-800 max-h-48 overflow-y-auto font-mono text-xs text-slate-400 space-y-1">
                  {result.edges?.map((e, idx) => (
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

          {/* Metrics */}
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div className="bg-slate-900/80 border border-slate-800 rounded-xl p-6 space-y-3">
              <h2 className="text-sm font-bold text-slate-200 flex items-center gap-2">
                <Cpu className="h-4 w-4 text-purple-400" />
                <span>Theoretical Reduction Complexity</span>
              </h2>
              <div className="grid grid-cols-2 gap-4 text-xs font-mono">
                <div className="bg-slate-950 p-3 rounded-lg border border-slate-800">
                  <span className="text-slate-400 block text-[10px]">TIME</span>
                  <span className="text-purple-400 text-sm font-bold">{result.complexity?.timeComplexity || 'O(m^2)'}</span>
                </div>
                <div className="bg-slate-950 p-3 rounded-lg border border-slate-800">
                  <span className="text-slate-400 block text-[10px]">SPACE</span>
                  <span className="text-purple-400 text-sm font-bold">{result.complexity?.spaceComplexity || 'O(m^2)'}</span>
                </div>
              </div>
            </div>

            <div className="bg-slate-900/80 border border-slate-800 rounded-xl p-6 space-y-3">
              <h2 className="text-sm font-bold text-slate-200 flex items-center gap-2">
                <Clock className="h-4 w-4 text-purple-400" />
                <span>Measured Execution</span>
              </h2>
              <div className="grid grid-cols-2 gap-4 text-xs font-mono">
                <div className="bg-slate-950 p-3 rounded-lg border border-slate-800">
                  <span className="text-slate-400 block text-[10px]">RUNTIME</span>
                  <span className="text-emerald-400 text-sm font-bold">
                    {(result.executionTimeNanos / 1000000).toFixed(3)} ms
                  </span>
                </div>
                <div className="bg-slate-950 p-3 rounded-lg border border-slate-800">
                  <span className="text-slate-400 block text-[10px]">EDGE OPERATIVE EVALS</span>
                  <span className="text-purple-400 text-sm font-bold">{result.operationCount} ops</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};
