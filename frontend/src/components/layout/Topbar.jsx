import React from 'react';
import { Server, Database, Menu, Train } from 'lucide-react';

export const Topbar = ({ onToggleSidebar }) => {
  return (
    <header className="h-16 bg-[#111827] border-b border-[#263449] px-4 sm:px-6 flex items-center justify-between shrink-0">
      <div className="flex items-center gap-3">
        <button
          onClick={onToggleSidebar}
          aria-label="Toggle Navigation Sidebar"
          className="md:hidden p-1.5 rounded-lg bg-[#172033] hover:bg-slate-800 text-[#94A3B8] border border-[#263449] transition"
        >
          <Menu className="h-5 w-5" />
        </button>
        <div className="flex items-center gap-2 text-xs text-[#94A3B8]">
          <Train className="h-4 w-4 text-teal-400" />
          <span className="font-semibold text-[#F1F5F9] hidden sm:inline">Railway Operations Workbench</span>
          <span className="font-semibold text-[#F1F5F9] sm:hidden">RailSync</span>
        </div>
      </div>

      <div className="flex items-center gap-3 text-xs">
        <div className="hidden lg:flex items-center gap-2 text-[#94A3B8] bg-[#172033] px-3 py-1.5 rounded-lg border border-[#263449]">
          <Database className="h-3.5 w-3.5 text-[#94A3B8]" />
          <span>PostgreSQL:</span>
          <span className="flex items-center gap-1 text-emerald-400 font-mono text-[11px]">
            <span className="h-1.5 w-1.5 rounded-full bg-emerald-500"></span> Connected
          </span>
        </div>

        <div className="flex items-center gap-2 text-[#94A3B8] bg-[#172033] px-2.5 sm:px-3 py-1.5 rounded-lg border border-[#263449]">
          <Server className="h-3.5 w-3.5 text-[#94A3B8]" />
          <span className="hidden sm:inline">Spring Boot:</span>
          <span className="flex items-center gap-1 text-emerald-400 font-mono text-[11px]">
            <span className="h-1.5 w-1.5 rounded-full bg-emerald-500"></span> Port 8080
          </span>
        </div>
      </div>
    </header>
  );
};

