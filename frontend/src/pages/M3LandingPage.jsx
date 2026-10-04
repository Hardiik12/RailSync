import React from 'react';
import { Link } from 'react-router-dom';
import { Zap, ArrowRight, CheckCircle2, Activity, GitCommit, Network, Layers, Binary } from 'lucide-react';

export const M3LandingPage = () => {
  const algorithms = [
    {
      id: 'levenshtein',
      name: 'Levenshtein Edit Distance',
      path: '/dsa/m3/levenshtein',
      complexityTime: 'O(n · m)',
      complexitySpace: 'O(n · m)',
      icon: GitCommit,
      useCase: 'Station/passenger text correction (Insert, Delete, Substitute).',
      highlights: ['Full DP matrix construction', 'Exact edit path backtracking', 'Station name spell correction'],
    },
    {
      id: 'damerau',
      name: 'Damerau-Levenshtein Distance',
      path: '/dsa/m3/damerau',
      complexityTime: 'O(n · m)',
      complexitySpace: 'O(n · m)',
      icon: Activity,
      useCase: 'Restricted adjacent transposition station and code spell correction.',
      highlights: ['Adjacent transposition support', 'Damerau vs Levenshtein comparison', 'Visual operation tags'],
    },
    {
      id: 'bitmask',
      name: 'Bitmask DP — Minimum Hamiltonian Route',
      path: '/dsa/m3/bitmask',
      complexityTime: 'O(2ⁿ · n²)',
      complexitySpace: 'O(2ⁿ · n)',
      icon: Binary,
      useCase: 'Bounded exponential subset DP computing minimum Hamiltonian route starting at startNode (N ≤ 16).',
      highlights: ['Subset DP dp[mask][u]', 'Binary mask state tracking', 'Safety input limits'],
    },
    {
      id: 'matrix-chain',
      name: 'Matrix-Chain Multiplication',
      path: '/dsa/m3/matrix-chain',
      complexityTime: 'O(n³)',
      complexitySpace: 'O(n²)',
      icon: Layers,
      useCase: 'Model for ordering compatible railway data operations.',
      highlights: ['Interval DP dp[i][j]', 'Optimal parenthesization', 'Split table reconstruction'],
    },
    {
      id: 'optimal-bst',
      name: 'Optimal Binary Search Tree',
      path: '/dsa/m3/optimal-bst',
      complexityTime: 'O(n³)',
      complexitySpace: 'O(n²)',
      icon: Network,
      useCase: 'Frequently accessed station/service record access simulation.',
      highlights: ['Search cost minimization', 'Root table reconstruction', 'Optimal BST tree visualization'],
    },
  ];

  return (
    <div className="p-8 max-w-7xl mx-auto space-y-8">
      {/* Header */}
      <div className="space-y-3">
        <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-cyan-500/10 border border-cyan-500/20 text-cyan-400 text-xs font-mono">
          <Zap className="h-3.5 w-3.5" />
          <span>Module 3 — Advanced Dynamic Programming (CO3)</span>
        </div>
        <h1 className="text-3xl font-bold text-slate-100 tracking-tight">
          Railway Dynamic Programming & Optimal Decision Systems
        </h1>
        <p className="text-slate-400 max-w-3xl text-sm leading-relaxed">
          Operational DP algorithms for text correction, transposition-aware edit distance, bitmask subset route minimization,
          interval DP matrix ordering, and optimal search tree construction.
        </p>
      </div>

      {/* Grid of Algorithms */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
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
                      <h2 className="text-base font-bold text-slate-100 group-hover:text-cyan-300 transition-colors">
                        {algo.name}
                      </h2>
                      <div className="flex items-center gap-2 mt-1">
                        <span className="text-[10px] font-mono px-2 py-0.5 rounded bg-slate-800 text-slate-300 border border-slate-700">
                          Time: {algo.complexityTime}
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
