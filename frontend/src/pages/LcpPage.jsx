import React, { useState } from 'react';
import { runLCP } from '../api/m2Api';
import { Search, Play } from 'lucide-react';

export const LcpPage = () => {
  const [text, setText] = useState('Train 12951 Express New Delhi. Train 12951 Express Mathura Junction.');
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleRun = async (e) => {
    e?.preventDefault();
    setLoading(true);
    setError(null);
    try {
      const res = await runLCP({ text, traceEnabled: true, maxTraceSteps: 500 });
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
          <div className="flex items-center gap-2 text-cyan-400 font-mono text-xs mb-1">
            <Search className="h-4 w-4" />
            <span>M2 Suffix Structures — CO2</span>
          </div>
          <h1 className="text-2xl font-bold text-slate-100">LCP Repeated Service Description Analysis</h1>
          <p className="text-xs text-slate-400 mt-1">
            Suffix Array → Kasai → LCP Array pipeline for detecting repeated railway service descriptions.
          </p>
        </div>
        <div className="flex items-center gap-3">
          <div className="px-3 py-1.5 rounded-lg bg-slate-900 border border-slate-800 text-xs font-mono text-slate-300">
            Pipeline: <span className="text-cyan-400 font-bold">SA + Kasai → LCP</span>
          </div>
        </div>
      </div>

      <form onSubmit={handleRun} className="bg-slate-900/80 border border-slate-800 rounded-xl p-6 space-y-4">
        <div>
          <label className="block text-xs font-mono uppercase text-slate-400 mb-2">Corpus / Description Text</label>
          <textarea
            rows={3}
            value={text}
            onChange={(e) => setText(e.target.value)}
            className="w-full bg-slate-950 border border-slate-800 rounded-lg p-3 text-sm text-slate-100 font-mono focus:outline-none focus:border-cyan-500/50"
            required
          />
        </div>
        <div className="flex justify-end pt-2">
          <button
            type="submit"
            disabled={loading}
            className="flex items-center gap-2 px-5 py-2.5 rounded-lg bg-cyan-500/10 border border-cyan-500/30 text-cyan-300 hover:bg-cyan-500/20 text-xs font-semibold transition-all disabled:opacity-50"
          >
            <Play className="h-4 w-4" />
            <span>{loading ? 'Analyzing...' : 'Analyze Repeated Substrings'}</span>
          </button>
        </div>
      </form>

      {error && <div className="p-4 rounded-xl bg-rose-500/10 border border-rose-500/30 text-rose-400 text-xs font-mono">{error}</div>}

      {result && (
        <div className="space-y-6">
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div className="bg-slate-900/60 border border-slate-800/80 p-4 rounded-xl">
              <span className="text-[10px] font-mono uppercase text-slate-400 block">Max Common Prefix Length</span>
              <span className="text-2xl font-bold font-mono text-cyan-400">{result.maxLcpValue}</span>
            </div>
            <div className="bg-slate-900/60 border border-slate-800/80 p-4 rounded-xl col-span-2">
              <span className="text-[10px] font-mono uppercase text-slate-400 block">Longest Repeated Substring</span>
              <span className="text-lg font-bold font-mono text-emerald-300">"{result.longestRepeatedSubstring}"</span>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};
