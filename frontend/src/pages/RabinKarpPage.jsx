import React, { useState } from 'react';
import { runRabinKarpSearch } from '../api/m1Api';
import { Hash, Play, RotateCcw, Activity, ShieldAlert, CheckCircle2 } from 'lucide-react';

export const RabinKarpPage = () => {
  const [text, setText] = useState('INDIAN_RAILWAYS_EXPRESS');
  const [pattern, setPattern] = useState('RAIL');
  const [primeModulus, setPrimeModulus] = useState(1000000007);
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const [currentStepIndex, setCurrentStepIndex] = useState(0);

  const handleRun = async (e) => {
    e?.preventDefault();
    setLoading(true);
    setError(null);
    try {
      const res = await runRabinKarpSearch({
        text,
        pattern,
        primeModulus: Number(primeModulus),
        traceEnabled: true,
        maxTraceSteps: 500,
      });
      setResult(res.data);
      setCurrentStepIndex(0);
    } catch (err) {
      setError(err.message || 'Execution failed');
    } finally {
      setLoading(false);
    }
  };

  const currentStep = result?.trace?.[currentStepIndex];

  return (
    <div className="p-8 max-w-7xl mx-auto space-y-8">
      {/* Header */}
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4 border-b border-slate-800 pb-6">
        <div>
          <div className="flex items-center gap-2 text-purple-400 font-mono text-xs mb-1">
            <Hash className="h-4 w-4" />
            <span>M1 String Algorithms — CO1</span>
          </div>
          <h1 className="text-2xl font-bold text-slate-100">Rabin-Karp Rolling Hash Visualizer</h1>
          <p className="text-xs text-slate-400 mt-1">
            Manual polynomial rolling hash pattern search with character verification on hash match.
          </p>
        </div>

        <div className="flex items-center gap-3">
          <div className="px-3 py-1.5 rounded-lg bg-slate-900 border border-slate-800 text-xs font-mono text-slate-300">
            Time: <span className="text-purple-400 font-bold">O(n + m) avg</span>
          </div>
          <div className="px-3 py-1.5 rounded-lg bg-slate-900 border border-slate-800 text-xs font-mono text-slate-300">
            Space: <span className="text-purple-400 font-bold">O(1)</span>
          </div>
        </div>
      </div>

      {/* Input Form */}
      <form onSubmit={handleRun} className="bg-slate-900/80 border border-slate-800 rounded-xl p-6 space-y-4">
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div>
            <label className="block text-xs font-mono uppercase text-slate-400 mb-2">Input Text</label>
            <input
              type="text"
              value={text}
              onChange={(e) => setText(e.target.value)}
              className="w-full bg-slate-950 border border-slate-800 rounded-lg px-4 py-2 text-sm text-slate-100 font-mono focus:outline-none focus:border-purple-500/50"
              required
            />
          </div>
          <div>
            <label className="block text-xs font-mono uppercase text-slate-400 mb-2">Pattern</label>
            <input
              type="text"
              value={pattern}
              onChange={(e) => setPattern(e.target.value)}
              className="w-full bg-slate-950 border border-slate-800 rounded-lg px-4 py-2 text-sm text-slate-100 font-mono focus:outline-none focus:border-purple-500/50"
              required
            />
          </div>
          <div>
            <label className="block text-xs font-mono uppercase text-slate-400 mb-2">
              Prime Modulus (e.g. 13 for collision demo)
            </label>
            <input
              type="number"
              value={primeModulus}
              onChange={(e) => setPrimeModulus(e.target.value)}
              className="w-full bg-slate-950 border border-slate-800 rounded-lg px-4 py-2 text-sm text-slate-100 font-mono focus:outline-none focus:border-purple-500/50"
              required
            />
          </div>
        </div>

        <div className="flex justify-end gap-3 pt-2">
          <button
            type="submit"
            disabled={loading}
            className="flex items-center gap-2 px-5 py-2.5 rounded-lg bg-purple-500/10 border border-purple-500/30 text-purple-300 hover:bg-purple-500/20 text-xs font-semibold transition-all disabled:opacity-50"
          >
            <Play className="h-4 w-4" />
            <span>{loading ? 'Calculating...' : 'Run Rabin-Karp'}</span>
          </button>
        </div>
      </form>

      {error && (
        <div className="p-4 rounded-xl bg-rose-500/10 border border-rose-500/30 text-rose-400 text-xs font-mono">
          {error}
        </div>
      )}

      {result && (
        <div className="space-y-6">
          {/* Metrics Overview */}
          <div className="grid grid-cols-2 md:grid-cols-5 gap-4">
            <div className="bg-slate-900/60 border border-slate-800/80 p-4 rounded-xl">
              <span className="text-[10px] font-mono uppercase text-slate-400 block">Matches Found</span>
              <span className="text-xl font-bold font-mono text-purple-400">{result.matchCount}</span>
            </div>
            <div className="bg-slate-900/60 border border-slate-800/80 p-4 rounded-xl">
              <span className="text-[10px] font-mono uppercase text-slate-400 block">Pattern Hash</span>
              <span className="text-xl font-bold font-mono text-purple-300">{result.patternHash}</span>
            </div>
            <div className="bg-slate-900/60 border border-slate-800/80 p-4 rounded-xl">
              <span className="text-[10px] font-mono uppercase text-slate-400 block">Hash Verifications</span>
              <span className="text-xl font-bold font-mono text-slate-200">{result.hashVerifications}</span>
            </div>
            <div className="bg-slate-900/60 border border-slate-800/80 p-4 rounded-xl">
              <span className="text-[10px] font-mono uppercase text-slate-400 block">Hash Collisions</span>
              <span className={`text-xl font-bold font-mono ${result.hashCollisions > 0 ? 'text-amber-400' : 'text-slate-400'}`}>
                {result.hashCollisions}
              </span>
            </div>
            <div className="bg-slate-900/60 border border-slate-800/80 p-4 rounded-xl">
              <span className="text-[10px] font-mono uppercase text-slate-400 block">Total Operations</span>
              <span className="text-xl font-bold font-mono text-slate-200">{result.operationCount}</span>
            </div>
          </div>

          {/* Matches List */}
          <div className="bg-slate-900/80 border border-slate-800 rounded-xl p-6 space-y-4">
            <h3 className="text-sm font-bold text-slate-200 font-mono uppercase">Verified Match Positions</h3>
            {result.matches && result.matches.length > 0 ? (
              <div className="flex flex-wrap gap-2">
                {result.matches.map((pos, idx) => (
                  <div
                    key={idx}
                    className="px-3 py-1.5 rounded-lg bg-purple-500/10 border border-purple-500/30 text-purple-300 text-xs font-mono flex items-center gap-2"
                  >
                    <CheckCircle2 className="h-3.5 w-3.5 text-purple-400" />
                    <span>Index {pos}</span>
                  </div>
                ))}
              </div>
            ) : (
              <p className="text-xs text-slate-400">No pattern matches found in input text.</p>
            )}
          </div>

          {/* Trace Viewer */}
          {result.trace && result.trace.length > 0 && (
            <div className="bg-slate-900/80 border border-slate-800 rounded-xl p-6 space-y-4">
              <div className="flex items-center justify-between">
                <h3 className="text-sm font-bold text-slate-200 font-mono uppercase">Rolling Hash Step Trace</h3>
                <div className="flex items-center gap-2">
                  <button
                    onClick={() => setCurrentStepIndex(Math.max(0, currentStepIndex - 1))}
                    disabled={currentStepIndex === 0}
                    className="px-3 py-1 rounded bg-slate-800 hover:bg-slate-700 text-xs font-mono text-slate-300 disabled:opacity-40"
                  >
                    Prev
                  </button>
                  <span className="text-xs font-mono text-slate-400 px-2">
                    Step {currentStepIndex + 1} of {result.trace.length}
                  </span>
                  <button
                    onClick={() => setCurrentStepIndex(Math.min(result.trace.length - 1, currentStepIndex + 1))}
                    disabled={currentStepIndex === result.trace.length - 1}
                    className="px-3 py-1 rounded bg-slate-800 hover:bg-slate-700 text-xs font-mono text-slate-300 disabled:opacity-40"
                  >
                    Next
                  </button>
                </div>
              </div>

              {currentStep && (
                <div className="p-4 rounded-xl bg-slate-950 border border-slate-800 space-y-2">
                  <div className="flex items-center justify-between text-xs font-mono">
                    <span className="text-purple-400 font-bold">Action: {currentStep.action}</span>
                    <span className="text-slate-500">Step #{currentStep.step}</span>
                  </div>
                  <p className="text-xs text-slate-300 font-mono">{currentStep.description}</p>
                  {currentStep.state && (
                    <pre className="text-[11px] font-mono text-slate-400 bg-slate-900 p-3 rounded-lg border border-slate-800 overflow-x-auto">
                      {JSON.stringify(currentStep.state, null, 2)}
                    </pre>
                  )}
                </div>
              )}
            </div>
          )}
        </div>
      )}
    </div>
  );
};
