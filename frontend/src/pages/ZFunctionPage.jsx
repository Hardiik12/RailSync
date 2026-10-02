import React, { useState } from 'react';
import { runZFunctionSearch } from '../api/m1Api';
import { Zap, Play, RotateCcw, Activity, Info, CheckCircle2, ChevronRight } from 'lucide-react';

export const ZFunctionPage = () => {
  const [text, setText] = useState('aabzaab');
  const [pattern, setPattern] = useState('aab');
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  // Step trace state
  const [currentStepIndex, setCurrentStepIndex] = useState(0);

  const handleRun = async (e) => {
    e?.preventDefault();
    setLoading(true);
    setError(null);
    try {
      const res = await runZFunctionSearch({
        text,
        pattern,
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
          <div className="flex items-center gap-2 text-emerald-400 font-mono text-xs mb-1">
            <Zap className="h-4 w-4" />
            <span>M1 String Algorithms — CO1</span>
          </div>
          <h1 className="text-2xl font-bold text-slate-100">Z-Function Algorithm Visualizer</h1>
          <p className="text-xs text-slate-400 mt-1">
            Manual linear-time Z-box technique for calculating substring match prefixes.
          </p>
        </div>

        <div className="flex items-center gap-3">
          <div className="px-3 py-1.5 rounded-lg bg-slate-900 border border-slate-800 text-xs font-mono text-slate-300">
            Time: <span className="text-emerald-400 font-bold">O(n)</span>
          </div>
          <div className="px-3 py-1.5 rounded-lg bg-slate-900 border border-slate-800 text-xs font-mono text-slate-300">
            Space: <span className="text-emerald-400 font-bold">O(n)</span>
          </div>
        </div>
      </div>

      {/* Input Form */}
      <form onSubmit={handleRun} className="bg-slate-900/80 border border-slate-800 rounded-xl p-6 space-y-4">
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <label className="block text-xs font-mono uppercase text-slate-400 mb-2">Input Text</label>
            <input
              type="text"
              value={text}
              onChange={(e) => setText(e.target.value)}
              className="w-full bg-slate-950 border border-slate-800 rounded-lg px-4 py-2 text-sm text-slate-100 font-mono focus:outline-none focus:border-emerald-500/50"
              placeholder="Enter text..."
              required
            />
          </div>
          <div>
            <label className="block text-xs font-mono uppercase text-slate-400 mb-2">
              Optional Pattern (for Pattern Search via P + $ + T)
            </label>
            <input
              type="text"
              value={pattern}
              onChange={(e) => setPattern(e.target.value)}
              className="w-full bg-slate-950 border border-slate-800 rounded-lg px-4 py-2 text-sm text-slate-100 font-mono focus:outline-none focus:border-emerald-500/50"
              placeholder="Optional pattern..."
            />
          </div>
        </div>

        <div className="flex justify-end gap-3 pt-2">
          <button
            type="submit"
            disabled={loading}
            className="flex items-center gap-2 px-5 py-2.5 rounded-lg bg-emerald-500/10 border border-emerald-500/30 text-emerald-300 hover:bg-emerald-500/20 text-xs font-semibold transition-all disabled:opacity-50"
          >
            <Play className="h-4 w-4" />
            <span>{loading ? 'Calculating...' : 'Run Z-Algorithm'}</span>
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
          <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
            <div className="bg-slate-900/60 border border-slate-800/80 p-4 rounded-xl">
              <span className="text-[10px] font-mono uppercase text-slate-400 block">Matches Found</span>
              <span className="text-xl font-bold font-mono text-emerald-400">{result.matchCount}</span>
            </div>
            <div className="bg-slate-900/60 border border-slate-800/80 p-4 rounded-xl">
              <span className="text-[10px] font-mono uppercase text-slate-400 block">Comparisons</span>
              <span className="text-xl font-bold font-mono text-slate-200">{result.operationCount}</span>
            </div>
            <div className="bg-slate-900/60 border border-slate-800/80 p-4 rounded-xl">
              <span className="text-[10px] font-mono uppercase text-slate-400 block">Execution Time</span>
              <span className="text-xl font-bold font-mono text-slate-200">
                {(result.executionTimeNanos / 1000000).toFixed(3)} ms
              </span>
            </div>
            <div className="bg-slate-900/60 border border-slate-800/80 p-4 rounded-xl">
              <span className="text-[10px] font-mono uppercase text-slate-400 block">Trace Steps</span>
              <span className="text-xl font-bold font-mono text-slate-200">{result.trace?.length || 0}</span>
            </div>
          </div>

          {/* Processed String & Z-Array Visualization */}
          <div className="bg-slate-900/80 border border-slate-800 rounded-xl p-6 space-y-4">
            <h3 className="text-sm font-bold text-slate-200 font-mono uppercase">Z-Array Visualization</h3>
            <p className="text-xs text-slate-400">
              Processed String: <span className="font-mono text-emerald-400">{result.processedString}</span>
            </p>

            <div className="flex flex-wrap gap-2 pt-2">
              {result.processedString.split('').map((char, idx) => {
                const zVal = result.zArray[idx];
                const isMatch = result.matches?.includes(idx - (pattern ? pattern.length + 1 : 0));
                const isCurrentIdx = currentStep?.state?.i === idx;
                const boxL = currentStep?.state?.L;
                const boxR = currentStep?.state?.R;
                const inBox = boxL !== undefined && boxR !== undefined && idx >= boxL && idx <= boxR;

                return (
                  <div
                    key={idx}
                    className={`flex flex-col items-center p-2 rounded-lg border text-xs font-mono transition-all min-w-[48px] ${
                      isCurrentIdx
                        ? 'bg-amber-500/20 border-amber-500/60 text-amber-300 scale-105 shadow-md shadow-amber-500/10'
                        : inBox
                        ? 'bg-emerald-500/10 border-emerald-500/40 text-emerald-300'
                        : isMatch
                        ? 'bg-cyan-500/20 border-cyan-500 text-cyan-300'
                        : 'bg-slate-950 border-slate-800 text-slate-300'
                    }`}
                  >
                    <span className="text-[10px] text-slate-500">{idx}</span>
                    <span className="text-base font-bold my-0.5">{char}</span>
                    <span className="text-[10px] px-1.5 py-0.5 rounded bg-slate-900 text-slate-400 font-bold border border-slate-800">
                      Z: {zVal}
                    </span>
                  </div>
                );
              })}
            </div>
          </div>

          {/* Trace Viewer Controls & Details */}
          {result.trace && result.trace.length > 0 && (
            <div className="bg-slate-900/80 border border-slate-800 rounded-xl p-6 space-y-4">
              <div className="flex items-center justify-between">
                <h3 className="text-sm font-bold text-slate-200 font-mono uppercase">Algorithm Execution Trace</h3>
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
                    <span className="text-emerald-400 font-bold">Action: {currentStep.action}</span>
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
