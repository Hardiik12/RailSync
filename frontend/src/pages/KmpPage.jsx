import React, { useState } from 'react';
import { runKmpSearch } from '../api/m1Api';
import { AlgorithmPageLayout } from '../components/layout/AlgorithmPageLayout';

export const KmpPage = () => {
  const [text, setText] = useState('Kondapuram Express departs from Vijayawada Junction platform 4');
  const [pattern, setPattern] = useState('Vijayawada');
  const [traceEnabled, setTraceEnabled] = useState(true);

  const [loading, setLoading] = useState(false);
  const [result, setResult] = useState(null);
  const [error, setError] = useState(null);

  const presets = [
    { label: 'Vijayawada Search', onClick: () => { setText('Kondapuram Express departs from Vijayawada Junction platform 4'); setPattern('Vijayawada'); } },
    { label: 'Overlapping Pattern', onClick: () => { setText('AAAAA'); setPattern('AAA'); } },
    { label: 'Train Code Search', onClick: () => { setText('TKT-12727-VJA-20261002, TKT-12727-SC-20261003'); setPattern('12727'); } },
    { label: 'Notice Search', onClick: () => { setText('Signal maintenance at Secunderabad platform 12 causing 15m delay'); setPattern('maintenance'); } },
  ];

  const handleRun = async () => {
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
        maxTraceSteps: 200,
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
          <mark key={idx} className="bg-cyan-500/30 text-cyan-200 border border-cyan-400/50 px-1 py-0.5 rounded font-bold font-mono">
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

  const inputForm = (
    <>
      <div className="space-y-1.5">
        <label className="text-xs font-mono text-slate-300 font-medium">Operational Target Text</label>
        <textarea
          value={text}
          onChange={(e) => setText(e.target.value)}
          rows={3}
          className="w-full bg-slate-950 border border-slate-800 rounded-lg px-3 py-2 text-xs text-slate-200 font-mono focus:outline-none focus:border-cyan-500"
        />
      </div>

      <div className="space-y-1.5">
        <label className="text-xs font-mono text-slate-300 font-medium">Search Pattern</label>
        <input
          type="text"
          value={pattern}
          onChange={(e) => setPattern(e.target.value)}
          className="w-full bg-slate-950 border border-slate-800 rounded-lg px-3 py-2 text-xs text-slate-200 font-mono focus:outline-none focus:border-cyan-500"
        />
      </div>

      <div className="pt-1">
        <label className="flex items-center gap-2 text-xs text-slate-300 cursor-pointer">
          <input
            type="checkbox"
            checked={traceEnabled}
            onChange={(e) => setTraceEnabled(e.target.checked)}
            className="rounded bg-slate-950 border-slate-800 text-cyan-500 focus:ring-cyan-500"
          />
          <span>Enable LPS Table & Step-by-Step Trace</span>
        </label>
      </div>
    </>
  );

  const resultView = result ? (
    <div className="space-y-5">
      <div className="flex items-center justify-between bg-slate-950 p-4 rounded-lg border border-slate-800">
        <span className="text-xs font-mono text-slate-400 uppercase">KMP Search Result:</span>
        <span className="inline-flex items-center gap-1.5 text-xs px-3 py-1 rounded-full bg-cyan-500/10 text-cyan-400 border border-cyan-500/30 font-mono font-bold">
          {result.matchCount ?? result.matches?.length ?? 0} MATCHES FOUND
        </span>
      </div>

      <div className="space-y-2">
        <h3 className="text-xs font-mono text-slate-400 uppercase font-bold">Highlighted Target Text:</h3>
        <div className="bg-slate-950 p-4 rounded-lg border border-slate-800/80 font-mono text-xs text-slate-200 leading-relaxed overflow-x-auto">
          {renderHighlightedText()}
        </div>
      </div>

      {result.lps && (
        <div className="space-y-2">
          <h3 className="text-xs font-mono text-slate-400 uppercase font-bold">Computed LPS (Longest Prefix Suffix) Array:</h3>
          <div className="flex flex-wrap gap-2">
            {result.lps.map((val, i) => (
              <div key={i} className="bg-slate-950 px-3 py-1.5 rounded border border-slate-800 flex items-center gap-2">
                <span className="text-slate-500 font-mono text-[10px]">{pattern[i]}:</span>
                <span className="text-cyan-400 font-mono text-xs font-bold">{val}</span>
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  ) : null;

  return (
    <AlgorithmPageLayout
      moduleTag="DSA MODULE 1 · CO1"
      title="Knuth-Morris-Pratt (KMP) Pattern Search"
      description="Exact O(n + m) string pattern matching using precomputed Longest Prefix Suffix (LPS) table to skip redundant character comparisons."
      presets={presets}
      inputForm={inputForm}
      onRun={handleRun}
      onReset={handleReset}
      loading={loading}
      error={error}
      result={result}
      resultView={resultView}
      railwayNote="KMP is utilized for rapid, deterministic text searching across station schedules, train codes, and operational alerts."
    />
  );
};
