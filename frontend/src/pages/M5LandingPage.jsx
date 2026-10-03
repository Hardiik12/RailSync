import React from 'react';
import { Link } from 'react-router-dom';
import { Cpu, ArrowRight, CheckCircle2, CheckSquare, Target, GitCommit, Layers, ShieldCheck } from 'lucide-react';

export const M5LandingPage = () => {
  const algorithms = [
    {
      id: 'sat',
      name: 'SAT Solver (DPLL)',
      path: '/dsa/m5/sat',
      complexityTime: 'O(2^V)',
      complexitySpace: 'O(V + C)',
      icon: CheckSquare,
      useCase: 'Operational constraint satisfaction modeling for railway service combinations.',
      highlights: ['Manual DPLL solver implementation', 'Unit propagation & pure literal elimination', 'Satisfying assignment generation'],
    },
    {
      id: '3sat',
      name: '3-SAT Solver',
      path: '/dsa/m5/3sat',
      complexityTime: 'O(2^V)',
      complexitySpace: 'O(V + C)',
      icon: Target,
      useCase: '3-literal canonical constraint checking for platform & departure rules.',
      highlights: ['Strict 3-literal clause validation', 'Bounded input validation (<= 20 vars)', 'Deterministic assignment calculation'],
    },
    {
      id: '3sat-to-clique',
      name: '3-SAT → CLIQUE Reduction',
      path: '/dsa/m5/3sat-to-clique',
      complexityTime: 'O(m²)',
      complexitySpace: 'O(m)',
      icon: GitCommit,
      useCase: 'Polynomial transformation from 3-SAT to graph clique compatibility network.',
      highlights: ['3m vertex graph construction', 'Cross-clause literal compatibility edges', 'Target clique size k = m proof'],
    },
    {
      id: 'clique-to-independent-set',
      name: 'CLIQUE → Independent Set',
      path: '/dsa/m5/clique-to-independent-set',
      complexityTime: 'O(V²)',
      complexitySpace: 'O(V + E)',
      icon: Layers,
      useCase: 'Graph complement transformation for non-interfering railway service selection.',
      highlights: ['Complement graph construction G → Ḡ', 'Exact target size k preservation', 'Equivalence verification'],
    },
    {
      id: 'independent-set-to-vertex-cover',
      name: 'Independent Set → Vertex Cover',
      path: '/dsa/m5/independent-set-to-vertex-cover',
      complexityTime: 'O(V + E)',
      complexitySpace: 'O(V)',
      icon: ShieldCheck,
      useCase: 'Conflict-graph transformation identifying critical monitoring stations.',
      highlights: ['Complement set relationship C = V \\ S', '|S| + |C| == |V| verification', 'Edge coverage assertion'],
    },
    {
      id: 'vertex-cover-2approx',
      name: 'Vertex Cover 2-Approximation',
      path: '/dsa/m5/vertex-cover-2approx',
      complexityTime: 'O(V + E)',
      complexitySpace: 'O(V + E)',
      icon: Cpu,
      useCase: 'Fast approximation of critical junction coverage for conflict analysis.',
      highlights: ['Maximal-matching edge selection', '|C| <= 2 * OPT theoretical bound', 'Guaranteed edge coverage check'],
    },
  ];

  return (
    <div className="p-8 max-w-7xl mx-auto space-y-8">
      {/* Header */}
      <div className="space-y-3">
        <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-purple-500/10 border border-purple-500/20 text-purple-400 text-xs font-mono">
          <Cpu className="h-3.5 w-3.5" />
          <span>Module 5 — NP-Completeness & Approximation (CO5)</span>
        </div>
        <h1 className="text-3xl font-bold text-slate-100 tracking-tight">
          Railway Complexity Analysis & Approximation Laboratory
        </h1>
        <p className="text-slate-400 max-w-3xl text-sm leading-relaxed">
          Educational laboratory demonstrating NP-completeness reductions (3-SAT → CLIQUE → Independent Set → Vertex Cover)
          and a maximal-matching based 2-approximation algorithm for railway constraint modeling and conflict analysis.
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
                    <div className="h-10 w-10 rounded-lg bg-slate-800 border border-slate-700 flex items-center justify-center text-purple-400 group-hover:scale-105 transition-transform">
                      <Icon className="h-5 w-5" />
                    </div>
                    <div>
                      <h2 className="text-base font-bold text-slate-100 group-hover:text-purple-300 transition-colors">
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
                      <CheckCircle2 className="h-3.5 w-3.5 text-purple-400 shrink-0" />
                      <span>{h}</span>
                    </li>
                  ))}
                </ul>
              </div>

              <div className="mt-6 pt-4 border-t border-slate-800/80 flex justify-end">
                <Link
                  to={algo.path}
                  className="inline-flex items-center gap-2 px-4 py-2 text-xs font-semibold rounded-lg bg-purple-500/10 text-purple-300 hover:bg-purple-500/20 border border-purple-500/30 transition-all"
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
