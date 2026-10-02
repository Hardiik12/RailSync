import React, { useState } from 'react';
import { runAhoCorasickSearch } from '../api/m1Api';
import { Cpu, Play, AlertTriangle, CheckCircle2, GitBranch, ArrowRight } from 'lucide-react';

export const AhoCorasickPage = () => {
  const [text, setText] = useState(
    'NOTICE: Train 12951 delay due to platform maintenance at New Delhi. Route diverted and rescheduled.'
  );
  const [keywordsStr, setKeywordsStr] = useState('delay, cancelled, platform, diverted, maintenance, rescheduled');
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const [currentStepIndex, setCurrentStepIndex] = useState(0);

  const handleRun = async (e) => {
    e?.preventDefault();
    setLoading(true);
    setError(null);
    try {
      const keywords = keywordsStr
        .split(',')
        .map((k) => k.trim())
        .filter((k) => k.length > 0);

      const res = await runAhoCorasickSearch({
        text,
        keywords,
        traceEnabled: true,
        benchmarkEnabled: true,
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
          <div className="flex items-center gap-2 text-amber-400 font-mono text-xs mb-1">
            <Cpu className="h-4 w-4" />
            <span>M1 String Algorithms — CO1</span>
          </div>
          <h1 className="text-2xl font-bold text-slate-100">Aho-Corasick Service Alert Detector</h1>
          <p className="text-xs text-slate-400 mt-1">
            Manual multi-pattern automaton (Trie + Failure Links) for single-pass railway alert keyword matching.
          </p>
        </div>

        <div className="flex items-center gap-3">
          <div className="px-3 py-1.5 rounded-lg bg-slate-900 border border-slate-800 text-xs font-mono text-slate-300">
            Time: <span className="text-amber-400 font-bold">O(n + Σ|P| + Z)</span>
          </div>
          <div className="px-3 py-1.5 rounded-lg bg-slate-900 border border-slate-800 text-xs font-mono text-slate-300">
            Space: <span className="text-amber-400 font-bold">O(Σ|P| · |Σ|)</span>
          </div>
        </div>
      </div>

      {/* Input Form */}
      <form onSubmit={handleRun} className="bg-slate-900/80 border border-slate-800 rounded-xl p-6 space-y-4">
        <div>
          <label className="block text-xs font-mono uppercase text-slate-400 mb-2">Service Alert Announcement Text</label>
          <textarea
            rows={3}
            value={text}
            onChange={(e) => setText(e.target.value)}
            className="w-full bg-slate-950 border border-slate-800 rounded-lg p-3 text-sm text-slate-100 font-mono focus:outline-none focus:border-amber-500/50"
            required
          />
        </div>

        <div>
          <label className="block text-xs font-mono uppercase text-slate-400 mb-2">
            Target Keywords (comma-separated synthetic alert words)
          </label>
          <input
            type="text"
            value={keywordsStr}
            onChange={(e) => setKeywordsStr(e.target.value)}
            className="w-full bg-slate-950 border border-slate-800 rounded-lg px-4 py-2 text-sm text-slate-100 font-mono focus:outline-none focus:border-amber-500/50"
            required
          />
        </div>

        <div className="flex justify-end gap-3 pt-2">
          <button
            type="submit"
            disabled={loading}
            className="flex items-center gap-2 px-5 py-2.5 rounded-lg bg-amber-500/10 border border-amber-500/30 text-amber-300 hover:bg-amber-500/20 text-xs font-semibold transition-all disabled:opacity-50"
          >
            <Play className="h-4 w-4" />
            <span>{loading ? 'Analyzing...' : 'Scan Service Alerts'}</span>
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
              <span className="text-[10px] font-mono uppercase text-slate-400 block">Matches Detected</span>
              <span className="text-xl font-bold font-mono text-amber-400">{result.matchCount}</span>
            </div>
            <div className="bg-slate-900/60 border border-slate-800/80 p-4 rounded-xl">
              <span className="text-[10px] font-mono uppercase text-slate-400 block">Unique Keywords</span>
              <span className="text-xl font-bold font-mono text-amber-300">
                {result.matchedKeywords?.length || 0}
              </span>
            </div>
            <div className="bg-slate-900/60 border border-slate-800/80 p-4 rounded-xl">
              <span className="text-[10px] font-mono uppercase text-slate-400 block">Automaton Nodes</span>
              <span className="text-xl font-bold font-mono text-slate-200">
                {result.automatonNodes?.length || 0}
              </span>
            </div>
            <div className="bg-slate-900/60 border border-slate-800/80 p-4 rounded-xl">
              <span className="text-[10px] font-mono uppercase text-slate-400 block">Trie Build Time</span>
              <span className="text-xl font-bold font-mono text-slate-200">
                {(result.trieBuildNanos / 1000).toFixed(1)} µs
              </span>
            </div>
            <div className="bg-slate-900/60 border border-slate-800/80 p-4 rounded-xl">
              <span className="text-[10px] font-mono uppercase text-slate-400 block">Scan Time</span>
              <span className="text-xl font-bold font-mono text-slate-200">
                {(result.searchNanos / 1000).toFixed(1)} µs
              </span>
            </div>
          </div>

          {/* Matched Keywords Grid */}
          <div className="bg-slate-900/80 border border-slate-800 rounded-xl p-6 space-y-4">
            <h3 className="text-sm font-bold text-slate-200 font-mono uppercase flex items-center gap-2">
              <AlertTriangle className="h-4 w-4 text-amber-400" />
              <span>Detected Alert Occurrences</span>
            </h3>

            {result.matches && result.matches.length > 0 ? (
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-3">
                {result.matches.map((m, idx) => (
                  <div
                    key={idx}
                    className="p-3 rounded-lg bg-slate-950 border border-amber-500/30 flex items-center justify-between"
                  >
                    <div>
                      <span className="text-xs font-mono font-bold text-amber-300 block">{m.keyword}</span>
                      <span className="text-[10px] font-mono text-slate-500">
                        Indices: [{m.startIndex} .. {m.endIndex}]
                      </span>
                    </div>
                    <span className="text-[10px] font-mono px-2 py-0.5 rounded bg-amber-500/10 text-amber-400 border border-amber-500/20">
                      Match #{idx + 1}
                    </span>
                  </div>
                ))}
              </div>
            ) : (
              <p className="text-xs text-slate-400">No alert keywords detected in text.</p>
            )}
          </div>

          {/* Automaton Trie Nodes & Failure Links Table */}
          <div className="bg-slate-900/80 border border-slate-800 rounded-xl p-6 space-y-4">
            <h3 className="text-sm font-bold text-slate-200 font-mono uppercase flex items-center gap-2">
              <GitBranch className="h-4 w-4 text-cyan-400" />
              <span>Automaton Trie Nodes & Failure Links</span>
            </h3>

            <div className="overflow-x-auto">
              <table className="w-full text-left text-xs font-mono">
                <thead className="bg-slate-950 text-slate-400 border-b border-slate-800">
                  <tr>
                    <th className="p-3">Node ID</th>
                    <th className="p-3">Char</th>
                    <th className="p-3">Parent ID</th>
                    <th className="p-3">Failure Link</th>
                    <th className="p-3">Transitions</th>
                    <th className="p-3">Outputs</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-slate-800/60">
                  {result.automatonNodes?.map((node) => (
                    <tr key={node.id} className="hover:bg-slate-800/40">
                      <td className="p-3 font-bold text-amber-400">
                        #{node.id} {node.isRoot && '(ROOT)'}
                      </td>
                      <td className="p-3 text-slate-200">{node.charLabel || 'ROOT'}</td>
                      <td className="p-3 text-slate-400">{node.parentId}</td>
                      <td className="p-3 text-cyan-400">#{node.failLink}</td>
                      <td className="p-3 text-slate-300">
                        {node.transitions && Object.keys(node.transitions).length > 0
                          ? Object.entries(node.transitions)
                              .map(([ch, target]) => `'${ch}' -> #${target}`)
                              .join(', ')
                          : '-'}
                      </td>
                      <td className="p-3 text-emerald-400">
                        {node.outputs && node.outputs.length > 0 ? node.outputs.join(', ') : '-'}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>

          {/* Trace Viewer */}
          {result.trace && result.trace.length > 0 && (
            <div className="bg-slate-900/80 border border-slate-800 rounded-xl p-6 space-y-4">
              <div className="flex items-center justify-between">
                <h3 className="text-sm font-bold text-slate-200 font-mono uppercase">Automaton Step Trace</h3>
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
                    <span className="text-amber-400 font-bold">Action: {currentStep.action}</span>
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
