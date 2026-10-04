import React, { useState } from 'react';
import { NavLink, useLocation } from 'react-router-dom';
import {
  LayoutDashboard,
  Building2,
  Network,
  Database,
  Search,
  FileText,
  Zap,
  Cpu,
  Settings,
  ChevronDown,
  ChevronRight,
  X,
  Train,
} from 'lucide-react';

export const Sidebar = ({ isOpen, onClose }) => {
  const location = useLocation();

  const dsaModules = [
    {
      id: 'm1',
      name: 'M1 · String Algorithms',
      path: '/dsa/m1/kmp',
      matchPrefix: '/dsa/m1',
      items: [
        { name: 'KMP Search', path: '/dsa/m1/kmp' },
        { name: 'Z-Function', path: '/dsa/m1/z-function' },
        { name: 'Rabin-Karp', path: '/dsa/m1/rabin-karp' },
        { name: 'Aho-Corasick', path: '/dsa/m1/aho-corasick' },
      ],
    },
    {
      id: 'm2',
      name: 'M2 · Suffix Structures',
      path: '/dsa/m2/suffix-array',
      matchPrefix: '/dsa/m2',
      items: [
        { name: 'Suffix Array', path: '/dsa/m2/suffix-array' },
        { name: 'SA-IS (Induced)', path: '/dsa/m2/sa-is' },
        { name: 'Kasai LCP', path: '/dsa/m2/kasai' },
        { name: 'LCP Substring', path: '/dsa/m2/lcp' },
        { name: 'Suffix Automaton', path: '/dsa/m2/suffix-automaton' },
      ],
    },
    {
      id: 'm3',
      name: 'M3 · Dynamic Programming',
      path: '/dsa/m3/levenshtein',
      matchPrefix: '/dsa/m3',
      items: [
        { name: 'Levenshtein', path: '/dsa/m3/levenshtein' },
        { name: 'Damerau-Levenshtein', path: '/dsa/m3/damerau' },
        { name: 'Bitmask DP', path: '/dsa/m3/bitmask' },
        { name: 'Matrix-Chain DP', path: '/dsa/m3/matrix-chain' },
        { name: 'Optimal BST', path: '/dsa/m3/optimal-bst' },
      ],
    },
    {
      id: 'm4',
      name: 'M4 · Network Flow',
      path: '/dsa/m4/dinic',
      matchPrefix: '/dsa/m4',
      items: [
        { name: 'Ford-Fulkerson', path: '/dsa/m4/ford-fulkerson' },
        { name: 'Edmonds-Karp', path: '/dsa/m4/edmonds-karp' },
        { name: 'Dinic Algorithm', path: '/dsa/m4/dinic' },
        { name: 'Bipartite Matching', path: '/dsa/m4/bipartite-matching' },
        { name: 'König Theorem', path: '/dsa/m4/konig' },
        { name: 'Max-Flow Min-Cut', path: '/dsa/m4/max-flow-min-cut' },
      ],
    },
    {
      id: 'm5',
      name: 'M5 · Complexity & Approximation',
      path: '/dsa/m5/sat',
      matchPrefix: '/dsa/m5',
      items: [
        { name: 'SAT Solver', path: '/dsa/m5/sat' },
        { name: '3-SAT Solver', path: '/dsa/m5/3sat' },
        { name: '3-SAT → CLIQUE', path: '/dsa/m5/3sat-to-clique' },
        { name: 'CLIQUE → IS', path: '/dsa/m5/clique-to-independent-set' },
        { name: 'IS → Vertex Cover', path: '/dsa/m5/independent-set-to-vertex-cover' },
        { name: 'VC 2-Approx', path: '/dsa/m5/vertex-cover-2approx' },
      ],
    },
  ];

  // Auto expand active module based on location
  const [expandedModules, setExpandedModules] = useState(() => {
    const active = dsaModules.find((m) => location.pathname.startsWith(m.matchPrefix));
    return active ? { [active.id]: true } : {};
  });

  const toggleExpand = (id) => {
    setExpandedModules((prev) => ({ ...prev, [id]: !prev[id] }));
  };

  return (
    <>
      {/* Mobile Backdrop Overlay */}
      {isOpen && (
        <div
          onClick={onClose}
          className="fixed inset-0 bg-[#0B1220]/80 backdrop-blur-sm z-40 md:hidden transition-opacity"
        />
      )}

      <aside
        className={`fixed md:static inset-y-0 left-0 z-50 w-64 bg-[#111827] border-r border-[#263449] flex flex-col shrink-0 transform transition-transform duration-200 ease-in-out ${
          isOpen ? 'translate-x-0' : '-translate-x-full md:translate-x-0'
        }`}
      >
        {/* Brand Header */}
        <div className="h-16 flex items-center justify-between px-5 border-b border-[#263449]">
          <div className="flex items-center gap-2.5">
            <div className="h-8 w-8 rounded bg-teal-500/10 border border-teal-500/20 flex items-center justify-center text-teal-400">
              <Train className="h-4 w-4" />
            </div>
            <div>
              <h1 className="font-bold text-base tracking-tight text-[#F1F5F9]">
                RailSync
              </h1>
              <p className="text-[10px] font-mono text-[#94A3B8]">
                Railway Operations Workbench
              </p>
            </div>
          </div>
          <button
            onClick={onClose}
            aria-label="Close Mobile Navigation"
            className="md:hidden p-1 rounded bg-[#172033] text-[#94A3B8] hover:text-[#F1F5F9]"
          >
            <X className="h-5 w-5" />
          </button>
        </div>

        {/* Navigation */}
        <div className="flex-1 overflow-y-auto px-3 py-5 space-y-5">
          {/* Group 1: Overview */}
          <div className="space-y-1">
            <h2 className="px-3 text-[10px] font-mono uppercase tracking-wider text-[#94A3B8] font-semibold">
              Overview
            </h2>
            <NavLink
              to="/dashboard"
              onClick={onClose}
              className={({ isActive }) =>
                `flex items-center gap-2.5 px-3 py-2 text-xs font-medium rounded-lg transition-colors ${
                  isActive
                    ? 'bg-teal-500/10 text-teal-400 font-semibold border border-teal-500/20'
                    : 'text-[#94A3B8] hover:text-[#F1F5F9] hover:bg-[#172033]'
                }`
              }
            >
              <LayoutDashboard className="h-4 w-4" />
              <span>Dashboard</span>
            </NavLink>
          </div>

          {/* Group 2: Railway Operations */}
          <div className="space-y-1">
            <h2 className="px-3 text-[10px] font-mono uppercase tracking-wider text-[#94A3B8] font-semibold">
              Railway Operations
            </h2>
            <NavLink
              to="/operations/stations"
              onClick={onClose}
              className={({ isActive }) =>
                `flex items-center gap-2.5 px-3 py-2 text-xs font-medium rounded-lg transition-colors ${
                  isActive
                    ? 'bg-teal-500/10 text-teal-400 font-semibold border border-teal-500/20'
                    : 'text-[#94A3B8] hover:text-[#F1F5F9] hover:bg-[#172033]'
                }`
              }
            >
              <Building2 className="h-4 w-4" />
              <span>Stations</span>
            </NavLink>
            <NavLink
              to="/operations/network"
              onClick={onClose}
              className={({ isActive }) =>
                `flex items-center gap-2.5 px-3 py-2 text-xs font-medium rounded-lg transition-colors ${
                  isActive
                    ? 'bg-teal-500/10 text-teal-400 font-semibold border border-teal-500/20'
                    : 'text-[#94A3B8] hover:text-[#F1F5F9] hover:bg-[#172033]'
                }`
              }
            >
              <Network className="h-4 w-4" />
              <span>Network</span>
            </NavLink>
            <NavLink
              to="/operations/documents"
              onClick={onClose}
              className={({ isActive }) =>
                `flex items-center gap-2.5 px-3 py-2 text-xs font-medium rounded-lg transition-colors ${
                  isActive
                    ? 'bg-teal-500/10 text-teal-400 font-semibold border border-teal-500/20'
                    : 'text-[#94A3B8] hover:text-[#F1F5F9] hover:bg-[#172033]'
                }`
              }
            >
              <Database className="h-4 w-4" />
              <span>Documents</span>
            </NavLink>
          </div>

          {/* Group 3: DSA Workbench */}
          <div className="space-y-1">
            <h2 className="px-3 text-[10px] font-mono uppercase tracking-wider text-[#94A3B8] font-semibold">
              DSA Workbench
            </h2>
            {dsaModules.map((mod) => {
              const isModuleActive = location.pathname.startsWith(mod.matchPrefix);
              const isExpanded = !!expandedModules[mod.id] || isModuleActive;

              return (
                <div key={mod.id} className="space-y-0.5">
                  <div className="flex items-center justify-between">
                    <NavLink
                      to={mod.path}
                      onClick={onClose}
                      className={
                        `flex-1 flex items-center justify-between px-3 py-2 text-xs font-medium rounded-lg transition-colors ${
                          isModuleActive
                            ? 'bg-teal-500/10 text-teal-400 font-semibold border border-teal-500/20'
                            : 'text-[#94A3B8] hover:text-[#F1F5F9] hover:bg-[#172033]'
                        }`
                      }
                    >
                      <span>{mod.name}</span>
                    </NavLink>
                    <button
                      onClick={() => toggleExpand(mod.id)}
                      className="p-1.5 text-[#94A3B8] hover:text-[#F1F5F9] rounded"
                      aria-label={`Toggle ${mod.name}`}
                    >
                      {isExpanded ? (
                        <ChevronDown className="h-3.5 w-3.5" />
                      ) : (
                        <ChevronRight className="h-3.5 w-3.5" />
                      )}
                    </button>
                  </div>

                  {/* Submenu Algorithms */}
                  {isExpanded && (
                    <div className="pl-4 pr-1 py-1 space-y-0.5 border-l border-[#263449] ml-3">
                      {mod.items.map((sub) => (
                        <NavLink
                          key={sub.name}
                          to={sub.path}
                          onClick={onClose}
                          className={({ isActive }) =>
                            `block px-2.5 py-1.5 text-[11px] font-mono rounded transition-colors ${
                              isActive
                                ? 'text-teal-300 font-semibold bg-[#172033]'
                                : 'text-[#94A3B8] hover:text-[#F1F5F9] hover:bg-[#172033]/60'
                            }`
                          }
                        >
                          {sub.name}
                        </NavLink>
                      ))}
                    </div>
                  )}
                </div>
              );
            })}
          </div>

          {/* Group 4: System */}
          <div className="space-y-1">
            <h2 className="px-3 text-[10px] font-mono uppercase tracking-wider text-[#94A3B8] font-semibold">
              System
            </h2>
            <NavLink
              to="/settings"
              onClick={onClose}
              className={({ isActive }) =>
                `flex items-center gap-2.5 px-3 py-2 text-xs font-medium rounded-lg transition-colors ${
                  isActive
                    ? 'bg-teal-500/10 text-teal-400 font-semibold border border-teal-500/20'
                    : 'text-[#94A3B8] hover:text-[#F1F5F9] hover:bg-[#172033]'
                }`
              }
            >
              <Settings className="h-4 w-4" />
              <span>Settings</span>
            </NavLink>
          </div>
        </div>
      </aside>
    </>
  );
};

