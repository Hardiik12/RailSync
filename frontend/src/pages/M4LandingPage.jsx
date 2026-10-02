import React from 'react';
import { Link } from 'react-router-dom';
import { Network, ArrowRight, CheckCircle2, GitBranch, Share2, Layers, Shuffle, Scissors } from 'lucide-react';

export const M4LandingPage = () => {
  const algorithms = [
    {
      id: 'ford-fulkerson',
      name: 'Ford-Fulkerson Algorithm',
      path: '/dsa/m4/ford-fulkerson',
      complexityTime: 'O(E · F)',
      complexitySpace: 'O(V + E)',
      icon: GitBranch,
      useCase: 'Passenger flow through railway network connections using DFS augmenting paths.',
      highlights: ['DFS augmenting path search', 'Bottleneck capacity tracking', 'Residual edge capacity updates'],
    },
    {
      id: 'edmonds-karp',
      name: 'Edmonds-Karp Algorithm',
      path: '/dsa/m4/edmonds-karp',
      complexityTime: 'O(V · E²)',
      complexitySpace: 'O(V + E)',
      icon: Share2,
      useCase: 'Shortest augmenting path capacity analysis using BFS queue exploration.',
      highlights: ['BFS shortest path in edge count', 'Guaranteed polynomial time', 'Residual network backtracking'],
    },
    {
      id: 'dinic',
      name: "Dinic's Algorithm",
      path: '/dsa/m4/dinic',
      complexityTime: 'O(V² · E)',
      complexitySpace: 'O(V + E)',
      icon: Layers,
      useCase: 'Large passenger-flow network analysis using level graph + blocking flow.',
      highlights: ['BFS level graph construction', 'DFS blocking flow pushing', 'Pointer array work optimization'],
    },
    {
      id: 'bipartite-matching',
      name: 'Bipartite Maximum Matching',
      path: '/dsa/m4/bipartite-matching',
      complexityTime: 'O(V · E)',
      complexitySpace: 'O(V + E)',
      icon: Shuffle,
      useCase: 'Train/service to platform assignment optimization.',
      highlights: ['Left/right partition mapping', 'Augmenting path matching', 'Matched & unmatched vertex tracking'],
    },
    {
      id: 'konig',
      name: "König's Theorem Analysis",
      path: '/dsa/m4/konig',
      complexityTime: 'O(V · E)',
      complexitySpace: 'O(V + E)',
      icon: Network,
      useCase: 'Platform/service conflict analysis through max matching & min vertex cover equality.',
      highlights: ['Alternating path BFS reachability', 'Minimum vertex cover construction', 'Theorem equality verification'],
    },
    {
      id: 'max-flow-min-cut',
      name: 'Max-Flow Min-Cut Theorem',
      path: '/dsa/m4/max-flow-min-cut',
      complexityTime: 'O(V · E²)',
      complexitySpace: 'O(V + E)',
      icon: Scissors,
      useCase: 'Railway network bottleneck identification and cut capacity extraction.',
      highlights: ['Residual graph reachability cut', 'Source & sink cut partitions', 'Max-Flow == Min-Cut capacity proof'],
    },
  ];

  return (
    <div className="p-8 max-w-7xl mx-auto space-y-8">
      {/* Header */}
      <div className="space-y-3">
        <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-blue-500/10 border border-blue-500/20 text-blue-400 text-xs font-mono">
          <Network className="h-3.5 w-3.5" />
          <span>Module 4 — Network Flow (CO4)</span>
        </div>
        <h1 className="text-3xl font-bold text-slate-100 tracking-tight">
          Railway Network Flow & Optimization Systems
        </h1>
        <p className="text-slate-400 max-w-3xl text-sm leading-relaxed">
          Operational network flow algorithms for passenger throughput, augmenting paths, level-graph blocking flows,
          bipartite service-platform assignments, König's theorem vertex covers, and min-cut bottleneck extraction.
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
                    <div className="h-10 w-10 rounded-lg bg-slate-800 border border-slate-700 flex items-center justify-center text-blue-400 group-hover:scale-105 transition-transform">
                      <Icon className="h-5 w-5" />
                    </div>
                    <div>
                      <h2 className="text-base font-bold text-slate-100 group-hover:text-blue-300 transition-colors">
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
                      <CheckCircle2 className="h-3.5 w-3.5 text-blue-400 shrink-0" />
                      <span>{h}</span>
                    </li>
                  ))}
                </ul>
              </div>

              <div className="mt-6 pt-4 border-t border-slate-800/80 flex justify-end">
                <Link
                  to={algo.path}
                  className="inline-flex items-center gap-2 px-4 py-2 text-xs font-semibold rounded-lg bg-blue-500/10 text-blue-300 hover:bg-blue-500/20 border border-blue-500/30 transition-all"
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
