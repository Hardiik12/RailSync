import React, { useState } from 'react';
import { runSat } from '../api/m5Api';
import { AlgorithmPageLayout } from '../components/layout/AlgorithmPageLayout';
import { CheckCircle2, XCircle } from 'lucide-react';

export const SatPage = () => {
  const [variablesInput, setVariablesInput] = useState('T1_P1, T1_P2, T2_P1');
  const [clausesInput, setClausesInput] = useState('T1_P1\n!T1_P1, T1_P2\n!T2_P1');
  const [traceEnabled, setTraceEnabled] = useState(true);

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [result, setResult] = useState(null);

  const presets = [
    {
      label: 'Satisfiable Request',
      onClick: () => {
        setVariablesInput('T1_P1, T1_P2, T2_P1');
        setClausesInput('T1_P1\n!T1_P1, T1_P2\n!T2_P1');
      },
    },
    {
      label: 'Direct Contradiction',
      onClick: () => {
        setVariablesInput('PLATFORM_1, PLATFORM_2');
        setClausesInput('PLATFORM_1\n!PLATFORM_1\nPLATFORM_2');
      },
    },
    {
      label: 'Service Conflict',
      onClick: () => {
        setVariablesInput('EXPRESS_A, FREIGHT_B, LOCAL_C');
        setClausesInput('EXPRESS_A, FREIGHT_B\n!EXPRESS_A, LOCAL_C\n!FREIGHT_B, !LOCAL_C');
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

      const res = await runSat(payload);
      setResult(res.data);
    } catch (err) {
      setError(err.response?.data?.error?.message || err.message || 'Error running SAT algorithm');
    } finally {
      setLoading(false);
    }
  };

  const handleReset = () => {
    setVariablesInput('T1_P1, T1_P2, T2_P1');
    setClausesInput('T1_P1\n!T1_P1, T1_P2\n!T2_P1');
    setResult(null);
    setError(null);
  };

  const inputForm = (
    <>
      <div className="space-y-1.5">
        <label className="text-xs font-mono text-slate-300 font-medium">Boolean Variables (Comma-separated)</label>
        <input
          type="text"
          value={variablesInput}
          onChange={(e) => setVariablesInput(e.target.value)}
          className="w-full bg-slate-950 border border-slate-800 rounded-lg px-3 py-2 text-xs text-slate-200 font-mono focus:outline-none focus:border-cyan-500"
          placeholder="T1_P1, T1_P2, T2_P1"
        />
        <p className="text-[11px] text-slate-500">Variables representing train/platform states.</p>
      </div>

      <div className="space-y-1.5">
        <label className="text-xs font-mono text-slate-300 font-medium">Clauses (One clause per line, comma-separated literals)</label>
        <textarea
          value={clausesInput}
          onChange={(e) => setClausesInput(e.target.value)}
          rows={4}
          className="w-full bg-slate-950 border border-slate-800 rounded-lg px-3 py-2 text-xs text-slate-200 font-mono focus:outline-none focus:border-cyan-500"
          placeholder="T1_P1&#10;!T1_P1, T1_P2&#10;!T2_P1"
        />
        <p className="text-[11px] text-slate-500">Prefix literals with ! or ~ for negation.</p>
      </div>

      <div className="pt-1">
        <label className="flex items-center gap-2 text-xs text-slate-300 cursor-pointer">
          <input
            type="checkbox"
            checked={traceEnabled}
            onChange={(e) => setTraceEnabled(e.target.checked)}
            className="rounded bg-slate-950 border-slate-800 text-cyan-500 focus:ring-cyan-500"
          />
          <span>Enable DPLL Execution Tracing</span>
        </label>
      </div>
    </>
  );

  const resultView = result ? (
    <div className="space-y-5">
      <div className="flex items-center justify-between bg-slate-950 p-4 rounded-lg border border-slate-800">
        <span className="text-xs font-mono text-slate-400 uppercase">SAT Solver Verdict:</span>
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
          <h3 className="text-xs font-mono text-slate-400 uppercase font-bold">Satisfying Variable Assignment:</h3>
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
      title="Boolean Satisfiability (SAT) Solver"
      description="Backtracking DPLL algorithm with unit propagation and pure literal elimination to evaluate operational constraint satisfaction."
      presets={presets}
      inputForm={inputForm}
      onRun={handleRun}
      onReset={handleReset}
      loading={loading}
      error={error}
      result={result}
      resultView={resultView}
      railwayNote="SAT formulas model operational constraint satisfaction (e.g. checking whether platform assignments and departure rules can be simultaneously satisfied)."
    />
  );
};
