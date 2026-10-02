import React from 'react';
import { Link } from 'react-router-dom';
import { Search, Zap, Hash, Cpu, ArrowRight, CheckCircle2, Activity } from 'lucide-react';

export const M1LandingPage = () => {
  const algorithms = [
    {
      id: 'kmp',
      name: 'Knuth-Morris-Pratt (KMP)',
      path: '/dsa/m1/kmp',
      complexityTime: 'O(n + m)',
      complexitySpace: 'O(m)',
      icon: Search,
      badgeColor: 'cyan',
      useCase: 'Exact station code lookup, route sequence matching, pattern prefix table optimization.',
      highlights: ['LPS Array (Longest Prefix Suffix)', 'Zero backtracking in text', 'Linear time processing'],
    },
    {
      id: 'z-function',
      name: 'Z-Function Algorithm',
      path: '/dsa/m1/z-function',
      complexityTime: 'O(n)',
      complexitySpace: 'O(n)',
      icon: Zap,
      badgeColor: 'emerald',
      useCase: 'Z-box technique for linear-time prefix matching & periodic timetable analysis.',
      highlights: ['Z-box sliding window [L, R]', 'Pattern + $ + Text formulation', 'Substring prefix reuse'],
    },
    {
      id: 'rabin-karp',
      name: 'Rabin-Karp Rolling Hash',
      path: '/dsa/m1/rabin-karp',
      complexityTime: 'O(n + m) avg',
      complexitySpace: 'O(1)',
      icon: Hash,
      badgeColor: 'purple',
      useCase: 'High-speed rolling hash search across long operational log streams with verification.',
      highlights: ['Polynomial rolling hash', 'Constant time window shift', 'Explicit collision verification'],
    },
    {
      id: 'aho-corasick',
      name: 'Aho-Corasick Multi-Pattern',
      path: '/dsa/m1/aho-corasick',
      complexityTime: 'O(n + Σ|P| + Z)',
      complexitySpace: 'O(Σ|P| · |Σ|)',
      icon: Cpu,
      badgeColor: 'amber',
      useCase: 'Automaton-based multi-keyword service alert detection in a single text traversal.',
      highlights: ['Trie structure', 'BFS Failure links', 'Single pass multi-keyword detection'],
    },
  ];

  return (
    <div className="p-8 max-w-7xl mx-auto space-y-8">
      {/* Header */}
      <div className="space-y-3">
        <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-cyan-500/10 border border-cyan-500/20 text-cyan-400 text-xs font-mono">
          <Activity className="h-3.5 w-3.5" />
          <span>Module 1 — String Algorithms (CO1)</span>
        </div>
        <h1 className="text-3xl font-bold text-slate-100 tracking-tight">
          Railway String Processing & Pattern Matching
        </h1>
        <p className="text-slate-400 max-w-3xl text-sm leading-relaxed">
          Operational analytics platform algorithms for exact pattern search, rolling hash stream processing,
          linear Z-box analysis, and multi-keyword service alert detection.
        </p>
      </div>

      {/* Grid of Algorithms */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        {algorithms.map((algo) => {
          const Icon = algo.icon;
          return (
            <div
              key={algo.id}
              className="bg-slate-900/80 border border-slate-800 rounded-xl p-6 hover:border-slate-700 transition-all flex flex-col justify-between group"
            >
              <div className="space-y-4">
                <div className="flex items-center justify-between">
                  <div className="flex items-center gap-3">
                    <div className="h-10 w-10 rounded-lg bg-slate-800 border border-slate-700 flex items-center justify-center text-cyan-400 group-hover:scale-105 transition-transform">
                      <Icon className="h-5 w-5" />
                    </div>
                    <div>
                      <h2 className="text-lg font-bold text-slate-100 group-hover:text-cyan-300 transition-colors">
                        {algo.name}
                      </h2>
                      <div className="flex items-center gap-2 mt-1">
                        <span className="text-[10px] font-mono px-2 py-0.5 rounded bg-slate-800 text-slate-300 border border-slate-700">
                          Time: {algo.complexityTime}
                        </span>
                        <span className="text-[10px] font-mono px-2 py-0.5 rounded bg-slate-800 text-slate-400 border border-slate-700">
                          Space: {algo.complexitySpace}
                        </span>
                      </div>
                    </div>
                  </div>
                </div>

                <p className="text-xs text-slate-300 leading-relaxed bg-slate-950/40 p-3 rounded-lg border border-slate-800/80">
                  <strong className="text-slate-200">Railway Use Case:</strong> {algo.useCase}
                </p>

                <ul className="space-y-2">
                  {algo.highlights.map((h, idx) => (
                    <li key={idx} className="flex items-center gap-2 text-xs text-slate-400">
                      <CheckCircle2 className="h-3.5 w-3.5 text-cyan-400 shrink-0" />
                      <span>{h}</span>
                    </li>
                  ))}
                </ul>
              </div>

              <div className="mt-6 pt-4 border-t border-slate-800/80 flex justify-end">
                <Link
                  to={algo.path}
                  className="inline-flex items-center gap-2 px-4 py-2 text-xs font-semibold rounded-lg bg-cyan-500/10 text-cyan-300 hover:bg-cyan-500/20 border border-cyan-500/30 transition-all"
                >
                  <span>Launch Visualizer</span>
                  <ArrowRight className="h-4 w-4" />
                </Link>
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
};
