import React, { useState } from 'react';
import { runSAIS } from '../api/m2Api';
import { Zap, Play } from 'lucide-react';

export const SAISPage = () => {
  const [text, setText] = useState('DELHI_EXPRESS_SCHEDULE');
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [currentStepIndex, setCurrentStepIndex] = useState(0);

  const handleRun = async (e) => {
    e?.preventDefault();
    setLoading(true);
    setError(null);
    try {
      const res = await runSAIS({ text, traceEnabled: true, maxTraceSteps: 500 });
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
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4 border-b border-slate-800 pb-6">
        <div>
          <div className="flex items-center gap-2 text-emerald-400 font-mono text-xs mb-1">
            <Zap className="h-4 w-4" />
            <span>M2 Suffix Structures — CO2</span>
          </div>
          <h1 className="text-2xl font-bold text-slate-100">SA-IS (Induced Sorting) Visualizer</h1>
          <p className="text-xs text-slate-400 mt-1">
            Manual linear-time O(n) S/L classification and induced sorting algorithm.
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

      <form onSubmit={handleRun} className="bg-slate-900/80 border border-slate-800 rounded-xl p-6 space-y-4">
        <div>
          <label className="block text-xs font-mono uppercase text-slate-400 mb-2">Input Text</label>
          <input
            type="text"
            value={text}
            onChange={(e) => setText(e.target.value)}
            className="w-full bg-slate-950 border border-slate-800 rounded-lg px-4 py-2 text-sm text-slate-100 font-mono focus:outline-none focus:border-emerald-500/50"
            required
          />
        </div>
        <div className="flex justify-end pt-2">
          <button
            type="submit"
            disabled={loading}
            className="flex items-center gap-2 px-5 py-2.5 rounded-lg bg-emerald-500/10 border border-emerald-500/30 text-emerald-300 hover:bg-emerald-500/20 text-xs font-semibold transition-all disabled:opacity-50"
          >
            <Play className="h-4 w-4" />
            <span>{loading ? 'Executing SA-IS...' : 'Run SA-IS Algorithm'}</span>
          </button>
        </div>
      </form>

      {error && <div className="p-4 rounded-xl bg-rose-500/10 border border-rose-500/30 text-rose-400 text-xs font-mono">{error}</div>}

      {result && (
        <div className="space-y-6">
          <div className="grid grid-cols-2 md:grid-cols-5 gap-4">
            <div className="bg-slate-900/60 border border-slate-800/80 p-4 rounded-xl">
              <span className="text-[10px] font-mono uppercase text-slate-400 block">LMS Suffix Count</span>
              <span className="text-xl font-bold font-mono text-emerald-400">{result.lmsCount}</span>
            </div>
            <div className="bg-slate-900/60 border border-slate-800/80 p-4 rounded-xl">
              <span className="text-[10px] font-mono uppercase text-slate-400 block">Recursion Depth</span>
              <span className="text-xl font-bold font-mono text-slate-200">{result.recursionDepth}</span>
            </div>
            <div className="bg-slate-900/60 border border-slate-800/80 p-4 rounded-xl">
              <span className="text-[10px] font-mono uppercase text-slate-400 block">Operations</span>
              <span className="text-xl font-bold font-mono text-slate-200">{result.operationCount}</span>
            </div>
            <div className="bg-slate-900/60 border border-slate-800/80 p-4 rounded-xl">
              <span className="text-[10px] font-mono uppercase text-slate-400 block">Execution Time</span>
              <span className="text-xl font-bold font-mono text-slate-200">{(result.executionTimeNanos / 1000000).toFixed(3)} ms</span>
            </div>
            <div className="bg-slate-900/60 border border-slate-800/80 p-4 rounded-xl">
              <span className="text-[10px] font-mono uppercase text-slate-400 block">Phases Recorded</span>
              <span className="text-xl font-bold font-mono text-slate-200">{result.trace?.length || 0}</span>
            </div>
          </div>

          <div className="bg-slate-900/80 border border-slate-800 rounded-xl p-6 space-y-4">
            <h3 className="text-sm font-bold text-slate-200 font-mono uppercase">Induced Sorted Suffix Array</h3>
            <div className="overflow-x-auto">
              <table className="w-full text-left text-xs font-mono">
                <thead className="bg-slate-950 text-slate-400 border-b border-slate-800">
                  <tr>
                    <th className="p-3">Rank (i)</th>
                    <th className="p-3">SA[i]</th>
                    <th className="p-3">Suffix String T[SA[i] .. n-1]</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-slate-800/60">
                  {result.suffixArray?.map((saIdx, rank) => (
                    <tr key={rank} className="hover:bg-slate-800/40">
                      <td className="p-3 text-emerald-400 font-bold">#{rank}</td>
                      <td className="p-3 text-slate-300 font-bold">{saIdx}</td>
                      <td className="p-3 text-emerald-300 font-mono">{result.text?.substring(saIdx)}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};
