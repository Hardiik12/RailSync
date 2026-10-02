import React, { useState } from 'react';
import { runSuffixAutomaton } from '../api/m2Api';
import { Cpu, Play, CheckCircle2, XCircle, GitBranch } from 'lucide-react';

export const SuffixAutomatonPage = () => {
  const [text, setText] = useState('DELHI_EXPRESS_NEW_DELHI');
  const [query, setQuery] = useState('EXPRESS');
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleRun = async (e) => {
    e?.preventDefault();
    setLoading(true);
    setError(null);
    try {
      const res = await runSuffixAutomaton({ text, query, traceEnabled: true, maxTraceSteps: 500 });
      setResult(res.data);
    } catch (err) {
      setError(err.message || 'Execution failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="p-8 max-w-7xl mx-auto space-y-8">
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4 border-b border-slate-800 pb-6">
        <div>
          <div className="flex items-center gap-2 text-amber-400 font-mono text-xs mb-1">
            <Cpu className="h-4 w-4" />
            <span>M2 Suffix Structures — CO2</span>
          </div>
          <h1 className="text-2xl font-bold text-slate-100">Suffix Automaton (SAM) Visualizer</h1>
          <p className="text-xs text-slate-400 mt-1">
            Manual linear-time O(n) Suffix Automaton for substring indexing and search queries.
          </p>
        </div>
        <div className="flex items-center gap-3">
          <div className="px-3 py-1.5 rounded-lg bg-slate-900 border border-slate-800 text-xs font-mono text-slate-300">
            Time: <span className="text-amber-400 font-bold">O(n)</span>
          </div>
          <div className="px-3 py-1.5 rounded-lg bg-slate-900 border border-slate-800 text-xs font-mono text-slate-300">
            Space: <span className="text-amber-400 font-bold">O(n · |Σ|)</span>
          </div>
        </div>
      </div>

      <form onSubmit={handleRun} className="bg-slate-900/80 border border-slate-800 rounded-xl p-6 space-y-4">
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <label className="block text-xs font-mono uppercase text-slate-400 mb-2">Input Document Text</label>
            <input
              type="text"
              value={text}
              onChange={(e) => setText(e.target.value)}
              className="w-full bg-slate-950 border border-slate-800 rounded-lg px-4 py-2 text-sm text-slate-100 font-mono focus:outline-none focus:border-amber-500/50"
              required
            />
          </div>
          <div>
            <label className="block text-xs font-mono uppercase text-slate-400 mb-2">Query Substring</label>
            <input
              type="text"
              value={query}
              onChange={(e) => setQuery(e.target.value)}
              className="w-full bg-slate-950 border border-slate-800 rounded-lg px-4 py-2 text-sm text-slate-100 font-mono focus:outline-none focus:border-amber-500/50"
            />
          </div>
        </div>
        <div className="flex justify-end pt-2">
          <button
            type="submit"
            disabled={loading}
            className="flex items-center gap-2 px-5 py-2.5 rounded-lg bg-amber-500/10 border border-amber-500/30 text-amber-300 hover:bg-amber-500/20 text-xs font-semibold transition-all disabled:opacity-50"
          >
            <Play className="h-4 w-4" />
            <span>{loading ? 'Building SAM...' : 'Build SAM & Search'}</span>
          </button>
        </div>
      </form>

      {error && <div className="p-4 rounded-xl bg-rose-500/10 border border-rose-500/30 text-rose-400 text-xs font-mono">{error}</div>}

      {result && (
        <div className="space-y-6">
          <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
            <div className="bg-slate-900/60 border border-slate-800/80 p-4 rounded-xl">
              <span className="text-[10px] font-mono uppercase text-slate-400 block">Query Status</span>
              <div className="flex items-center gap-2 mt-1">
                {result.substringFound ? (
                  <>
                    <CheckCircle2 className="h-4 w-4 text-emerald-400" />
                    <span className="text-sm font-bold font-mono text-emerald-400">FOUND</span>
                  </>
                ) : (
                  <>
                    <XCircle className="h-4 w-4 text-rose-400" />
                    <span className="text-sm font-bold font-mono text-rose-400">NOT FOUND</span>
                  </>
                )}
              </div>
            </div>
            <div className="bg-slate-900/60 border border-slate-800/80 p-4 rounded-xl">
              <span className="text-[10px] font-mono uppercase text-slate-400 block">First Occurrence Index</span>
              <span className="text-xl font-bold font-mono text-amber-300">{result.firstOccurrenceIndex}</span>
            </div>
            <div className="bg-slate-900/60 border border-slate-800/80 p-4 rounded-xl">
              <span className="text-[10px] font-mono uppercase text-slate-400 block">SAM States Count</span>
              <span className="text-xl font-bold font-mono text-slate-200">{result.stateCount}</span>
            </div>
            <div className="bg-slate-900/60 border border-slate-800/80 p-4 rounded-xl">
              <span className="text-[10px] font-mono uppercase text-slate-400 block">Build Time</span>
              <span className="text-xl font-bold font-mono text-slate-200">{(result.buildNanos / 1000).toFixed(1)} µs</span>
            </div>
          </div>

          <div className="bg-slate-900/80 border border-slate-800 rounded-xl p-6 space-y-4">
            <h3 className="text-sm font-bold text-slate-200 font-mono uppercase flex items-center gap-2">
              <GitBranch className="h-4 w-4 text-amber-400" />
              <span>Suffix Automaton States & Suffix Links</span>
            </h3>

            <div className="overflow-x-auto">
              <table className="w-full text-left text-xs font-mono">
                <thead className="bg-slate-950 text-slate-400 border-b border-slate-800">
                  <tr>
                    <th className="p-3">State ID</th>
                    <th className="p-3">Max Len</th>
                    <th className="p-3">Suffix Link</th>
                    <th className="p-3">Type</th>
                    <th className="p-3">Transitions</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-slate-800/60">
                  {result.automatonStates?.map((s) => (
                    <tr key={s.id} className="hover:bg-slate-800/40">
                      <td className="p-3 text-amber-400 font-bold">#{s.id}</td>
                      <td className="p-3 text-slate-300">{s.len}</td>
                      <td className="p-3 text-cyan-400">#{s.link}</td>
                      <td className="p-3">
                        <span className={`text-[10px] px-2 py-0.5 rounded ${s.isClone ? 'bg-amber-500/10 text-amber-400 border border-amber-500/20' : 'bg-slate-800 text-slate-400'}`}>
                          {s.isClone ? 'CLONE' : 'PRIMARY'}
                        </span>
                      </td>
                      <td className="p-3 text-slate-300">
                        {s.transitions && Object.keys(s.transitions).length > 0
                          ? Object.entries(s.transitions).map(([ch, target]) => `'${ch}' -> #${target}`).join(', ')
                          : '-'}
                      </td>
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
