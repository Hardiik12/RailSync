import React, { useState } from 'react';
import { runThreeSat } from '../api/m5Api';
import { Target, Play, Clock, Cpu, HelpCircle, Layers, CheckCircle2, XCircle } from 'lucide-react';

export const ThreeSatPage = () => {
  const [variablesInput, setVariablesInput] = useState('A, B, C');
  const [clausesInput, setClausesInput] = useState('A, !B, C\n!A, B, C');
  const [traceEnabled, setTraceEnabled] = useState(true);

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [result, setResult] = useState(null);

  const handlePresetScenario = (type) => {
    if (type === 'valid-sat') {
      setVariablesInput('A, B, C');
      setClausesInput('A, !B, C\n!A, B, C');
    } else if (type === 'valid-unsat') {
      setVariablesInput('A, B, C');
      setClausesInput('A, B, C\nA, B, !C\nA, !B, C\nA, !B, !C\n!A, B, C\n!A, B, !C\n!A, !B, C\n!A, !B, !C');
    } else if (type === 'invalid-clause') {
      setVariablesInput('A, B, C');
      setClausesInput('A, B\n!A, B, C'); // Invalid clause size 2!
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

      const res = await runThreeSat(payload);
      setResult(res.data);
    } catch (err) {
      setError(err.response?.data?.error?.message || err.message || 'Error running 3-SAT algorithm');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="p-8 max-w-7xl mx-auto space-y-8">
      {/* Header */}
      <div className="space-y-3">
        <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-purple-500/10 border border-purple-500/20 text-purple-400 text-xs font-mono">
          <Target className="h-3.5 w-3.5" />
          <span>Module 5 — 3-SAT Solver</span>
        </div>
        <h1 className="text-3xl font-bold text-slate-100 tracking-tight">
          3-Satisfiability (3-SAT) Solver
        </h1>
        <p className="text-slate-400 max-w-3xl text-sm leading-relaxed">
          Canonical 3-literal CNF satisfiability solver. Enforces exactly 3 literals per clause and validates bounded decision constraints.
        </p>
      </div>

      {/* Preset Scenarios */}
      <div className="bg-slate-900/60 border border-slate-800 rounded-xl p-4 flex flex-wrap items-center gap-3">
        <span className="text-xs font-mono text-slate-400 uppercase tracking-wider">Presets:</span>
        <button
          onClick={() => handlePresetScenario('valid-sat')}
          className="px-3 py-1.5 text-xs font-medium rounded-lg bg-slate-800 text-slate-200 hover:bg-slate-700 border border-slate-700 transition-all"
        >
          Valid 3-SAT (Satisfiable)
        </button>
        <button
          onClick={() => handlePresetScenario('valid-unsat')}
          className="px-3 py-1.5 text-xs font-medium rounded-lg bg-slate-800 text-slate-200 hover:bg-slate-700 border border-slate-700 transition-all"
        >
          Valid 3-SAT (Unsatisfiable 8-Clause)
        </button>
        <button
          onClick={() => handlePresetScenario('invalid-clause')}
          className="px-3 py-1.5 text-xs font-medium rounded-lg bg-rose-950/40 text-rose-300 hover:bg-rose-900/50 border border-rose-800/60 transition-all"
        >
          Invalid Clause Size (Triggers INVALID_INPUT)
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
            placeholder="A, B, C"
          />
          <p className="text-xs text-slate-400">Comma-separated list of boolean variables.</p>
        </div>

        <div className="bg-slate-900/80 border border-slate-800 rounded-xl p-6 space-y-4">
          <h2 className="text-base font-bold text-slate-200">3-Literal Clauses (Exactly 3 literals per line)</h2>
          <textarea
            value={clausesInput}
            onChange={(e) => setClausesInput(e.target.value)}
            rows={4}
            className="w-full bg-slate-950 border border-slate-800 rounded-lg px-4 py-2.5 text-sm text-slate-200 font-mono focus:outline-none focus:border-purple-500"
            placeholder="A, !B, C&#10;!A, B, C"
          />
          <p className="text-xs text-slate-400">Each clause must contain exactly 3 literals.</p>
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
          <span>Enable Execution Tracing</span>
        </label>

        <button
          onClick={handleRun}
          disabled={loading}
          className="inline-flex items-center gap-2 px-6 py-2.5 text-xs font-semibold rounded-lg bg-purple-500 text-white hover:bg-purple-600 disabled:opacity-50 transition-all shadow-lg shadow-purple-500/20"
        >
          <Play className="h-4 w-4" />
          <span>{loading ? 'Solving...' : 'Run 3-SAT Solver'}</span>
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
              <span>3-SAT Satisfiability Status</span>
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
                <div className="grid grid-cols-3 md:grid-cols-4 gap-3">
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
        </div>
      )}
    </div>
  );
};
