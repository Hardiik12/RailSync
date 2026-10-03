import React, { useState } from 'react';
import { runSat } from '../api/m5Api';
import { CheckSquare, Play, Clock, Cpu, HelpCircle, Layers, CheckCircle2, XCircle } from 'lucide-react';

export const SatPage = () => {
  const [variablesInput, setVariablesInput] = useState('T1_P1, T1_P2, T2_P1');
  const [clausesInput, setClausesInput] = useState('T1_P1\n!T1_P1, T1_P2\n!T2_P1');
  const [traceEnabled, setTraceEnabled] = useState(true);

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [result, setResult] = useState(null);

  const handlePresetScenario = (type) => {
    if (type === 'satisfiable') {
      setVariablesInput('T1_P1, T1_P2, T2_P1');
      setClausesInput('T1_P1\n!T1_P1, T1_P2\n!T2_P1');
    } else if (type === 'unsatisfiable') {
      setVariablesInput('PLATFORM_1, PLATFORM_2');
      setClausesInput('PLATFORM_1\n!PLATFORM_1\nPLATFORM_2');
    } else if (type === 'conflict') {
      setVariablesInput('EXPRESS_A, FREIGHT_B, LOCAL_C');
      setClausesInput('EXPRESS_A, FREIGHT_B\n!EXPRESS_A, LOCAL_C\n!FREIGHT_B, !LOCAL_C');
    }
  };

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

  return (
    <div className="p-8 max-w-7xl mx-auto space-y-8">
      {/* Header */}
      <div className="space-y-3">
        <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-purple-500/10 border border-purple-500/20 text-purple-400 text-xs font-mono">
          <CheckSquare className="h-3.5 w-3.5" />
          <span>Module 5 — SAT Solver (DPLL)</span>
        </div>
        <h1 className="text-3xl font-bold text-slate-100 tracking-tight">
          Boolean Satisfiability (SAT) Solver
        </h1>
        <p className="text-slate-400 max-w-3xl text-sm leading-relaxed">
          Backtracking DPLL algorithm with unit propagation and pure literal elimination to evaluate operational constraint satisfaction.
        </p>
      </div>

      {/* Preset Scenarios */}
      <div className="bg-slate-900/60 border border-slate-800 rounded-xl p-4 flex flex-wrap items-center gap-3">
        <span className="text-xs font-mono text-slate-400 uppercase tracking-wider">Presets:</span>
        <button
          onClick={() => handlePresetScenario('satisfiable')}
          className="px-3 py-1.5 text-xs font-medium rounded-lg bg-slate-800 text-slate-200 hover:bg-slate-700 border border-slate-700 transition-all"
        >
          Satisfiable Platform Request
        </button>
        <button
          onClick={() => handlePresetScenario('unsatisfiable')}
          className="px-3 py-1.5 text-xs font-medium rounded-lg bg-slate-800 text-slate-200 hover:bg-slate-700 border border-slate-700 transition-all"
        >
          Unsatisfiable Direct Contradiction
        </button>
        <button
          onClick={() => handlePresetScenario('conflict')}
          className="px-3 py-1.5 text-xs font-medium rounded-lg bg-slate-800 text-slate-200 hover:bg-slate-700 border border-slate-700 transition-all"
        >
          Complex Service Conflict
        </button>
      </div>

      {/* Input Section */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div className="bg-slate-900/80 border border-slate-800 rounded-xl p-6 space-y-4">
          <h2 className="text-base font-bold text-slate-200">Variables (Comma-separated)</h2>
          <input
            type="text"
            value={variablesInput}
            onChange={(e) => setVariablesInput(e.target.value)}
            className="w-full bg-slate-950 border border-slate-800 rounded-lg px-4 py-2.5 text-sm text-slate-200 font-mono focus:outline-none focus:border-purple-500"
            placeholder="T1_P1, T1_P2, T2_P1"
          />
          <p className="text-xs text-slate-400">List of boolean variables representing operational states.</p>
        </div>

        <div className="bg-slate-900/80 border border-slate-800 rounded-xl p-6 space-y-4">
          <h2 className="text-base font-bold text-slate-200">Clauses (One clause per line, comma-separated literals)</h2>
          <textarea
            value={clausesInput}
            onChange={(e) => setClausesInput(e.target.value)}
            rows={4}
            className="w-full bg-slate-950 border border-slate-800 rounded-lg px-4 py-2.5 text-sm text-slate-200 font-mono focus:outline-none focus:border-purple-500"
            placeholder="T1_P1&#10;!T1_P1, T1_P2&#10;!T2_P1"
          />
          <p className="text-xs text-slate-400">Prefix literals with ! or ~ for negation.</p>
        </div>
      </div>

      {/* Controls */}
      <div className="flex items-center justify-between bg-slate-900/80 border border-slate-800 rounded-xl p-4">
        <label className="flex items-center gap-2 text-xs text-slate-300 cursor-pointer">
          <input
            type="checkbox"
            checked={traceEnabled}
            onChange={(e) => setTraceEnabled(e.target.checked)}
            className="rounded bg-slate-950 border-slate-800 text-purple-500 focus:ring-purple-500"
          />
          <span>Enable Step-by-Step DPLL Execution Tracing</span>
        </label>

        <button
          onClick={handleRun}
          disabled={loading}
          className="inline-flex items-center gap-2 px-6 py-2.5 text-xs font-semibold rounded-lg bg-purple-500 text-white hover:bg-purple-600 disabled:opacity-50 transition-all shadow-lg shadow-purple-500/20"
        >
          <Play className="h-4 w-4" />
          <span>{loading ? 'Solving...' : 'Run SAT Solver'}</span>
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
              <span>Satisfaction Result</span>
              {result.satisfiable ? (
                <span className="inline-flex items-center gap-1 text-xs px-2.5 py-1 rounded-full bg-emerald-500/10 text-emerald-400 border border-emerald-500/20">
                  <CheckCircle2 className="h-3.5 w-3.5" /> SATISFIABLE
                </span>
              ) : (
                <span className="inline-flex items-center gap-1 text-xs px-2.5 py-1 rounded-full bg-rose-500/10 text-rose-400 border border-rose-500/20">
                  <XCircle className="h-3.5 w-3.5" /> UNSATISFIABLE
                </span>
              )}
            </h2>

            {result.satisfiable && result.assignment && (
              <div className="space-y-2">
                <h3 className="text-xs font-mono text-slate-400 uppercase">Satisfying Variable Assignment:</h3>
                <div className="grid grid-cols-2 md:grid-cols-4 gap-3">
                  {Object.entries(result.assignment).map(([varName, val]) => (
                    <div
                      key={varName}
                      className="bg-slate-950 p-3 rounded-lg border border-slate-800 flex justify-between items-center"
                    >
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
                  <span className="text-purple-400 text-sm font-bold">{result.complexity?.timeComplexity || 'O(2^V)'}</span>
                </div>
                <div className="bg-slate-950 p-3 rounded-lg border border-slate-800">
                  <span className="text-slate-400 block text-[10px]">SPACE</span>
                  <span className="text-purple-400 text-sm font-bold">{result.complexity?.spaceComplexity || 'O(V + C)'}</span>
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
                <span>DPLL Execution Trace</span>
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
              <span>Railway Domain Application & Limitations</span>
            </h2>
            <p className="text-xs text-slate-300 leading-relaxed">
              SAT formulas model operational constraint satisfaction (e.g. checking whether platform assignments and departure rules can be simultaneously satisfied).
              <strong className="text-purple-300"> Note:</strong> RailSync demonstrates SAT as an academic complexity model and bounded analytical tool; it does not claim to solve optimal real-world railway dispatching at scale.
            </p>
          </div>
        </div>
      )}
    </div>
  );
};
