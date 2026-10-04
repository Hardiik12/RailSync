import React, { useState, useEffect } from 'react';
import { fetchDocuments, searchDocuments, analyzeDocumentRepeatedSubstrings, indexStationDocuments } from '../api/m2Api';
import { Database, Search, FileText, Activity, CheckCircle2, Layers, RefreshCw } from 'lucide-react';

export const DocumentsPage = () => {
  const [documents, setDocuments] = useState([]);
  const [query, setQuery] = useState('Train 12951');
  const [searchResults, setSearchResults] = useState(null);
  const [analysisResults, setAnalysisResults] = useState(null);
  const [indexResult, setIndexResult] = useState(null);
  const [loadingDocs, setLoadingDocs] = useState(false);
  const [searching, setSearching] = useState(false);
  const [analyzing, setAnalyzing] = useState(false);
  const [indexing, setIndexing] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    loadDocs();
  }, []);

  const loadDocs = async () => {
    setLoadingDocs(true);
    try {
      const res = await fetchDocuments();
      setDocuments(res.data || []);
    } catch (err) {
      setError('Failed to fetch documents from database');
    } finally {
      setLoadingDocs(false);
    }
  };

  const handleIndexStations = async () => {
    setIndexing(true);
    setError(null);
    try {
      const res = await indexStationDocuments();
      if (res.success && res.data) {
        setIndexResult(res.data);
        await loadDocs();
      }
    } catch (err) {
      setError(err.message || 'Failed to index station documents');
    } finally {
      setIndexing(false);
    }
  };

  const handleSearch = async (e) => {
    e?.preventDefault();
    setSearching(true);
    try {
      const res = await searchDocuments(query);
      setSearchResults(res.data);
    } catch (err) {
      setError('Search failed');
    } finally {
      setSearching(false);
    }
  };

  const handleAnalyzeCorpus = async () => {
    setAnalyzing(true);
    try {
      const res = await analyzeDocumentRepeatedSubstrings();
      setAnalysisResults(res.data);
    } catch (err) {
      setError('Corpus analysis failed');
    } finally {
      setAnalyzing(false);
    }
  };

  return (
    <div className="p-8 max-w-7xl mx-auto space-y-8">
      {/* Header */}
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4 border-b border-slate-800 pb-6">
        <div>
          <div className="flex items-center gap-2 text-cyan-400 font-mono text-xs mb-1">
            <Database className="h-4 w-4" />
            <span>Railway Operational Database</span>
          </div>
          <h1 className="text-2xl font-bold text-slate-100">Railway Document Search & Similarity</h1>
          <p className="text-xs text-slate-400 mt-1">
            Deterministic station documents and operational logs indexed using Suffix Automaton (SAM) and Kasai LCP algorithms.
          </p>
        </div>
        <div className="flex items-center gap-3">
          <button
            onClick={handleIndexStations}
            disabled={indexing}
            className="flex items-center gap-2 px-4 py-2 rounded-lg bg-cyan-500/10 border border-cyan-500/30 text-cyan-300 hover:bg-cyan-500/20 text-xs font-semibold transition-all disabled:opacity-50"
          >
            <Layers className="h-4 w-4" />
            <span>{indexing ? 'Indexing Public Stations...' : 'Index Station Documents'}</span>
          </button>
          <button
            onClick={handleAnalyzeCorpus}
            disabled={analyzing}
            className="flex items-center gap-2 px-4 py-2 rounded-lg bg-emerald-500/10 border border-emerald-500/30 text-emerald-300 hover:bg-emerald-500/20 text-xs font-semibold transition-all disabled:opacity-50"
          >
            <Activity className="h-4 w-4" />
            <span>{analyzing ? 'Analyzing Corpus...' : 'Detect Duplicated Substrings'}</span>
          </button>
        </div>
      </div>

      {error && <div className="p-4 rounded-xl bg-rose-500/10 border border-rose-500/30 text-rose-400 text-xs font-mono">{error}</div>}

      {/* Station Indexing Result Banner */}
      {indexResult && (
        <div className="p-5 rounded-xl bg-slate-900/90 border border-cyan-500/30 space-y-3 font-mono">
          <div className="flex items-center justify-between">
            <span className="text-xs font-bold text-cyan-400 uppercase tracking-wider flex items-center gap-2">
              <CheckCircle2 className="h-4 w-4" /> Station Document Indexing Result
            </span>
            <span className="text-[11px] text-slate-400">Source: {indexResult.source}</span>
          </div>
          <div className="grid grid-cols-2 md:grid-cols-5 gap-3 text-xs">
            <div className="bg-slate-950 p-2.5 rounded border border-slate-800">
              <span className="text-[10px] text-slate-500 block">Records Read</span>
              <span className="text-slate-100 font-bold">{indexResult.recordsConsidered}</span>
            </div>
            <div className="bg-slate-950 p-2.5 rounded border border-slate-800">
              <span className="text-[10px] text-slate-500 block">Docs Created</span>
              <span className="text-emerald-400 font-bold">{indexResult.documentsCreated}</span>
            </div>
            <div className="bg-slate-950 p-2.5 rounded border border-slate-800">
              <span className="text-[10px] text-slate-500 block">Docs Updated</span>
              <span className="text-cyan-400 font-bold">{indexResult.documentsUpdated}</span>
            </div>
            <div className="bg-slate-950 p-2.5 rounded border border-slate-800">
              <span className="text-[10px] text-slate-500 block">Skipped</span>
              <span className="text-amber-400 font-bold">{indexResult.skippedRecords}</span>
            </div>
            <div className="bg-slate-950 p-2.5 rounded border border-slate-800">
              <span className="text-[10px] text-slate-500 block">Duration</span>
              <span className="text-slate-300 font-bold">{indexResult.durationMillis} ms</span>
            </div>
          </div>
        </div>
      )}


      {/* Substring Search Form */}
      <form onSubmit={handleSearch} className="bg-slate-900/80 border border-slate-800 rounded-xl p-6 space-y-4">
        <div>
          <label className="block text-xs font-mono uppercase text-slate-400 mb-2">Search Substring Across Documents (SAM Adapter)</label>
          <div className="flex gap-3">
            <input
              type="text"
              value={query}
              onChange={(e) => setQuery(e.target.value)}
              className="flex-1 bg-slate-950 border border-slate-800 rounded-lg px-4 py-2 text-sm text-slate-100 font-mono focus:outline-none focus:border-cyan-500/50"
              placeholder="Enter search query..."
              required
            />
            <button
              type="submit"
              disabled={searching}
              className="flex items-center gap-2 px-5 py-2 rounded-lg bg-cyan-500/10 border border-cyan-500/30 text-cyan-300 hover:bg-cyan-500/20 text-xs font-semibold transition-all disabled:opacity-50"
            >
              <Search className="h-4 w-4" />
              <span>{searching ? 'Searching...' : 'Search SAM'}</span>
            </button>
          </div>
        </div>
      </form>

      {/* Search Results */}
      {searchResults && (
        <div className="bg-slate-900/80 border border-slate-800 rounded-xl p-6 space-y-4">
          <h3 className="text-sm font-bold text-slate-200 font-mono uppercase">SAM Search Results for "{query}"</h3>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            {searchResults.map((r, idx) => (
              <div key={idx} className="p-4 rounded-xl bg-slate-950 border border-slate-800 space-y-2">
                <div className="flex items-center justify-between">
                  <span className="text-xs font-mono font-bold text-cyan-400">{r.docIdentifier} — {r.title}</span>
                  {r.found ? (
                    <span className="text-[10px] font-mono px-2 py-0.5 rounded bg-emerald-500/10 text-emerald-400 border border-emerald-500/20 flex items-center gap-1">
                      <CheckCircle2 className="h-3 w-3" /> MATCH
                    </span>
                  ) : (
                    <span className="text-[10px] font-mono px-2 py-0.5 rounded bg-slate-800 text-slate-500">NO MATCH</span>
                  )}
                </div>
                <p className="text-xs text-slate-400 font-mono">Type: {r.docType}</p>
                {r.matchedSnippet && (
                  <p className="text-xs font-mono text-emerald-300 bg-slate-900 p-2.5 rounded border border-slate-800">
                    Snippet: "...{r.matchedSnippet}..."
                  </p>
                )}
              </div>
            ))}
          </div>
        </div>
      )}

      {/* Repeated Substrings Analysis */}
      {analysisResults && (
        <div className="bg-slate-900/80 border border-slate-800 rounded-xl p-6 space-y-4">
          <h3 className="text-sm font-bold text-slate-200 font-mono uppercase text-emerald-400">Kasai Corpus Duplication Analysis</h3>
          <div className="p-4 rounded-xl bg-slate-950 border border-emerald-500/30 space-y-2">
            <span className="text-xs font-mono text-slate-400 block">Longest Repeated Substring Across Documents:</span>
            <span className="text-base font-bold font-mono text-emerald-300">"{analysisResults.longestRepeatedSubstring}"</span>
            <span className="text-xs font-mono text-slate-500 block">Length: {analysisResults.maxLcpValue} characters</span>
          </div>
        </div>
      )}

      {/* Database Documents List */}
      <div className="bg-slate-900/80 border border-slate-800 rounded-xl p-6 space-y-4">
        <h3 className="text-sm font-bold text-slate-200 font-mono uppercase">Seeded Railway Documents ({documents.length})</h3>
        <div className="space-y-3">
          {documents.map((doc) => (
            <div key={doc.id} className="p-4 rounded-xl bg-slate-950 border border-slate-800/80 space-y-2">
              <div className="flex items-center justify-between">
                <div className="flex items-center gap-3">
                  <FileText className="h-4 w-4 text-cyan-400" />
                  <span className="text-xs font-mono font-bold text-slate-200">{doc.docIdentifier}: {doc.title}</span>
                </div>
                <span className="text-[10px] font-mono px-2 py-0.5 rounded bg-slate-900 text-cyan-400 border border-slate-800">
                  {doc.docType}
                </span>
              </div>
              <p className="text-xs text-slate-300 font-mono bg-slate-900/60 p-3 rounded border border-slate-800">
                {doc.content}
              </p>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};
