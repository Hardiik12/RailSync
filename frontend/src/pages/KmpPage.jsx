import React, { useState } from 'react';
import { runKmpSearch, searchRailwayStations } from '../api/m1Api';
import { AlgorithmPageLayout } from '../components/layout/AlgorithmPageLayout';
import { Building2, Search, Database, CheckCircle2 } from 'lucide-react';

export const KmpPage = () => {
  const [text, setText] = useState('Kondapuram Express departs from Vijayawada Junction platform 4');
  const [pattern, setPattern] = useState('Vijayawada');
  const [traceEnabled, setTraceEnabled] = useState(true);

  const [loading, setLoading] = useState(false);
  const [result, setResult] = useState(null);
  const [error, setError] = useState(null);

  // M1 Station Search Adapter State
  const [stQuery, setStQuery] = useState('NDL');
  const [stAlgo, setStAlgo] = useState('KMP');
  const [stOrigin, setStOrigin] = useState('ALL');
  const [stLoading, setStLoading] = useState(false);
  const [stResults, setStResults] = useState(null);
  const [stError, setStError] = useState(null);

  const handleStationSearch = async (e) => {
    e?.preventDefault();
    if (!stQuery) return;
    setStLoading(true);
    setStError(null);
    try {
      const res = await searchRailwayStations({
        query: stQuery,
        algorithm: stAlgo,
        dataOriginFilter: stOrigin,
        limit: 10,
        traceEnabled: false,
      });
      if (res.success && res.data) {
        setStResults(res.data);
      }
    } catch (err) {
      setStError(err.message || 'Station search failed');
    } finally {
      setStLoading(false);
    }
  };

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
    <div className="space-y-8">
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

      {/* M1 Railway Station Search Adapter Component */}
      <div className="bg-slate-900/80 border border-slate-800 rounded-xl p-6 space-y-6 max-w-7xl mx-auto">
        <div className="flex flex-col md:flex-row md:items-center justify-between gap-3 pb-4 border-b border-slate-800">
          <div>
            <div className="flex items-center gap-2 text-xs font-mono text-cyan-400">
              <Building2 className="h-4 w-4" />
              <span>M1 RAILWAY DOMAIN ADAPTER</span>
            </div>
            <h2 className="text-lg font-bold text-slate-100 mt-1">Railway Station Master Search</h2>
            <p className="text-xs text-slate-400">
              Executes manual M1 algorithms (KMP, Z, Rabin-Karp, Aho-Corasick) over PostgreSQL station records.
            </p>
          </div>
        </div>

        <form onSubmit={handleStationSearch} className="grid grid-cols-1 md:grid-cols-4 gap-4 items-end">
          <div className="space-y-1.5">
            <label className="text-xs font-mono text-slate-300">Station Query</label>
            <input
              type="text"
              value={stQuery}
              onChange={(e) => setStQuery(e.target.value)}
              placeholder="e.g. NDL or Vijayawada"
              className="w-full bg-slate-950 border border-slate-800 rounded-lg px-3 py-2 text-xs text-slate-200 font-mono focus:outline-none focus:border-cyan-500"
              required
            />
          </div>

          <div className="space-y-1.5">
            <label className="text-xs font-mono text-slate-300">M1 Algorithm</label>
            <select
              value={stAlgo}
              onChange={(e) => setStAlgo(e.target.value)}
              className="w-full bg-slate-950 border border-slate-800 rounded-lg px-3 py-2 text-xs text-slate-200 font-mono focus:outline-none focus:border-cyan-500"
            >
              <option value="KMP">KMP (Knuth-Morris-Pratt)</option>
              <option value="Z">Z-Function</option>
              <option value="RABIN_KARP">Rabin-Karp Rolling Hash</option>
              <option value="AHO_CORASICK">Aho-Corasick Automaton</option>
            </select>
          </div>

          <div className="space-y-1.5">
            <label className="text-xs font-mono text-slate-300">Data Origin Filter</label>
            <select
              value={stOrigin}
              onChange={(e) => setStOrigin(e.target.value)}
              className="w-full bg-slate-950 border border-slate-800 rounded-lg px-3 py-2 text-xs text-slate-200 font-mono focus:outline-none focus:border-cyan-500"
            >
              <option value="ALL">ALL (Public + Synthetic)</option>
              <option value="PUBLIC_DATA">PUBLIC_DATA Only</option>
              <option value="SYNTHETIC">SYNTHETIC Only</option>
            </select>
          </div>

          <button
            type="submit"
            disabled={stLoading}
            className="flex items-center justify-center gap-2 px-4 py-2 rounded-lg bg-cyan-500/10 border border-cyan-500/30 text-cyan-300 hover:bg-cyan-500/20 text-xs font-semibold font-mono transition-all disabled:opacity-50"
          >
            <Search className="h-4 w-4" />
            <span>{stLoading ? 'Searching...' : 'Search Stations'}</span>
          </button>
        </form>

        {stError && <div className="p-3 rounded-lg bg-rose-500/10 border border-rose-500/30 text-rose-400 text-xs font-mono">{stError}</div>}

        {stResults && (
          <div className="space-y-4 pt-2">
            <div className="flex items-center justify-between text-xs font-mono text-slate-400">
              <span>Matching Stations ({stResults.totalMatchesCount}):</span>
              <span className="text-cyan-400 font-bold">
                Algorithm: {stResults.algorithm} · Time: {(stResults.executionTimeNanos / 1e6).toFixed(2)} ms
              </span>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
              {stResults.matchedStations?.map((st) => (
                <div key={st.id} className="p-3.5 rounded-lg bg-slate-950 border border-slate-800/80 space-y-1.5">
                  <div className="flex items-center justify-between">
                    <span className="text-xs font-mono font-bold text-cyan-400">{st.stationCode}</span>
                    <span
                      className={`text-[10px] font-mono px-2 py-0.5 rounded border ${
                        st.dataOrigin === 'PUBLIC_DATA'
                          ? 'bg-purple-500/10 text-purple-400 border-purple-500/30'
                          : 'bg-emerald-500/10 text-emerald-400 border-emerald-500/30'
                      }`}
                    >
                      {st.dataOrigin || 'SYNTHETIC'}
                    </span>
                  </div>
                  <div className="text-xs font-medium text-slate-100">{st.name}</div>
                  <div className="text-[11px] text-slate-400 font-mono">
                    City: {st.city} · State: {st.state}
                  </div>
                  {st.sourceDataset && (
                    <div className="text-[10px] text-slate-500 font-mono">
                      Source: {st.sourceDataset}
                    </div>
                  )}
                </div>
              ))}
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

