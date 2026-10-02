import React, { useState } from 'react';
import { runKmpSearch } from '../api/m1Api';
import { Search, Play, RotateCcw, Clock, Hash, Cpu, Layers, AlertCircle, CheckCircle2, ListTree, HelpCircle } from 'lucide-react';

export const KmpPage = () => {
  const [text, setText] = useState('Kondapuram Express departs from Vijayawada Junction platform 4');
  const [pattern, setPattern] = useState('Vijayawada');
  const [traceEnabled, setTraceEnabled] = useState(true);
  const [maxTraceSteps, setMaxTraceSteps] = useState(200);

  const [loading, setLoading] = useState(false);
  const [result, setResult] = useState(null);
  const [error, setError] = useState(null);

  const samplePresets = [
    { label: 'Vijayawada Search', text: 'Kondapuram Express departs from Vijayawada Junction platform 4', pattern: 'Vijayawada' },
    { label: 'Overlapping Pattern (AAAAA)', text: 'AAAAA', pattern: 'AAA' },
    { label: 'Train Code Search', text: 'TKT-12727-VJA-20261002, TKT-12727-SC-20261003', pattern: '12727' },
    { label: 'Service Notice Search', text: 'Signal maintenance at Secunderabad platform 12 causing 15m delay', pattern: 'maintenance' },
  ];

  const handleRunAlgorithm = async (e) => {
    e?.preventDefault();
    if (!text || !pattern) {
      setError('Please provide both text and search pattern.');
      return;
    }

    setLoading(true);
    setError(null);
    try {
      const response = await runKmpSearch({
        text,
        pattern,
        traceEnabled,
        maxTraceSteps: Number(maxTraceSteps),
      });

      if (response.success && response.data) {
        setResult(response.data);
      }
    } catch (err) {
      setError(err.message || 'Failed to execute KMP algorithm on backend');
    } finally {
      setLoading(false);
    }
  };

  const handleReset = () => {
    setText('Kondapuram Express departs from Vijayawada Junction platform 4');
    setPattern('Vijayawada');
    setResult(null);
    setError(null);
  };

  const applyPreset = (preset) => {
    setText(preset.text);
    setPattern(preset.pattern);
    setResult(null);
    setError(null);
  };

  // Utility to highlight matches in text string
  const renderHighlightedText = () => {
    if (!result || !result.matches || result.matches.length === 0) {
      return text;
    }

    const matchesSet = new Set(result.matches);
    const patLen = pattern.length;
    const elements = [];
    let idx = 0;

    while (idx < text.length) {
      if (matchesSet.has(idx)) {
        elements.push(
          <mark
            key={idx}
            className="bg-cyan-500/30 text-cyan-200 border border-cyan-400/50 px-1 py-0.5 rounded font-bold font-mono"
          >
            {text.substring(idx, idx + patLen)}
          </mark>
        );
        idx += patLen;
      } else {
        elements.push(text.charAt(idx));
        idx++;
      }
    }

    return elements;
  };

  return (
    <div className="space-y-8 max-w-7xl mx-auto pb-12">
      {/* Header */}
      <div className="pb-6 border-b border-slate-800 flex flex-col md:flex-row md:items-center justify-between gap-4">
        <div>
          <div className="flex items-center gap-2 text-xs font-mono text-cyan-400 mb-1">
            <span className="px-2 py-0.5 rounded bg-cyan-500/10 border border-cyan-500/30">M1 · CO1</span>
            <span>STRING ALGORITHMS</span>
          </div>
          <h1 className="text-2xl font-bold text-white tracking-tight">
            Knuth-Morris-Pratt (KMP) Pattern Matcher
          </h1>
          <p className="text-xs text-slate-400 mt-1">
            Manual $O(n + m)$ exact string pattern matching using Longest Prefix Suffix (LPS) preprocessing.
          </p>
        </div>
      </div>

      {/* Preset Quick Selectors */}
      <div className="flex items-center gap-2 overflow-x-auto pb-2">
        <span className="text-xs font-mono text-slate-500 shrink-0">Sample Inputs:</span>
        {samplePresets.map((preset, i) => (
          <button
            key={i}
            onClick={() => applyPreset(preset)}
            className="px-3 py-1.5 rounded-lg bg-slate-900 border border-slate-800 hover:border-slate-700 text-slate-300 text-xs font-medium shrink-0 transition"
          >
            {preset.label}
          </button>
        ))}
      </div>

      {/* Input Section */}
      <form onSubmit={handleRunAlgorithm} className="p-6 rounded-2xl bg-slate-900/60 border border-slate-800/80 shadow-xl space-y-6">
        <h2 className="text-sm font-semibold text-slate-200 font-mono uppercase tracking-wider flex items-center gap-2">
          <Search className="h-4 w-4 text-cyan-400" /> Algorithm Execution Parameters
        </h2>

        <div className="grid grid-cols-1 gap-5">
          {/* Target Text */}
          <div className="space-y-2">
            <label className="text-xs font-mono text-slate-300">Operational Target Text ($N$ chars):</label>
            <textarea
              rows={3}
              value={text}
              onChange={(e) => setText(e.target.value)}
              placeholder="Enter text string..."
              className="w-full p-3 bg-slate-950 border border-slate-800 rounded-xl text-xs text-slate-100 placeholder-slate-600 font-mono focus:outline-none focus:border-cyan-500/50 transition"
            />
          </div>

          {/* Search Pattern */}
          <div className="space-y-2">
            <label className="text-xs font-mono text-slate-300">Search Pattern ($M$ chars):</label>
            <input
              type="text"
              value={pattern}
              onChange={(e) => setPattern(e.target.value)}
              placeholder="Enter pattern string to locate..."
              className="w-full p-3 bg-slate-950 border border-slate-800 rounded-xl text-xs text-slate-100 placeholder-slate-600 font-mono focus:outline-none focus:border-cyan-500/50 transition"
            />
          </div>
        </div>

        {/* Configuration Toggles */}
        <div className="pt-2 grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4 border-t border-slate-800/60">
          <label className="flex items-center gap-3 cursor-pointer">
            <input
              type="checkbox"
              checked={traceEnabled}
              onChange={(e) => setTraceEnabled(e.target.checked)}
              className="rounded bg-slate-950 border-slate-700 text-cyan-500 focus:ring-cyan-500/20"
            />
            <span className="text-xs text-slate-300 font-mono">Enable Step-by-Step Trace</span>
          </label>

          <div className="flex items-center gap-3">
            <label className="text-xs text-slate-300 font-mono shrink-0">Max Trace Steps:</label>
            <input
              type="number"
              value={maxTraceSteps}
              onChange={(e) => setMaxTraceSteps(e.target.value)}
              min="10"
              max="2000"
              className="w-24 px-3 py-1 bg-slate-950 border border-slate-800 rounded-lg text-xs text-slate-100 font-mono focus:outline-none focus:border-cyan-500/50"
            />
          </div>
        </div>

        {/* Form Actions */}
        <div className="flex items-center gap-3 pt-2">
          <button
            type="submit"
            disabled={loading}
            className="px-6 py-2.5 rounded-xl bg-cyan-500 hover:bg-cyan-400 text-slate-950 font-bold text-xs transition shadow-lg shadow-cyan-500/20 flex items-center gap-2 disabled:opacity-50"
          >
            {loading ? (
              <span className="flex items-center gap-2">
                <span className="h-3.5 w-3.5 border-2 border-slate-950 border-t-transparent rounded-full animate-spin"></span>
                Executing KMP...
              </span>
            ) : (
              <>
                <Play className="h-4 w-4 fill-current" /> Run KMP Algorithm
              </>
            )}
          </button>
          <button
            type="button"
            onClick={handleReset}
            className="px-4 py-2.5 rounded-xl bg-slate-950 border border-slate-800 hover:border-slate-700 text-slate-400 hover:text-slate-200 text-xs font-medium transition flex items-center gap-2"
          >
            <RotateCcw className="h-4 w-4" /> Reset
          </button>
        </div>
      </form>

      {/* Error Alert */}
      {error && (
        <div className="p-4 rounded-xl bg-red-500/10 border border-red-500/30 text-red-400 text-xs flex items-center gap-3">
          <AlertCircle className="h-5 w-5 shrink-0" />
          <div>
            <span className="font-semibold">Backend Execution Error:</span> {error}
          </div>
        </div>
      )}

      {/* Result Metrics & Output Section */}
      {result && (
        <div className="space-y-6 animate-in fade-in duration-300">
          {/* Highlighted Match Output */}
          <div className="p-6 rounded-2xl bg-slate-900/60 border border-cyan-500/30 space-y-3">
            <h3 className="text-xs font-mono uppercase tracking-wider text-cyan-400 font-semibold flex items-center gap-2">
              <CheckCircle2 className="h-4 w-4" /> Text Pattern Match Result
            </h3>
            <div className="p-4 rounded-xl bg-slate-950 border border-slate-800 text-sm font-mono leading-relaxed text-slate-200 break-all">
              {renderHighlightedText()}
            </div>
            <div className="flex items-center gap-4 text-xs font-mono text-slate-400 pt-1">
              <span>Match Count: <strong className="text-cyan-400">{result.matchCount}</strong></span>
              <span>Match Indices: <strong className="text-cyan-400">{result.matches && result.matches.length > 0 ? JSON.stringify(result.matches) : 'None'}</strong></span>
            </div>
          </div>

          {/* Execution Metrics Summary */}
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-5">
            {/* Metric 1: Comparisons */}
            <div className="p-5 rounded-xl bg-slate-900/40 border border-slate-800/80 space-y-1">
              <div className="flex items-center justify-between text-slate-400">
                <span className="text-xs font-mono uppercase">Character Comparisons</span>
                <Hash className="h-4 w-4 text-cyan-400" />
              </div>
              <p className="text-2xl font-bold font-mono text-white">{result.operationCount}</p>
              <p className="text-[11px] text-slate-500">Total runtime char compares</p>
            </div>

            {/* Metric 2: Execution Time */}
            <div className="p-5 rounded-xl bg-slate-900/40 border border-slate-800/80 space-y-1">
              <div className="flex items-center justify-between text-slate-400">
                <span className="text-xs font-mono uppercase">Execution Time</span>
                <Clock className="h-4 w-4 text-cyan-400" />
              </div>
              <p className="text-2xl font-bold font-mono text-white">
                {(result.executionTimeNanos / 1000000).toFixed(3)} <span className="text-xs font-normal text-slate-400">ms</span>
              </p>
              <p className="text-[11px] font-mono text-slate-500">{result.executionTimeNanos.toLocaleString()} ns</p>
            </div>

            {/* Metric 3: Time Complexity */}
            <div className="p-5 rounded-xl bg-slate-900/40 border border-slate-800/80 space-y-1">
              <div className="flex items-center justify-between text-slate-400">
                <span className="text-xs font-mono uppercase">Time Complexity</span>
                <Cpu className="h-4 w-4 text-cyan-400" />
              </div>
              <p className="text-2xl font-bold font-mono text-cyan-400">
                {result.complexity?.time || 'O(n + m)'}
              </p>
              <p className="text-[11px] text-slate-500">Optimal linear scan</p>
            </div>

            {/* Metric 4: Space Complexity */}
            <div className="p-5 rounded-xl bg-slate-900/40 border border-slate-800/80 space-y-1">
              <div className="flex items-center justify-between text-slate-400">
                <span className="text-xs font-mono uppercase">Space Complexity</span>
                <Layers className="h-4 w-4 text-cyan-400" />
              </div>
              <p className="text-2xl font-bold font-mono text-cyan-400">
                {result.complexity?.space || 'O(m)'}
              </p>
              <p className="text-[11px] text-slate-500">LPS table size</p>
            </div>
          </div>

          {/* LPS Array Table Rendering */}
          <div className="p-6 rounded-2xl bg-slate-900/60 border border-slate-800/80 space-y-4">
            <h3 className="text-xs font-mono uppercase tracking-wider text-slate-300 font-semibold flex items-center gap-2">
              <ListTree className="h-4 w-4 text-cyan-400" /> LPS (Longest Prefix Suffix) Preprocessing Table
            </h3>
            <div className="overflow-x-auto">
              <table className="text-center font-mono text-xs border-collapse">
                <thead>
                  <tr className="bg-slate-950 border-b border-slate-800 text-slate-400">
                    <th className="p-2 border border-slate-800 text-left px-3">Index ($i$)</th>
                    {pattern.split('').map((_, i) => (
                      <th key={i} className="p-2 border border-slate-800 w-10">{i}</th>
                    ))}
                  </tr>
                </thead>
                <tbody>
                  <tr className="bg-slate-900/40 text-cyan-300">
                    <td className="p-2 border border-slate-800 font-semibold text-slate-400 text-left px-3">Pattern Char</td>
                    {pattern.split('').map((char, i) => (
                      <td key={i} className="p-2 border border-slate-800 font-bold">{char}</td>
                    ))}
                  </tr>
                  <tr className="bg-slate-950/80 text-emerald-400 font-bold">
                    <td className="p-2 border border-slate-800 font-semibold text-slate-400 text-left px-3">LPS Value</td>
                    {result.lps && result.lps.map((val, i) => (
                      <td key={i} className="p-2 border border-slate-800">{val}</td>
                    ))}
                  </tr>
                </tbody>
              </table>
            </div>
          </div>

          {/* Execution Trace Steps */}
          {result.trace && result.trace.length > 0 && (
            <div className="p-6 rounded-2xl bg-slate-900/60 border border-slate-800/80 space-y-4">
              <div className="flex items-center justify-between">
                <h3 className="text-xs font-mono uppercase tracking-wider text-slate-300 font-semibold flex items-center gap-2">
                  <ListTree className="h-4 w-4 text-cyan-400" /> Backend Execution Trace Steps ({result.trace.length})
                </h3>
              </div>

              <div className="max-h-96 overflow-y-auto rounded-xl border border-slate-800 bg-slate-950 p-2 space-y-1 text-xs font-mono">
                {result.trace.map((step, idx) => (
                  <div
                    key={idx}
                    className={`p-2.5 rounded-lg border flex flex-col sm:flex-row sm:items-center justify-between gap-2 transition ${
                      step.action === 'PATTERN_FOUND'
                        ? 'bg-emerald-500/10 border-emerald-500/40 text-emerald-300'
                        : step.action === 'LPS_FALLBACK'
                        ? 'bg-amber-500/10 border-amber-500/30 text-amber-300'
                        : 'bg-slate-900/50 border-slate-800/60 text-slate-300'
                    }`}
                  >
                    <div className="flex items-center gap-3">
                      <span className="px-2 py-0.5 rounded bg-slate-800 border border-slate-700 text-[10px] text-slate-400 font-semibold shrink-0">
                        Step {step.step}
                      </span>
                      <span className="font-bold shrink-0">{step.action}</span>
                      <span className="text-slate-400 text-[11px]">{step.description}</span>
                    </div>

                    {step.state && (
                      <div className="text-[10px] text-slate-500 font-mono shrink-0">
                        {JSON.stringify(step.state)}
                      </div>
                    )}
                  </div>
                ))}
              </div>
            </div>
          )}
        </div>
      )}
    </div>
  );
};
