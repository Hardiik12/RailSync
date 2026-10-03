import React, { useState } from 'react';
import { runThreeSatToClique } from '../api/m5Api';
import { AlgorithmPageLayout } from '../components/layout/AlgorithmPageLayout';
import { CheckCircle2, ArrowRight } from 'lucide-react';

export const ThreeSatToCliquePage = () => {
  const [variablesInput, setVariablesInput] = useState('A, B, C');
  const [clausesInput, setClausesInput] = useState('A, !B, C\n!A, B, C');

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [result, setResult] = useState(null);

  const presets = [
    {
      label: '2-Clause Example',
      onClick: () => {
        setVariablesInput('A, B, C');
        setClausesInput('A, !B, C\n!A, B, C');
      },
    },
  ];

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

  const handleReset = () => {
    setVariablesInput('A, B, C');
    setClausesInput('A, !B, C\n!A, B, C');
    setResult(null);
    setError(null);
  };

  const inputForm = (
    <>
      <div className="space-y-1.5">
        <label className="text-xs font-mono text-slate-300 font-medium">3-SAT Variables</label>
        <input
          type="text"
          value={variablesInput}
          onChange={(e) => setVariablesInput(e.target.value)}
          className="w-full bg-slate-950 border border-slate-800 rounded-lg px-3 py-2 text-xs text-slate-200 font-mono focus:outline-none focus:border-cyan-500"
        />
      </div>

      <div className="space-y-1.5">
        <label className="text-xs font-mono text-slate-300 font-medium">3-Literal Clauses (m clauses)</label>
        <textarea
          value={clausesInput}
          onChange={(e) => setClausesInput(e.target.value)}
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
          <span className="text-xs font-mono text-slate-400 uppercase block">Reduction Result</span>
          <span className="text-xs font-mono text-cyan-400 font-bold">Target Clique Size k = {result.targetCliqueSize}</span>
        </div>
        {result.satisfiable ? (
          <span className="inline-flex items-center gap-1.5 text-xs px-3 py-1 rounded-full bg-emerald-500/10 text-emerald-400 border border-emerald-500/30 font-mono font-bold">
            <CheckCircle2 className="h-4 w-4" /> SATISFIABLE (Clique Found)
          </span>
        ) : (
          <span className="inline-flex items-center gap-1.5 text-xs px-3 py-1 rounded-full bg-rose-500/10 text-rose-400 border border-rose-500/30 font-mono font-bold">
            UNSATISFIABLE (No Clique)
          </span>
        )}
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div className="space-y-2">
          <h3 className="text-xs font-mono text-slate-400 uppercase font-bold">Constructed Vertices ({result.vertices?.length}):</h3>
          <div className="bg-slate-950 p-3 rounded-lg border border-slate-800/80 max-h-48 overflow-y-auto space-y-1 text-xs font-mono">
            {result.vertices?.map((v) => (
              <div
                key={v}
                className={`px-2.5 py-1 rounded border flex justify-between items-center ${
                  result.cliqueFound?.includes(v)
                    ? 'bg-cyan-500/20 text-cyan-300 border-cyan-500/40 font-bold'
                    : 'bg-slate-900/60 border-slate-800/80 text-slate-400'
                }`}
              >
                <span>{v}</span>
                <span className="text-[10px] text-slate-500">{result.mapping?.[v]}</span>
              </div>
            ))}
          </div>
        </div>

        <div className="space-y-2">
          <h3 className="text-xs font-mono text-slate-400 uppercase font-bold">Compatibility Edges ({result.edges?.length}):</h3>
          <div className="bg-slate-950 p-3 rounded-lg border border-slate-800/80 max-h-48 overflow-y-auto font-mono text-xs text-slate-400 space-y-1">
            {result.edges?.map((e, idx) => (
              <div key={idx} className="flex items-center gap-2">
                <span className="text-cyan-400">{e[0]}</span>
                <ArrowRight className="h-3 w-3 text-slate-600" />
                <span className="text-cyan-400">{e[1]}</span>
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
      title="3-SAT to CLIQUE Reduction"
      description="Polynomial transformation mapping a 3-SAT formula with m clauses to a graph G where a clique of size k = m exists if and only if the formula is satisfiable."
      presets={presets}
      inputForm={inputForm}
      onRun={handleRun}
      onReset={handleReset}
      loading={loading}
      error={error}
      result={result}
      resultView={resultView}
      railwayNote="Demonstrates polynomial-time transformation mapping clause satisfaction into compatibility graph clique identification."
    />
  );
};
