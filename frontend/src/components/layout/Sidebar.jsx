import React from 'react';
import { NavLink } from 'react-router-dom';
import {
  LayoutDashboard,
  Building2,
  Cpu,
  Search,
  Activity,
  FileText,
  LineChart,
  Terminal,
  Zap,
  Hash,
  Database,
  GitCommit,
  Binary,
  Layers,
  Network,
} from 'lucide-react';

export const Sidebar = () => {
  const navGroups = [
    {
      title: 'Platform Overview',
      items: [
        { name: 'Dashboard', path: '/dashboard', icon: LayoutDashboard },
        { name: 'Station Directory', path: '/operations/stations', icon: Building2 },
        { name: 'Railway Documents', path: '/operations/documents', icon: Database, tag: 'INDEX' },
      ],
    },
    {
      title: 'DSA Module 1: String Algorithms (CO1)',
      items: [
        { name: 'M1 Overview', path: '/dsa/m1', icon: Search, tag: 'CO1' },
        { name: 'KMP Search', path: '/dsa/m1/kmp', icon: Search, tag: 'KMP' },
        { name: 'Z-Function', path: '/dsa/m1/z-function', icon: Zap, tag: 'Z' },
        { name: 'Rabin-Karp', path: '/dsa/m1/rabin-karp', icon: Hash, tag: 'RK' },
        { name: 'Aho-Corasick', path: '/dsa/m1/aho-corasick', icon: Cpu, tag: 'AC' },
      ],
    },
    {
      title: 'DSA Module 2: Suffix Structures (CO2)',
      items: [
        { name: 'M2 Overview', path: '/dsa/m2', icon: FileText, tag: 'CO2' },
        { name: 'Suffix Array', path: '/dsa/m2/suffix-array', icon: FileText, tag: 'SA' },
        { name: 'SA-IS (Induced)', path: '/dsa/m2/sa-is', icon: Zap, tag: 'SA-IS' },
        { name: 'Kasai LCP', path: '/dsa/m2/kasai', icon: Activity, tag: 'KASAI' },
        { name: 'LCP Substring', path: '/dsa/m2/lcp', icon: Search, tag: 'LCP' },
        { name: 'Suffix Automaton', path: '/dsa/m2/suffix-automaton', icon: Cpu, tag: 'SAM' },
      ],
    },
    {
      title: 'DSA Module 3: Advanced DP (CO3)',
      items: [
        { name: 'M3 Overview', path: '/dsa/m3', icon: Zap, tag: 'CO3' },
        { name: 'Levenshtein', path: '/dsa/m3/levenshtein', icon: GitCommit, tag: 'LEV' },
        { name: 'Damerau-Levenshtein', path: '/dsa/m3/damerau', icon: Activity, tag: 'DAM' },
        { name: 'Bitmask DP', path: '/dsa/m3/bitmask', icon: Binary, tag: 'MASK' },
        { name: 'Matrix-Chain DP', path: '/dsa/m3/matrix-chain', icon: Layers, tag: 'MCM' },
        { name: 'Optimal BST', path: '/dsa/m3/optimal-bst', icon: Network, tag: 'OBST' },
      ],
    },
    {
      title: 'DSA Module 4: Network Flow (CO4)',
      items: [
        { name: 'M4 Overview', path: '/dsa/m4', icon: Network, tag: 'CO4' },
        { name: 'Ford-Fulkerson', path: '/dsa/m4/ford-fulkerson', icon: GitCommit, tag: 'FF' },
        { name: 'Edmonds-Karp', path: '/dsa/m4/edmonds-karp', icon: Activity, tag: 'EK' },
        { name: 'Dinic Algorithm', path: '/dsa/m4/dinic', icon: Layers, tag: 'DINIC' },
        { name: 'Bipartite Matching', path: '/dsa/m4/bipartite-matching', icon: Binary, tag: 'BIP' },
        { name: 'König Theorem', path: '/dsa/m4/konig', icon: Network, tag: 'KONIG' },
        { name: 'Max-Flow Min-Cut', path: '/dsa/m4/max-flow-min-cut', icon: Zap, tag: 'CUT' },
      ],
    },
    {
      title: 'Future DSA Modules (CO5 - CO6)',
      items: [
        { name: 'M5: NP-Completeness', path: '/dsa/m5', icon: Cpu, tag: 'CO5', disabled: true },
        { name: 'M6: Parallel & Random', path: '/dsa/m6', icon: LineChart, tag: 'CO6', disabled: true },
      ],
    },
  ];

  return (
    <aside className="w-64 bg-slate-900/90 backdrop-blur border-r border-slate-800/80 flex flex-col shrink-0">
      {/* Brand Header */}
      <div className="h-16 flex items-center px-6 border-b border-slate-800/80 gap-3">
        <div className="h-9 w-9 rounded-lg bg-cyan-500/10 border border-cyan-500/30 flex items-center justify-center text-cyan-400">
          <Terminal className="h-5 w-5" />
        </div>
        <div>
          <h1 className="font-bold text-lg tracking-tight bg-gradient-to-r from-slate-100 via-cyan-100 to-cyan-400 bg-clip-text text-transparent">
            RailSync
          </h1>
          <p className="text-[10px] uppercase font-mono tracking-wider text-cyan-400/70 font-medium">
            Control Center v1.0
          </p>
        </div>
      </div>

      {/* Navigation */}
      <div className="flex-1 overflow-y-auto px-4 py-6 space-y-6">
        {navGroups.map((group, idx) => (
          <div key={idx} className="space-y-2">
            <h2 className="px-3 text-[11px] font-mono uppercase tracking-wider text-slate-500 font-semibold">
              {group.title}
            </h2>
            <nav className="space-y-1">
              {group.items.map((item) => {
                const Icon = item.icon;
                if (item.disabled) {
                  return (
                    <div
                      key={item.name}
                      className="flex items-center justify-between px-3 py-2 text-xs font-medium text-slate-600 cursor-not-allowed opacity-60 rounded-md"
                    >
                      <div className="flex items-center gap-2.5">
                        <Icon className="h-4 w-4" />
                        <span>{item.name}</span>
                      </div>
                      {item.tag && (
                        <span className="text-[10px] font-mono px-1.5 py-0.5 rounded bg-slate-800/50 text-slate-500 border border-slate-800">
                          {item.tag}
                        </span>
                      )}
                    </div>
                  );
                }

                return (
                  <NavLink
                    key={item.name}
                    to={item.path}
                    className={({ isActive }) =>
                      `flex items-center justify-between px-3 py-2 text-xs font-medium rounded-md transition-all duration-150 ${
                        isActive
                          ? 'bg-cyan-500/10 text-cyan-300 border border-cyan-500/30 shadow-sm shadow-cyan-500/5'
                          : 'text-slate-400 hover:text-slate-200 hover:bg-slate-800/50'
                      }`
                    }
                  >
                    <div className="flex items-center gap-2.5">
                      <Icon className="h-4 w-4" />
                      <span>{item.name}</span>
                    </div>
                    {item.tag && (
                      <span className="text-[10px] font-mono px-1.5 py-0.5 rounded bg-cyan-500/10 text-cyan-400 border border-cyan-500/20">
                        {item.tag}
                      </span>
                    )}
                  </NavLink>
                );
              })}
            </nav>
          </div>
        ))}
      </div>

      {/* Footer / System Status */}
      <div className="p-4 border-t border-slate-800/80 bg-slate-950/40">
        <div className="flex items-center justify-between text-xs text-slate-400">
          <span className="flex items-center gap-2">
            <span className="h-2 w-2 rounded-full bg-emerald-400 animate-pulse"></span>
            System Engine
          </span>
          <span className="font-mono text-[10px] text-emerald-400">ONLINE</span>
        </div>
      </div>
    </aside>
  );
};
