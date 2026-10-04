import React, { useState, useEffect } from 'react';
import {
  Network,
  GitCommit,
  Layers,
  Zap,
  Building2,
  Plus,
  Play,
  CheckCircle2,
  AlertTriangle,
  ArrowRight,
  ShieldCheck,
  Activity,
} from 'lucide-react';
import axios from 'axios';
import { DataOriginBadge } from '../components/common/DataOriginBadge';


export function NetworkPage() {
  const [networkData, setNetworkData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // Flow analysis form
  const [sourceCode, setSourceCode] = useState('CSMT');
  const [sinkCode, setSinkCode] = useState('PUNE');
  const [algorithm, setAlgorithm] = useState('DINIC');
  const [traceEnabled, setTraceEnabled] = useState(true);
  const [flowRunning, setFlowRunning] = useState(false);
  const [flowResult, setFlowResult] = useState(null);

  // Add edge form
  const [showAddEdge, setShowAddEdge] = useState(false);
  const [newFromCode, setNewFromCode] = useState('CSMT');
  const [newToCode, setNewToCode] = useState('PUNE');
  const [newCapacity, setNewCapacity] = useState('40.0');
  const [newDistance, setNewDistance] = useState('150.0');
  const [newTravelTime, setNewTravelTime] = useState('120');
  const [addEdgeError, setAddEdgeError] = useState(null);

  const fetchNetwork = async () => {
    try {
      setLoading(true);
      setError(null);
      const res = await axios.get('/api/network');
      if (res.data?.success) {
        setNetworkData(res.data.data);
        if (res.data.data.stations?.length > 0) {
          if (!sourceCode && res.data.data.stations[0]) setSourceCode(res.data.data.stations[0].stationCode);
          if (!sinkCode && res.data.data.stations[1]) setSinkCode(res.data.data.stations[1].stationCode);
        }
      }
    } catch (err) {
      setError(err.response?.data?.error?.message || 'Failed to load network topology');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchNetwork();
  }, []);

  const handleRunFlow = async () => {
    if (!sourceCode || !sinkCode) return;
    try {
      setFlowRunning(true);
      setFlowResult(null);
      setError(null);

      const res = await axios.post('/api/network/flow', {
        sourceStationCode: sourceCode,
        sinkStationCode: sinkCode,
        algorithm,
        traceEnabled,
      });

      if (res.data?.success) {
        setFlowResult(res.data.data);
      }
    } catch (err) {
      setError(err.response?.data?.error?.message || 'Failed to execute flow analysis');
    } finally {
      setFlowRunning(false);
    }
  };

  const handleCreateEdge = async (e) => {
    e.preventDefault();
    try {
      setAddEdgeError(null);
      const res = await axios.post('/api/network/edges', {
        fromStationCode: newFromCode,
        toStationCode: newToCode,
        capacity: parseFloat(newCapacity),
        distanceKm: parseFloat(newDistance),
        travelTimeMinutes: parseInt(newTravelTime, 10),
        dataOrigin: 'SYNTHETIC',
        sourceDataset: 'USER_CUSTOM_EDGE',
      });

      if (res.data?.success) {
        setShowAddEdge(false);
        fetchNetwork();
      }
    } catch (err) {
      setAddEdgeError(err.response?.data?.error?.message || 'Failed to create network edge');
    }
  };

  return (
    <div className="space-y-6 max-w-7xl mx-auto">
      {/* Header */}
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4 border-b border-[#263449] pb-5">
        <div>
          <div className="flex items-center gap-2 mb-1">
            <div className="h-7 w-7 rounded bg-teal-500/10 border border-teal-500/20 flex items-center justify-center text-teal-400">
              <Network className="h-4 w-4" />
            </div>
            <h1 className="text-xl font-bold text-[#F1F5F9] tracking-tight">
              Railway Network Analysis
            </h1>
          </div>
          <p className="text-xs text-[#94A3B8]">
            Graph network model connecting railway station nodes for Module M4 network flow algorithms.
          </p>
        </div>

        <button
          onClick={() => setShowAddEdge(!showAddEdge)}
          className="px-3.5 py-2 rounded-lg bg-[#172033] hover:bg-slate-800 text-teal-400 border border-[#263449] text-xs font-semibold flex items-center gap-2 transition"
        >
          <Plus className="h-4 w-4" />
          {showAddEdge ? 'Close Form' : 'Add Directed Edge'}
        </button>
      </div>

      {/* Notice Banner */}
      <div className="p-4 rounded-xl bg-[#111827] border border-[#263449] text-xs text-[#94A3B8] flex items-start gap-3">
        <ShieldCheck className="h-5 w-5 text-teal-400 shrink-0 mt-0.5" />
        <div>
          <span className="font-semibold text-[#F1F5F9]">Network Graph Capacity Model:</span>
          <p className="text-[#94A3B8] mt-0.5">
            Station nodes combine <code className="text-teal-400 font-mono">PUBLIC DATA</code> and <code className="text-[#94A3B8] font-mono">SYNTHETIC</code> records. Graph edges specify explicit track throughput capacity constraints for network flow optimization.
          </p>
        </div>
      </div>

      {/* Error Alert */}
      {error && (
        <div className="p-4 rounded-xl bg-rose-500/10 border border-rose-500/20 text-rose-400 text-xs flex items-center gap-3">
          <AlertTriangle className="h-5 w-5 shrink-0" />
          <span>{error}</span>
        </div>
      )}

      {/* Summary Cards */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
        <div className="p-4 rounded-xl bg-slate-900/60 border border-slate-800/80">
          <div className="text-[11px] font-mono text-slate-400 uppercase tracking-wider mb-1">
            Network Stations
          </div>
          <div className="text-2xl font-bold font-mono text-slate-100">
            {networkData?.stationCount ?? 0}
          </div>
          <div className="flex gap-2 mt-2 text-[10px] font-mono">
            <span className="text-emerald-400">{networkData?.publicStationCount ?? 0} Public</span>
            <span className="text-slate-500">•</span>
            <span className="text-cyan-400">{networkData?.syntheticStationCount ?? 0} Synthetic</span>
          </div>
        </div>

        <div className="p-4 rounded-xl bg-slate-900/60 border border-slate-800/80">
          <div className="text-[11px] font-mono text-slate-400 uppercase tracking-wider mb-1">
            Directed Network Edges
          </div>
          <div className="text-2xl font-bold font-mono text-cyan-400">
            {networkData?.edgeCount ?? 0}
          </div>
          <div className="flex gap-2 mt-2 text-[10px] font-mono">
            <span className="text-emerald-400">{networkData?.publicEdgeCount ?? 0} Public</span>
            <span className="text-slate-500">•</span>
            <span className="text-amber-400">{networkData?.syntheticEdgeCount ?? 0} Synthetic</span>
          </div>
        </div>

        <div className="p-4 rounded-xl bg-slate-900/60 border border-slate-800/80">
          <div className="text-[11px] font-mono text-slate-400 uppercase tracking-wider mb-1">
            M4 Flow Algorithms
          </div>
          <div className="text-sm font-semibold text-slate-200 mt-1">
            FF == EK == Dinic
          </div>
          <div className="text-[10px] font-mono text-emerald-400 mt-2">
            Cross-Validated Output
          </div>
        </div>

        <div className="p-4 rounded-xl bg-slate-900/60 border border-slate-800/80">
          <div className="text-[11px] font-mono text-slate-400 uppercase tracking-wider mb-1">
            Graph Adapter
          </div>
          <div className="text-sm font-semibold text-slate-200 mt-1">
            Dense Vertex Indexing
          </div>
          <div className="text-[10px] font-mono text-cyan-400 mt-2">
            0..V-1 Dynamic Mapping
          </div>
        </div>
      </div>

      {/* Add Edge Form Modal/Panel */}
      {showAddEdge && (
        <form onSubmit={handleCreateEdge} className="p-5 rounded-xl bg-slate-900/90 border border-cyan-500/30 space-y-4">
          <h3 className="text-sm font-semibold text-cyan-400 flex items-center gap-2">
            <Plus className="h-4 w-4" /> Add Directed Railway Network Edge
          </h3>
          {addEdgeError && (
            <div className="text-xs text-rose-400 bg-rose-500/10 p-2.5 rounded border border-rose-500/20">
              {addEdgeError}
            </div>
          )}
          <div className="grid grid-cols-1 md:grid-cols-5 gap-3 text-xs">
            <div>
              <label className="block text-slate-400 font-mono mb-1">From Station</label>
              <select
                value={newFromCode}
                onChange={(e) => setNewFromCode(e.target.value)}
                className="w-full bg-slate-800 border border-slate-700 rounded p-2 text-slate-200 font-mono"
              >
                {networkData?.stations?.map((s) => (
                  <option key={s.id} value={s.stationCode}>
                    {s.stationCode} - {s.name}
                  </option>
                ))}
              </select>
            </div>
            <div>
              <label className="block text-slate-400 font-mono mb-1">To Station</label>
              <select
                value={newToCode}
                onChange={(e) => setNewToCode(e.target.value)}
                className="w-full bg-slate-800 border border-slate-700 rounded p-2 text-slate-200 font-mono"
              >
                {networkData?.stations?.map((s) => (
                  <option key={s.id} value={s.stationCode}>
                    {s.stationCode} - {s.name}
                  </option>
                ))}
              </select>
            </div>
            <div>
              <label className="block text-slate-400 font-mono mb-1">Capacity</label>
              <input
                type="number"
                step="0.1"
                min="0"
                value={newCapacity}
                onChange={(e) => setNewCapacity(e.target.value)}
                className="w-full bg-slate-800 border border-slate-700 rounded p-2 text-slate-200 font-mono"
              />
            </div>
            <div>
              <label className="block text-slate-400 font-mono mb-1">Distance (km)</label>
              <input
                type="number"
                step="0.1"
                min="0"
                value={newDistance}
                onChange={(e) => setNewDistance(e.target.value)}
                className="w-full bg-slate-800 border border-slate-700 rounded p-2 text-slate-200 font-mono"
              />
            </div>
            <div>
              <label className="block text-slate-400 font-mono mb-1">Travel Time (min)</label>
              <input
                type="number"
                min="0"
                value={newTravelTime}
                onChange={(e) => setNewTravelTime(e.target.value)}
                className="w-full bg-slate-800 border border-slate-700 rounded p-2 text-slate-200 font-mono"
              />
            </div>
          </div>
          <div className="flex justify-end gap-3 pt-2">
            <button
              type="button"
              onClick={() => setShowAddEdge(false)}
              className="px-3 py-1.5 rounded bg-slate-800 text-slate-400 text-xs hover:bg-slate-700"
            >
              Cancel
            </button>
            <button
              type="submit"
              className="px-4 py-1.5 rounded bg-cyan-500/20 text-cyan-300 border border-cyan-500/40 text-xs font-semibold hover:bg-cyan-500/30"
            >
              Save Directed Edge
            </button>
          </div>
        </form>
      )}

      {/* Main Grid: Controls + Topology */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Controls Card */}
        <div className="p-5 rounded-xl bg-slate-900/60 border border-slate-800/80 space-y-4">
          <h2 className="text-xs font-mono uppercase tracking-wider text-slate-400 font-semibold flex items-center gap-2">
            <Activity className="h-4 w-4 text-cyan-400" />
            Network Flow Controls
          </h2>

          <div className="space-y-3 text-xs">
            <div>
              <label className="block text-slate-400 mb-1 font-mono">Source Station (s)</label>
              <select
                value={sourceCode}
                onChange={(e) => setSourceCode(e.target.value)}
                className="w-full bg-slate-950 border border-slate-800 rounded-lg p-2.5 text-slate-200 font-mono focus:border-cyan-500/50"
              >
                {networkData?.stations?.map((s) => (
                  <option key={s.id} value={s.stationCode}>
                    {s.stationCode} ({s.name})
                  </option>
                ))}
              </select>
            </div>

            <div>
              <label className="block text-slate-400 mb-1 font-mono">Sink Station (t)</label>
              <select
                value={sinkCode}
                onChange={(e) => setSinkCode(e.target.value)}
                className="w-full bg-slate-950 border border-slate-800 rounded-lg p-2.5 text-slate-200 font-mono focus:border-cyan-500/50"
              >
                {networkData?.stations?.map((s) => (
                  <option key={s.id} value={s.stationCode}>
                    {s.stationCode} ({s.name})
                  </option>
                ))}
              </select>
            </div>

            <div>
              <label className="block text-slate-400 mb-1 font-mono">Primary M4 Flow Engine</label>
              <select
                value={algorithm}
                onChange={(e) => setAlgorithm(e.target.value)}
                className="w-full bg-slate-950 border border-slate-800 rounded-lg p-2.5 text-slate-200 font-mono focus:border-cyan-500/50"
              >
                <option value="DINIC">Dinic's Algorithm (Level Graph + Blocking Flow)</option>
                <option value="FORD_FULKERSON">Ford-Fulkerson (Augmenting Paths)</option>
                <option value="EDMONDS_KARP">Edmonds-Karp (BFS Shortest Path)</option>
              </select>
            </div>

            <div className="flex items-center justify-between pt-1">
              <span className="text-slate-400 font-mono text-xs">Execution Trace</span>
              <label className="relative inline-flex items-center cursor-pointer">
                <input
                  type="checkbox"
                  checked={traceEnabled}
                  onChange={(e) => setTraceEnabled(e.target.checked)}
                  className="sr-only peer"
                />
                <div className="w-9 h-5 bg-slate-800 peer-focus:outline-none rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-slate-300 after:border-slate-300 after:border after:rounded-full after:h-4 after:w-4 after:transition-all peer-checked:bg-cyan-500"></div>
              </label>
            </div>

            <button
              onClick={handleRunFlow}
              disabled={flowRunning || !sourceCode || !sinkCode}
              className="w-full py-2.5 rounded-lg bg-cyan-500/20 hover:bg-cyan-500/30 text-cyan-300 border border-cyan-500/40 text-xs font-semibold flex items-center justify-center gap-2 transition disabled:opacity-50"
            >
              <Play className="h-4 w-4 fill-cyan-300" />
              {flowRunning ? 'Executing M4 Flow Engine...' : 'Run Network Flow Analysis'}
            </button>
          </div>
        </div>

        {/* Results & Edge Visualization Panel */}
        <div className="lg:col-span-2 space-y-6">
          {flowResult && (
            <div className="p-5 rounded-xl bg-slate-900/90 border border-cyan-500/30 space-y-4">
              <div className="flex items-center justify-between border-b border-slate-800 pb-3">
                <div className="flex items-center gap-2">
                  <CheckCircle2 className="h-5 w-5 text-emerald-400" />
                  <h3 className="font-semibold text-slate-100 text-sm">
                    Max Flow Calculation: <span className="text-cyan-400 font-mono">{flowResult.maxFlow} units</span>
                  </h3>
                </div>
                <div className="flex items-center gap-2">
                  <span className="px-2 py-0.5 rounded text-[10px] font-mono bg-emerald-500/10 text-emerald-400 border border-emerald-500/30">
                    FF == EK == Dinic MATCH
                  </span>
                </div>
              </div>

              {/* Algorithm Comparison Breakdown */}
              <div className="grid grid-cols-3 gap-3 text-xs font-mono">
                <div className="p-2.5 rounded bg-slate-950/60 border border-slate-800">
                  <span className="text-slate-500 text-[10px] block">Ford-Fulkerson</span>
                  <span className="text-slate-200 font-semibold">{flowResult.fordFulkersonFlow}</span>
                </div>
                <div className="p-2.5 rounded bg-slate-950/60 border border-slate-800">
                  <span className="text-slate-500 text-[10px]">Edmonds-Karp</span>
                  <span className="text-slate-200 font-semibold block">{flowResult.edmondsKarpFlow}</span>
                </div>
                <div className="p-2.5 rounded bg-slate-950/60 border border-cyan-500/30">
                  <span className="text-cyan-400 text-[10px]">Dinic Algorithm</span>
                  <span className="text-cyan-300 font-semibold block">{flowResult.dinicFlow}</span>
                </div>
              </div>
            </div>
          )}

          {/* Directed Edges Table */}
          <div className="p-5 rounded-xl bg-slate-900/60 border border-slate-800/80 space-y-4">
            <div className="flex items-center justify-between">
              <h2 className="text-xs font-mono uppercase tracking-wider text-slate-400 font-semibold flex items-center gap-2">
                <GitCommit className="h-4 w-4 text-cyan-400" />
                Network Graph Topology ({networkData?.edgeCount ?? 0} Edges)
              </h2>
            </div>

            <div className="overflow-x-auto">
              <table className="w-full text-left text-xs text-slate-300">
                <thead className="bg-slate-950/60 font-mono text-[10px] text-slate-400 uppercase tracking-wider border-b border-slate-800">
                  <tr>
                    <th className="py-2.5 px-3">From Station</th>
                    <th className="py-2.5 px-3">To Station</th>
                    <th className="py-2.5 px-3">Capacity</th>
                    <th className="py-2.5 px-3">Distance</th>
                    <th className="py-2.5 px-3">Travel Time</th>
                    <th className="py-2.5 px-3">Origin</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-slate-800/50">
                  {networkData?.edges?.map((edge) => (
                    <tr key={edge.id} className="hover:bg-slate-800/30">
                      <td className="py-2.5 px-3 font-mono font-semibold text-slate-200">
                        {edge.fromStationCode}
                      </td>
                      <td className="py-2.5 px-3 font-mono text-cyan-400">
                        {edge.toStationCode}
                      </td>
                      <td className="py-2.5 px-3 font-mono text-slate-200">
                        {edge.capacity}
                      </td>
                      <td className="py-2.5 px-3 text-slate-400">
                        {edge.distanceKm} km
                      </td>
                      <td className="py-2.5 px-3 text-slate-400">
                        {edge.travelTimeMinutes} min
                      </td>
                      <td className="py-2.5 px-3">
                        <span
                          className={`text-[10px] font-mono px-2 py-0.5 rounded border ${
                            edge.dataOrigin === 'PUBLIC_DATA'
                              ? 'bg-emerald-500/10 text-emerald-400 border-emerald-500/30'
                              : 'bg-amber-500/10 text-amber-400 border-amber-500/30'
                          }`}
                        >
                          {edge.dataOrigin}
                        </span>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
