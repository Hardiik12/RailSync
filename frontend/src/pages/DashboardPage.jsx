import React from 'react';
import { Link } from 'react-router-dom';
import { Building2, Search, Cpu, Zap, Network, Layers, ArrowRight, Database, FileText } from 'lucide-react';

export const DashboardPage = () => {
  const summaryMetrics = [
    { label: 'Stations', count: 20, icon: Building2, color: 'text-teal-400' },
    { label: 'Network Edges', count: 8, icon: Network, color: 'text-teal-400' },
    { label: 'Railway Documents', count: 18, icon: FileText, color: 'text-teal-400' },
    { label: 'Public Data', count: 2, icon: Database, color: 'text-emerald-400' },
    { label: 'Synthetic Data', count: 18, icon: Layers, color: 'text-slate-400' },
  ];

  const modules = [
    {
      id: 'm1',
      title: 'M1 — String Algorithms',
      countText: '4 algorithms',
      description: 'KMP, Z-Function, Rabin-Karp, Aho-Corasick for station search and alert pattern matching.',
      path: '/dsa/m1/kmp',
      icon: Search,
    },
    {
      id: 'm2',
      title: 'M2 — Suffix Structures',
      countText: '5 algorithms',
      description: 'Suffix Array, SA-IS, LCP, Kasai, Suffix Automaton for document indexing and duplicate detection.',
      path: '/dsa/m2/suffix-array',
      icon: Layers,
    },
    {
      id: 'm3',
      title: 'M3 — Dynamic Programming',
      countText: '5 algorithms',
      description: 'Levenshtein, Damerau-Levenshtein, Bitmask DP, Matrix-Chain, Optimal BST for typo correction and route optimization.',
      path: '/dsa/m3/levenshtein',
      icon: Zap,
    },
    {
      id: 'm4',
      title: 'M4 — Network Flow',
      countText: '6 algorithms',
      description: 'Ford-Fulkerson, Edmonds-Karp, Dinic, Bipartite Matching, König, Max-Flow Min-Cut for track capacity.',
      path: '/dsa/m4/dinic',
      icon: Network,
    },
    {
      id: 'm5',
      title: 'M5 — Complexity & Approximation',
      countText: '6 algorithms',
      description: 'SAT, 3-SAT, CLIQUE, Independent Set, Vertex Cover, 2-Approx for scheduling constraints.',
      path: '/dsa/m5/sat',
      icon: Cpu,
    },
  ];

  return (
    <div className="space-y-6 max-w-7xl mx-auto">
      {/* Compact Hero Section */}
      <div className="bg-[#111827] border border-[#263449] p-6 rounded-xl space-y-3">
        <div className="space-y-1">
          <div className="flex items-center gap-3">
            <h1 className="text-2xl font-bold text-[#F1F5F9] tracking-tight">RailSync</h1>
            <span className="text-xs font-mono px-2.5 py-0.5 rounded bg-teal-500/10 text-teal-400 border border-teal-500/20">
              Railway Operations & DSA Workbench
            </span>
          </div>
          <p className="text-xs text-[#94A3B8] leading-relaxed max-w-3xl">
            Explore railway data, analyze network flow, and run DSA algorithms through an interactive workbench.
          </p>
        </div>

        <div className="pt-2 flex flex-wrap gap-3">
          <Link
            to="/operations/stations"
            className="inline-flex items-center gap-2 px-3.5 py-2 rounded-lg bg-teal-600 hover:bg-teal-500 text-white font-medium text-xs transition"
          >
            <Building2 className="h-4 w-4" />
            View Stations
          </Link>
          <Link
            to="/operations/network"
            className="inline-flex items-center gap-2 px-3.5 py-2 rounded-lg bg-[#172033] hover:bg-slate-800 text-[#F1F5F9] font-medium text-xs border border-[#263449] transition"
          >
            <Network className="h-4 w-4 text-teal-400" />
            Open Network
          </Link>
          <Link
            to="/dsa/m1/kmp"
            className="inline-flex items-center gap-2 px-3.5 py-2 rounded-lg bg-[#172033] hover:bg-slate-800 text-[#F1F5F9] font-medium text-xs border border-[#263449] transition"
          >
            <Search className="h-4 w-4 text-teal-400" />
            Explore DSA
          </Link>
        </div>
      </div>

      {/* Operational Data Summary Metrics */}
      <div>
        <h2 className="text-xs font-mono uppercase tracking-wider text-[#94A3B8] font-semibold mb-3">
          Operational Data Summary
        </h2>
        <div className="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-5 gap-3">
          {summaryMetrics.map((item, idx) => {
            const Icon = item.icon;
            return (
              <div key={idx} className="bg-[#111827] border border-[#263449] p-4 rounded-xl space-y-1">
                <div className="flex items-center justify-between text-xs text-[#94A3B8]">
                  <span>{item.label}</span>
                  <Icon className={`h-4 w-4 ${item.color}`} />
                </div>
                <div className="text-xl font-bold font-mono text-[#F1F5F9]">
                  {item.count}
                </div>
              </div>
            );
          })}
        </div>
      </div>

      {/* DSA Modules List */}
      <div>
        <h2 className="text-xs font-mono uppercase tracking-wider text-[#94A3B8] font-semibold mb-3">
          DSA Syllabus Modules (CO1 - CO5)
        </h2>
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          {modules.map((mod) => {
            const Icon = mod.icon;
            return (
              <div key={mod.id} className="bg-[#111827] border border-[#263449] p-5 rounded-xl flex flex-col justify-between space-y-4">
                <div className="space-y-2">
                  <div className="flex items-center justify-between">
                    <div className="flex items-center gap-2">
                      <div className="p-2 rounded bg-teal-500/10 text-teal-400">
                        <Icon className="h-4 w-4" />
                      </div>
                      <h3 className="font-semibold text-[#F1F5F9] text-sm">{mod.title}</h3>
                    </div>
                    <span className="text-[10px] font-mono px-2 py-0.5 rounded bg-[#172033] text-[#94A3B8] border border-[#263449]">
                      {mod.countText}
                    </span>
                  </div>
                  <p className="text-xs text-[#94A3B8] leading-normal">
                    {mod.description}
                  </p>
                </div>
                <Link
                  to={mod.path}
                  className="inline-flex items-center justify-between px-3 py-2 rounded bg-[#172033] hover:bg-teal-600 hover:text-white text-xs font-medium text-teal-400 border border-[#263449] transition"
                >
                  <span>Open Module</span>
                  <ArrowRight className="h-3.5 w-3.5" />
                </Link>
              </div>
            );
          })}
        </div>
      </div>
    </div>
  );
};

