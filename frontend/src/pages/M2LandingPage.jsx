import React from 'react';
import { Link } from 'react-router-dom';
import { FileText, Cpu, Zap, Activity, ArrowRight, CheckCircle2, Search, Database } from 'lucide-react';

export const M2LandingPage = () => {
  const algorithms = [
    {
      id: 'suffix-array',
      name: 'Suffix Array',
      path: '/dsa/m2/suffix-array',
      complexityTime: 'O(n log² n)',
      complexitySpace: 'O(n)',
      icon: FileText,
      useCase: 'Railway document and service timetable indexing.',
      highlights: ['Prefix doubling ranking', 'Lexicographical suffix sorting', 'Foundation for LCP & search'],
    },
    {
      id: 'sa-is',
      name: 'SA-IS (Induced Sorting)',
      path: '/dsa/m2/sa-is',
      complexityTime: 'O(n)',
      complexitySpace: 'O(n)',
      icon: Zap,
      useCase: 'Large-scale linear-time railway text & document corpus indexing.',
      highlights: ['S/L classification', 'LMS identification & naming', 'Recursive reduced problem solving'],
    },
    {
      id: 'kasai',
      name: 'Kasai LCP Algorithm',
      path: '/dsa/m2/kasai',
      complexityTime: 'O(n)',
      complexitySpace: 'O(n)',
      icon: Activity,
      useCase: 'Linear-time Longest Common Prefix (LCP) array construction from text + Suffix Array.',
      highlights: ['Rank array lookup', 'Rolling LCP height h', 'Zero redundant character checks'],
    },
    {
      id: 'lcp',
      name: 'LCP Repeated Substring Analysis',
      path: '/dsa/m2/lcp',
      complexityTime: 'O(n)',
      complexitySpace: 'O(n)',
      icon: Search,
      useCase: 'Detecting duplicated operational notes, station announcements, and service templates.',
      highlights: ['Suffix Array + LCP pipeline', 'Max LCP value identification', 'Repeated substring extraction'],
    },
    {
      id: 'suffix-automaton',
      name: 'Suffix Automaton (SAM)',
      path: '/dsa/m2/suffix-automaton',
      complexityTime: 'O(n)',
      complexitySpace: 'O(n · |Σ|)',
      icon: Cpu,
      useCase: 'Linear-time substring search & automaton indexing over operational documents.',
      highlights: ['State transitions & suffix links', 'Clone state handling', 'O(|query|) substring queries'],
    },
    {
      id: 'documents',
      name: 'Railway Document Search Panel',
      path: '/operations/documents',
      complexityTime: 'O(n) per doc',
      complexitySpace: 'O(Corpus)',
      icon: Database,
      useCase: 'Search synthetic operational notices, timetables, and detect duplicated service instructions.',
      highlights: ['Flyway DB seeded documents', 'SAM substring search', 'Kasai LCP corpus analysis'],
    },
  ];

  return (
    <div className="p-8 max-w-7xl mx-auto space-y-8">
      {/* Header */}
      <div className="space-y-3">
        <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-cyan-500/10 border border-cyan-500/20 text-cyan-400 text-xs font-mono">
          <Activity className="h-3.5 w-3.5" />
          <span>Module 2 — Suffix Structures & Document Similarity (CO2)</span>
        </div>
        <h1 className="text-3xl font-bold text-slate-100 tracking-tight">
          Railway Text Indexing & Suffix Structures
        </h1>
        <p className="text-slate-400 max-w-3xl text-sm leading-relaxed">
          High-performance suffix structures for indexing railway documents, linear-time induced sorting (SA-IS),
          Kasai LCP repeated service description detection, and Suffix Automaton substring search.
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
                  <span>Launch Module</span>
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
