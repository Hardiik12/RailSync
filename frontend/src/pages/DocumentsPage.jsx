import React, { useState, useEffect } from 'react';
import { fetchDocuments, searchDocuments, analyzeDocumentRepeatedSubstrings, indexStationDocuments } from '../api/m2Api';
import { Database, Search, FileText, Activity, CheckCircle2, Layers } from 'lucide-react';

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
    <div className="p-6 md:p-8 max-w-7xl mx-auto space-y-6">
      {/* Header */}
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4 border-b border-[#263449] pb-5">
        <div>
          <h1 className="text-xl md:text-2xl font-semibold text-[#F1F5F9] tracking-tight">
            Railway Documents
          </h1>
          <p className="text-sm text-[#94A3B8] mt-1">
            Indexed operational logs, timetables, and station descriptions for substring search and analysis.
          </p>
        </div>
        <div className="flex items-center gap-3">
          <button
            onClick={handleIndexStations}
            disabled={indexing}
            className="flex items-center gap-2 px-3.5 py-2 rounded-lg bg-[#172033] hover:bg-[#1f2c47] border border-[#263449] text-sm text-[#F1F5F9] font-medium transition-colors disabled:opacity-50"
          >
            <Layers className="h-4 w-4 text-teal-400" />
            <span>{indexing ? 'Indexing Public Stations...' : 'Index Station Documents'}</span>
          </button>
          <button
            onClick={handleAnalyzeCorpus}
            disabled={analyzing}
            className="flex items-center gap-2 px-3.5 py-2 rounded-lg bg-[#172033] hover:bg-[#1f2c47] border border-[#263449] text-sm text-[#F1F5F9] font-medium transition-colors disabled:opacity-50"
          >
            <Activity className="h-4 w-4 text-emerald-400" />
            <span>{analyzing ? 'Analyzing Corpus...' : 'Detect Duplicated Substrings'}</span>
          </button>
        </div>
      </div>

      {error && (
        <div className="p-4 rounded-lg bg-rose-500/10 border border-rose-500/20 text-rose-400 text-xs">
          {error}
        </div>
      )}

      {/* Station Indexing Result Banner */}
      {indexResult && (
        <div className="p-5 rounded-lg bg-[#111827] border border-[#263449] space-y-3">
          <div className="flex items-center justify-between">
            <span className="text-xs font-semibold text-teal-400 uppercase tracking-wider flex items-center gap-2">
              <CheckCircle2 className="h-4 w-4" /> Station Document Indexing Result
            </span>
            <span className="text-xs text-[#94A3B8]">Source: {indexResult.source}</span>
          </div>
          <div className="grid grid-cols-2 md:grid-cols-5 gap-3 text-xs">
            <div className="bg-[#172033] p-3 rounded border border-[#263449]">
              <span className="text-[#94A3B8] block text-[11px] mb-0.5">Records Read</span>
              <span className="text-[#F1F5F9] font-semibold text-sm">{indexResult.recordsConsidered}</span>
            </div>
            <div className="bg-[#172033] p-3 rounded border border-[#263449]">
              <span className="text-[#94A3B8] block text-[11px] mb-0.5">Docs Created</span>
              <span className="text-emerald-400 font-semibold text-sm">{indexResult.documentsCreated}</span>
            </div>
            <div className="bg-[#172033] p-3 rounded border border-[#263449]">
              <span className="text-[#94A3B8] block text-[11px] mb-0.5">Docs Updated</span>
              <span className="text-teal-400 font-semibold text-sm">{indexResult.documentsUpdated}</span>
            </div>
            <div className="bg-[#172033] p-3 rounded border border-[#263449]">
              <span className="text-[#94A3B8] block text-[11px] mb-0.5">Skipped</span>
              <span className="text-amber-400 font-semibold text-sm">{indexResult.skippedRecords}</span>
            </div>
            <div className="bg-[#172033] p-3 rounded border border-[#263449]">
              <span className="text-[#94A3B8] block text-[11px] mb-0.5">Duration</span>
              <span className="text-[#F1F5F9] font-semibold text-sm">{indexResult.durationMillis} ms</span>
            </div>
          </div>
        </div>
      )}

      {/* Substring Search Form */}
      <form onSubmit={handleSearch} className="bg-[#111827] border border-[#263449] rounded-xl p-5 space-y-4">
        <div>
          <label className="block text-xs font-semibold text-[#94A3B8] uppercase tracking-wider mb-2">
            Search Substring Across Documents (Suffix Automaton)
          </label>
          <div className="flex gap-3">
            <input
              type="text"
              value={query}
              onChange={(e) => setQuery(e.target.value)}
              className="flex-1 bg-[#172033] border border-[#263449] rounded-lg px-3.5 py-2 text-sm text-[#F1F5F9] focus:outline-none focus:border-teal-500/50"
              placeholder="Enter search substring..."
              required
            />
            <button
              type="submit"
              disabled={searching}
              className="flex items-center gap-2 px-4 py-2 rounded-lg bg-teal-600 hover:bg-teal-500 text-white text-xs font-medium transition-colors disabled:opacity-50"
            >
              <Search className="h-4 w-4" />
              <span>{searching ? 'Searching...' : 'Search Documents'}</span>
            </button>
          </div>
        </div>
      </form>

      {/* Search Results */}
      {searchResults && (
        <div className="bg-[#111827] border border-[#263449] rounded-xl p-5 space-y-4">
          <h3 className="text-xs font-semibold text-[#94A3B8] uppercase tracking-wider">
            Search Results for "{query}"
          </h3>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            {searchResults.map((r, idx) => (
              <div key={idx} className="p-4 rounded-lg bg-[#172033] border border-[#263449] space-y-2">
                <div className="flex items-center justify-between">
                  <span className="text-xs font-semibold text-teal-400">{r.docIdentifier} — {r.title}</span>
                  {r.found ? (
                    <span className="text-[10px] font-medium px-2 py-0.5 rounded bg-emerald-500/10 text-emerald-400 border border-emerald-500/20 flex items-center gap-1">
                      <CheckCircle2 className="h-3 w-3" /> MATCH
                    </span>
                  ) : (
                    <span className="text-[10px] font-medium px-2 py-0.5 rounded bg-slate-800 text-[#94A3B8]">NO MATCH</span>
                  )}
                </div>
                <p className="text-xs text-[#94A3B8]">Type: {r.docType}</p>
                {r.matchedSnippet && (
                  <p className="text-xs text-emerald-300 bg-[#0B1220] p-2.5 rounded border border-[#263449]">
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
        <div className="bg-[#111827] border border-[#263449] rounded-xl p-5 space-y-4">
          <h3 className="text-xs font-semibold text-emerald-400 uppercase tracking-wider">
            Corpus Duplication Analysis (Kasai LCP)
          </h3>
          <div className="p-4 rounded-lg bg-[#172033] border border-emerald-500/20 space-y-1">
            <span className="text-xs text-[#94A3B8] block">Longest Repeated Substring Across Documents:</span>
            <span className="text-base font-semibold text-emerald-300">"{analysisResults.longestRepeatedSubstring}"</span>
            <span className="text-xs text-[#94A3B8] block">Length: {analysisResults.maxLcpValue} characters</span>
          </div>
        </div>
      )}

      {/* Database Documents List */}
      <div className="bg-[#111827] border border-[#263449] rounded-xl p-5 space-y-4">
        <h3 className="text-xs font-semibold text-[#94A3B8] uppercase tracking-wider">
          Indexed Railway Documents ({documents.length})
        </h3>
        <div className="space-y-3">
          {documents.map((doc) => (
            <div key={doc.id} className="p-4 rounded-lg bg-[#172033] border border-[#263449] space-y-2">
              <div className="flex items-center justify-between">
                <div className="flex items-center gap-2.5">
                  <FileText className="h-4 w-4 text-teal-400" />
                  <span className="text-xs font-semibold text-[#F1F5F9]">{doc.docIdentifier}: {doc.title}</span>
                </div>
                <span className="text-[10px] font-medium px-2 py-0.5 rounded bg-[#0B1220] text-teal-400 border border-[#263449]">
                  {doc.docType}
                </span>
              </div>
              <p className="text-xs text-[#94A3B8] bg-[#0B1220] p-3 rounded border border-[#263449]">
                {doc.content}
              </p>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

