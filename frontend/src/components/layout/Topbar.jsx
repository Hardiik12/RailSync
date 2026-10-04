import React from 'react';
import { Activity, Bell, Server, Database } from 'lucide-react';

export const Topbar = () => {
  return (
    <header className="h-16 bg-slate-900/60 backdrop-blur border-b border-slate-800/80 px-8 flex items-center justify-between shrink-0">
      <div className="flex items-center gap-3">
        <span className="px-2.5 py-1 rounded-full text-[11px] font-mono bg-cyan-500/10 text-cyan-400 border border-cyan-500/30 flex items-center gap-1.5">
          <Activity className="h-3 w-3" />
          Railway Operations Simulation
        </span>
      </div>

      <div className="flex items-center gap-4 text-xs">
        <div className="flex items-center gap-2 text-slate-400 bg-slate-800/40 px-3 py-1.5 rounded-lg border border-slate-800">
          <Database className="h-3.5 w-3.5 text-slate-400" />
          <span>PostgreSQL:</span>
          <span className="text-emerald-400 font-mono">CONNECTED</span>
        </div>

        <div className="flex items-center gap-2 text-slate-400 bg-slate-800/40 px-3 py-1.5 rounded-lg border border-slate-800">
          <Server className="h-3.5 w-3.5 text-slate-400" />
          <span>Spring Boot:</span>
          <span className="text-emerald-400 font-mono">PORT 8080</span>
        </div>

        <button className="h-8 w-8 rounded-lg bg-slate-800/60 hover:bg-slate-800 text-slate-300 flex items-center justify-center border border-slate-700/50 transition">
          <Bell className="h-4 w-4" />
        </button>
      </div>
    </header>
  );
};
