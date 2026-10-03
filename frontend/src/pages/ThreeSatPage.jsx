import React, { useState } from 'react';
import { runThreeSat } from '../api/m5Api';
import { AlgorithmPageLayout } from '../components/layout/AlgorithmPageLayout';
import { CheckCircle2, XCircle } from 'lucide-react';

export const ThreeSatPage = () => {
  const [variablesInput, setVariablesInput] = useState('A, B, C, D');
  const [clausesInput, setClausesInput] = useState('A, !B, C\n!A, B, D\n!C, !D, A');
  const [traceEnabled, setTraceEnabled] = useState(true);

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [result, setResult] = useState(null);

  const presets = [
    {
      label: 'Standard 3-SAT',
      onClick: () => {
        setVariablesInput('A, B, C, D');
        setClausesInput('A, !B, C\n!A, B, D\n!C, !D, A');
      },
    },
    {
      label: 'Unsatisfiable 3-SAT',
      onClick: () => {
        setVariablesInput('A, B, C');
        setClausesInput('A, A, A\n!A, !A, !A');
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
        traceEnabled,
        maxTraceSteps: 50,
      };

      const res = await runThreeSat(payload);
      setResult(res.data);
    } catch (err) {
      setError(err.response?.data?.error?.message || err.message || 'Error running 3-SAT algorithm');
    } finally {
      setLoading(false);
    }
  };

  const handleReset = () => {
    setVariablesInput('A, B, C, D');
    setClausesInput('A, !B, C\n!A, B, D\n!C, !D, A');
    setResult(null);
    setError(null);
  };

  const inputForm = (
    <>
      <div className="space-y-1.5">
        <label className="text-xs font-mono text-slate-300 font-medium">3-SAT Variables (Comma-separated)</label>
        <input
          type="text"
          value={variablesInput}
          onChange={(e) => setVariablesInput(e.target.value)}
          className="w-full bg-slate-950 border border-slate-800 rounded-lg px-3 py-2 text-xs text-slate-200 font-mono focus:outline-none focus:border-cyan-500"
          placeholder="A, B, C, D"
        />
      </div>

      <div className="space-y-1.5">
        <label className="text-xs font-mono text-slate-300 font-medium">3-Literal Clauses (Exactly 3 literals per line)</label>
        <textarea
          value={clausesInput}
          onChange={(e) => setClausesInput(e.target.value)}
          rows={4}
          className="w-full bg-slate-950 border border-slate-800 rounded-lg px-3 py-2 text-xs text-slate-200 font-mono focus:outline-none focus:border-cyan-500"
          placeholder="A, !B, C&#10;!A, B, D"
        />
        <p className="text-[11px] text-slate-500">Each clause must contain exactly 3 literals.</p>
      </div>

      <div className="pt-1">
        <label className="flex items-center gap-2 text-xs text-slate-300 cursor-pointer">
          <input
            type="checkbox"
            checked={traceEnabled}
            onChange={(e) => setTraceEnabled(e.target.checked)}
            className="rounded bg-slate-950 border-slate-800 text-cyan-500 focus:ring-cyan-500"
          />
          <span>Enable Step-by-Step Execution Tracing</span>
        </label>
      </div>
    </>
  );

  const resultView = result ? (
    <div className="space-y-5">
      <div className="flex items-center justify-between bg-slate-950 p-4 rounded-lg border border-slate-800">
        <span className="text-xs font-mono text-slate-400 uppercase">3-SAT Satisfaction Verdict:</span>
        {result.satisfiable ? (
          <span className="inline-flex items-center gap-1.5 text-xs px-3 py-1 rounded-full bg-emerald-500/10 text-emerald-400 border border-emerald-500/30 font-mono font-bold">
            <CheckCircle2 className="h-4 w-4" /> SATISFIABLE
          </span>
        ) : (
          <span className="inline-flex items-center gap-1.5 text-xs px-3 py-1 rounded-full bg-rose-500/10 text-rose-400 border border-rose-500/30 font-mono font-bold">
            <XCircle className="h-4 w-4" /> UNSATISFIABLE
          </span>
        )}
      </div>

      {result.satisfiable && result.assignment && (
        <div className="space-y-2">
          <h3 className="text-xs font-mono text-slate-400 uppercase font-bold">Variable Assignment:</h3>
          <div className="grid grid-cols-2 gap-3">
            {Object.entries(result.assignment).map(([varName, val]) => (
              <div key={varName} className="bg-slate-950 p-3 rounded-lg border border-slate-800/80 flex justify-between items-center">
                <span className="font-mono text-xs text-slate-300">{varName}</span>
                <span
                  className={`text-xs font-bold font-mono px-2 py-0.5 rounded ${
                    val ? 'bg-emerald-500/20 text-emerald-400' : 'bg-rose-500/20 text-rose-400'
                  }`}
                >
                  {val ? 'TRUE' : 'FALSE'}
                </span>
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  ) : null;

  return (
    <AlgorithmPageLayout
      moduleTag="DSA MODULE 5 · CO5"
      title="3-Satisfiability (3-SAT) Solver"
      description="DPLL implementation for formulas restricted to 3 literals per clause, foundational for NP-completeness reduction chains."
      presets={presets}
      inputForm={inputForm}
      onRun={handleRun}
      onReset={handleReset}
      loading={loading}
      error={error}
      result={result}
      resultView={resultView}
      railwayNote="3-SAT serves as the standard starting point for decision-problem reduction chains in railway scheduling constraint analysis."
    />
  );
};
