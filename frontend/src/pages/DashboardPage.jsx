import React from 'react';
import { Link } from 'react-router-dom';
import { Building2, Search, Cpu, Zap, Activity, ArrowRight, CheckCircle2, ShieldAlert } from 'lucide-react';

export const DashboardPage = () => {
  return (
    <div className="space-y-8 max-w-7xl mx-auto">
      {/* Header Banner */}
      <div className="bg-gradient-to-r from-slate-900 via-slate-900/90 to-cyan-950/40 p-8 rounded-2xl border border-slate-800/80 shadow-xl relative overflow-hidden">
        <div className="absolute right-0 top-0 translate-x-12 -translate-y-12 w-64 h-64 bg-cyan-500/10 rounded-full blur-3xl pointer-events-none"></div>
        <div className="max-w-3xl space-y-4 relative z-10">
          <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-cyan-500/10 border border-cyan-500/20 text-cyan-400 text-xs font-mono">
            <CheckCircle2 className="h-3.5 w-3.5" />
            First Vertical Slice Operational
          </div>
          <h1 className="text-3xl font-bold tracking-tight text-white">
            RailSync — Intelligent Railway Operations & DSA Analytics
          </h1>
          <p className="text-sm text-slate-400 leading-relaxed">
            Demonstrating advanced Data Structures & Algorithms applied to railway operations problems. 
            Explore live railway station infrastructure data and execute manual pattern-matching algorithms in the DSA Laboratory.
          </p>

          <div className="pt-2 flex flex-wrap gap-4">
            <Link
              to="/operations/stations"
              className="inline-flex items-center gap-2 px-4 py-2.5 rounded-lg bg-cyan-500 hover:bg-cyan-400 text-slate-950 font-semibold text-xs transition shadow-lg shadow-cyan-500/20"
            >
              <Building2 className="h-4 w-4" />
              Explore Stations Directory
            </Link>
            <Link
              to="/dsa/m1/kmp"
              className="inline-flex items-center gap-2 px-4 py-2.5 rounded-lg bg-slate-800 hover:bg-slate-700 text-slate-200 font-semibold text-xs border border-slate-700 transition"
            >
              <Search className="h-4 w-4 text-cyan-400" />
              Open KMP Lab (CO1)
            </Link>
          </div>
        </div>
      </div>

      {/* Synthetic Data Notice Banner */}
      <div className="p-4 rounded-xl bg-amber-500/10 border border-amber-500/20 text-amber-300 text-xs flex items-start gap-3">
        <ShieldAlert className="h-5 w-5 shrink-0 text-amber-400 mt-0.5" />
        <div>
          <span className="font-semibold text-amber-200">Simulation Notice:</span>
          <p className="text-amber-300/90 mt-0.5">
            The dataset seeded in this platform is synthetic and deterministic. 
            Station codes and names serve as structured domain inputs for algorithm demonstrations (such as KMP string search and flow networks) and do not reflect live Indian Railways operational infrastructure.
          </p>
        </div>
      </div>

      {/* Module Overview Cards */}
      <div>
        <h2 className="text-base font-semibold text-slate-200 mb-4 font-mono uppercase tracking-wider text-xs">
          DSA Syllabus Modules (CO1 - CO6)
        </h2>
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-5">
          {/* M1 */}
          <div className="p-6 rounded-xl bg-slate-900/60 border border-cyan-500/30 hover:border-cyan-500/60 transition space-y-4">
            <div className="flex items-center justify-between">
              <div className="h-10 w-10 rounded-lg bg-cyan-500/10 border border-cyan-500/30 flex items-center justify-center text-cyan-400">
                <Search className="h-5 w-5" />
              </div>
              <span className="text-xs font-mono px-2 py-0.5 rounded bg-cyan-500/20 text-cyan-300 border border-cyan-500/30">
                CO1 · READY
              </span>
            </div>
            <div>
              <h3 className="font-semibold text-white">M1 — String Algorithms</h3>
              <p className="text-xs text-slate-400 mt-1">
                KMP, Z-Function, Rabin-Karp, Aho-Corasick. Applied to train numbers, station names, and alert keyword filtering.
              </p>
            </div>
            <Link
              to="/dsa/m1/kmp"
              className="inline-flex items-center gap-1.5 text-xs text-cyan-400 font-semibold hover:text-cyan-300 transition"
            >
              Launch KMP Lab <ArrowRight className="h-3.5 w-3.5" />
            </Link>
          </div>

          {/* M2 */}
          <div className="p-6 rounded-xl bg-slate-900/40 border border-slate-800 opacity-75 space-y-4">
            <div className="flex items-center justify-between">
              <div className="h-10 w-10 rounded-lg bg-slate-800 flex items-center justify-center text-slate-400">
                <Cpu className="h-5 w-5" />
              </div>
              <span className="text-xs font-mono px-2 py-0.5 rounded bg-slate-800 text-slate-500 border border-slate-700">
                CO2 · PLANNED
              </span>
            </div>
            <div>
              <h3 className="font-semibold text-slate-300">M2 — Suffix Structures</h3>
              <p className="text-xs text-slate-500 mt-1">
                Suffix Array, SA-IS, LCP, Kasai, Suffix Automaton. Indexing railway documents and identifying duplicate operational descriptions.
              </p>
            </div>
          </div>

          {/* M3 */}
          <div className="p-6 rounded-xl bg-slate-900/40 border border-slate-800 opacity-75 space-y-4">
            <div className="flex items-center justify-between">
              <div className="h-10 w-10 rounded-lg bg-slate-800 flex items-center justify-center text-slate-400">
                <Zap className="h-5 w-5" />
              </div>
              <span className="text-xs font-mono px-2 py-0.5 rounded bg-slate-800 text-slate-500 border border-slate-700">
                CO3 · PLANNED
              </span>
            </div>
            <div>
              <h3 className="font-semibold text-slate-300">M3 — Advanced DP</h3>
              <p className="text-xs text-slate-500 mt-1">
                Levenshtein, Damerau, Bitmask DP, Matrix-Chain, Optimal BST. Typo correction in passenger search and bounded route optimization.
              </p>
            </div>
          </div>

          {/* M4 */}
          <div className="p-6 rounded-xl bg-slate-900/40 border border-slate-800 opacity-75 space-y-4">
            <div className="flex items-center justify-between">
              <div className="h-10 w-10 rounded-lg bg-slate-800 flex items-center justify-center text-slate-400">
                <Activity className="h-5 w-5" />
              </div>
              <span className="text-xs font-mono px-2 py-0.5 rounded bg-slate-800 text-slate-500 border border-slate-700">
                CO4 · PLANNED
              </span>
            </div>
            <div>
              <h3 className="font-semibold text-slate-300">M4 — Network Flow</h3>
              <p className="text-xs text-slate-500 mt-1">
                Ford-Fulkerson, Edmonds-Karp, Dinic, Bipartite Matching, Max-Flow Min-Cut. Passenger capacity and platform assignment matching.
              </p>
            </div>
          </div>

          {/* M5 */}
          <div className="p-6 rounded-xl bg-slate-900/40 border border-slate-800 opacity-75 space-y-4">
            <div className="flex items-center justify-between">
              <div className="h-10 w-10 rounded-lg bg-slate-800 flex items-center justify-center text-slate-400">
                <Cpu className="h-5 w-5" />
              </div>
              <span className="text-xs font-mono px-2 py-0.5 rounded bg-slate-800 text-slate-500 border border-slate-700">
                CO5 · PLANNED
              </span>
            </div>
            <div>
              <h3 className="font-semibold text-slate-300">M5 — NP-Completeness</h3>
              <p className="text-xs text-slate-500 mt-1">
                SAT, 3-SAT, CLIQUE, Independent Set, Vertex Cover. Reductions modeling railway scheduling and platform resource conflict constraints.
              </p>
            </div>
          </div>

          {/* M6 */}
          <div className="p-6 rounded-xl bg-slate-900/40 border border-slate-800 opacity-75 space-y-4">
            <div className="flex items-center justify-between">
              <div className="h-10 w-10 rounded-lg bg-slate-800 flex items-center justify-center text-slate-400">
                <Activity className="h-5 w-5" />
              </div>
              <span className="text-xs font-mono px-2 py-0.5 rounded bg-slate-800 text-slate-500 border border-slate-700">
                CO6 · PLANNED
              </span>
            </div>
            <div>
              <h3 className="font-semibold text-slate-300">M6 — Randomized & Parallel</h3>
              <p className="text-xs text-slate-500 mt-1">
                Randomized QuickSort, Reservoir Sampling, Miller-Rabin, Blelloch Scan, Parallel Reduce, Brent's theorem for streaming analytics.
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
