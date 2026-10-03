import React, { useState } from 'react';
import {
  Play,
  RotateCcw,
  Cpu,
  Clock,
  Layers,
  HelpCircle,
  CheckCircle2,
  AlertCircle,
  BarChart2,
  FileText,
  Sliders,
  ChevronRight,
} from 'lucide-react';

export const AlgorithmPageLayout = ({
  moduleTag = 'DSA MODULE',
  title,
  description,
  presets = [],
  inputForm,
  onRun,
  onReset,
  loading = false,
  error = null,
  result = null,
  complexity = null,
  metrics = null,
  trace = [],
  railwayNote = null,
  resultView = null,
}) => {
  const [activeTab, setActiveTab] = useState('result'); // 'result' | 'metrics' | 'trace' | 'domain'

  const actualTrace = trace || result?.trace || [];
  const actualComplexity = complexity || result?.complexity;
  const actualTimeNanos = metrics?.executionTimeNanos ?? result?.executionTimeNanos;
  const actualOpCount = metrics?.operationCount ?? result?.operationCount;

  return (
    <div className="max-w-7xl mx-auto space-y-6 pb-12">
      {/* Top Header Bar */}
      <div className="bg-slate-900/90 border border-slate-800/80 rounded-xl p-6 shadow-sm">
        <div className="flex flex-col lg:flex-row lg:items-center lg:justify-between gap-4">
          <div className="space-y-1.5">
            <div className="inline-flex items-center gap-2 px-2.5 py-0.5 rounded bg-slate-800 border border-slate-700 text-cyan-400 text-xs font-mono font-medium">
              <Sliders className="h-3 w-3" />
              <span>{moduleTag}</span>
            </div>
            <h1 className="text-2xl font-bold text-slate-100 tracking-tight">{title}</h1>
            {description && <p className="text-slate-400 text-xs leading-relaxed max-w-3xl">{description}</p>}
          </div>

          {/* Quick Presets */}
          {presets.length > 0 && (
            <div className="flex flex-wrap items-center gap-2 bg-slate-950/60 p-2 rounded-lg border border-slate-800/60 self-start lg:self-center">
              <span className="text-[11px] font-mono text-slate-500 uppercase px-1">Presets:</span>
              {presets.map((preset, idx) => (
                <button
                  key={idx}
                  onClick={preset.onClick}
                  className="px-2.5 py-1 text-xs font-medium rounded bg-slate-800/80 text-slate-300 hover:bg-slate-700 hover:text-white border border-slate-700/60 transition-all"
                >
                  {preset.label}
                </button>
              ))}
            </div>
          )}
        </div>
      </div>

      {/* Main Split Workbench (2 Column Layout) */}
      <div className="grid grid-cols-1 lg:grid-cols-12 gap-6 items-start">
        {/* Left Column: Input & Controls (5 Cols on LG) */}
        <div className="lg:col-span-5 bg-slate-900/90 border border-slate-800/80 rounded-xl p-5 space-y-5 shadow-sm">
          <div className="flex items-center justify-between border-b border-slate-800/80 pb-3">
            <h2 className="text-xs font-mono font-bold uppercase tracking-wider text-slate-300 flex items-center gap-2">
              <Sliders className="h-4 w-4 text-cyan-400" />
              <span>Input Parameters</span>
            </h2>
            {onReset && (
              <button
                onClick={onReset}
                className="text-xs font-mono text-slate-400 hover:text-slate-200 flex items-center gap-1 transition-colors"
                title="Reset to default inputs"
              >
                <RotateCcw className="h-3 w-3" />
                <span>Reset</span>
              </button>
            )}
          </div>

          {/* Form Inputs Container */}
          <div className="space-y-4">{inputForm}</div>

          {/* Error Banner */}
          {error && (
            <div className="bg-rose-500/10 border border-rose-500/30 rounded-lg p-3 flex items-start gap-2.5 text-rose-400 text-xs font-mono">
              <AlertCircle className="h-4 w-4 shrink-0 mt-0.5" />
              <div className="space-y-0.5">
                <span className="font-bold block">Execution Error</span>
                <p className="text-rose-300/90 leading-normal">{error}</p>
              </div>
            </div>
          )}

          {/* Primary Action Controls */}
          <div className="pt-2">
            <button
              onClick={onRun}
              disabled={loading}
              className="w-full py-2.5 px-4 rounded-lg bg-cyan-600 hover:bg-cyan-500 disabled:opacity-50 text-white font-medium text-xs tracking-wide flex items-center justify-center gap-2 transition-all shadow-md shadow-cyan-900/20 active:scale-[0.99]"
            >
              {loading ? (
                <>
                  <div className="h-3.5 w-3.5 border-2 border-white/30 border-t-white rounded-full animate-spin" />
                  <span>Executing Algorithm...</span>
                </>
              ) : (
                <>
                  <Play className="h-4 w-4 fill-current" />
                  <span>Execute Algorithm</span>
                </>
              )}
            </button>
          </div>
        </div>

        {/* Right Column: Execution Workbench & Results (7 Cols on LG) */}
        <div className="lg:col-span-7 space-y-4">
          {/* Output Workbench Container */}
          <div className="bg-slate-900/90 border border-slate-800/80 rounded-xl overflow-hidden shadow-sm min-h-[480px] flex flex-col">
            {/* Tab Navigation Bar */}
            <div className="bg-slate-950/80 border-b border-slate-800/80 px-4 pt-2 flex items-center gap-1 overflow-x-auto">
              <button
                onClick={() => setActiveTab('result')}
                className={`px-4 py-2.5 text-xs font-medium border-b-2 flex items-center gap-2 transition-all whitespace-nowrap ${
                  activeTab === 'result'
                    ? 'border-cyan-400 text-cyan-300 bg-slate-900/60'
                    : 'border-transparent text-slate-400 hover:text-slate-200 hover:bg-slate-900/30'
                }`}
              >
                <CheckCircle2 className="h-3.5 w-3.5" />
                <span>Result & Visuals</span>
                {result && <span className="h-1.5 w-1.5 rounded-full bg-cyan-400" />}
              </button>

              <button
                onClick={() => setActiveTab('metrics')}
                className={`px-4 py-2.5 text-xs font-medium border-b-2 flex items-center gap-2 transition-all whitespace-nowrap ${
                  activeTab === 'metrics'
                    ? 'border-cyan-400 text-cyan-300 bg-slate-900/60'
                    : 'border-transparent text-slate-400 hover:text-slate-200 hover:bg-slate-900/30'
                }`}
              >
                <BarChart2 className="h-3.5 w-3.5" />
                <span>Complexity & Metrics</span>
                {actualTimeNanos !== undefined && actualTimeNanos !== null && (
                  <span className="text-[10px] font-mono px-1.5 py-0.2 rounded bg-slate-800 text-slate-300">
                    {(actualTimeNanos / 1000000).toFixed(2)}ms
                  </span>
                )}
              </button>

              <button
                onClick={() => setActiveTab('trace')}
                className={`px-4 py-2.5 text-xs font-medium border-b-2 flex items-center gap-2 transition-all whitespace-nowrap ${
                  activeTab === 'trace'
                    ? 'border-cyan-400 text-cyan-300 bg-slate-900/60'
                    : 'border-transparent text-slate-400 hover:text-slate-200 hover:bg-slate-900/30'
                }`}
              >
                <Layers className="h-3.5 w-3.5" />
                <span>Execution Trace</span>
                {actualTrace.length > 0 && (
                  <span className="text-[10px] font-mono px-1.5 py-0.2 rounded bg-slate-800 text-slate-400">
                    {actualTrace.length} steps
                  </span>
                )}
              </button>

              {railwayNote && (
                <button
                  onClick={() => setActiveTab('domain')}
                  className={`px-4 py-2.5 text-xs font-medium border-b-2 flex items-center gap-2 transition-all whitespace-nowrap ${
                    activeTab === 'domain'
                      ? 'border-cyan-400 text-cyan-300 bg-slate-900/60'
                      : 'border-transparent text-slate-400 hover:text-slate-200 hover:bg-slate-900/30'
                  }`}
                >
                  <HelpCircle className="h-3.5 w-3.5" />
                  <span>Railway Context</span>
                </button>
              )}
            </div>

            {/* Tab Body View */}
            <div className="p-5 flex-1 overflow-y-auto">
              {/* TAB 1: RESULT & VISUALIZATION */}
              {activeTab === 'result' && (
                <div className="space-y-4">
                  {resultView ? (
                    resultView
                  ) : result ? (
                    <div className="bg-slate-950 border border-slate-800/80 rounded-lg p-4 font-mono text-xs text-slate-300 overflow-x-auto">
                      <pre>{JSON.stringify(result, null, 2)}</pre>
                    </div>
                  ) : (
                    <div className="h-64 flex flex-col items-center justify-center text-slate-500 text-xs space-y-2 border border-dashed border-slate-800 rounded-lg bg-slate-950/40">
                      <Sliders className="h-8 w-8 text-slate-700" />
                      <p>Run the algorithm to view the computed results and visualizations.</p>
                    </div>
                  )}
                </div>
              )}

              {/* TAB 2: COMPLEXITY & METRICS */}
              {activeTab === 'metrics' && (
                <div className="space-y-6">
                  {/* Theoretical Complexity */}
                  <div className="space-y-3">
                    <h3 className="text-xs font-mono uppercase tracking-wider text-slate-400 font-bold flex items-center gap-2">
                      <Cpu className="h-4 w-4 text-cyan-400" />
                      <span>Theoretical Complexity</span>
                    </h3>
                    <div className="grid grid-cols-2 gap-4 text-xs font-mono">
                      <div className="bg-slate-950 p-4 rounded-lg border border-slate-800/80">
                        <span className="text-slate-500 block text-[10px] mb-1">TIME COMPLEXITY</span>
                        <span className="text-cyan-400 text-base font-bold">
                          {actualComplexity?.timeComplexity || actualComplexity?.time || 'N/A'}
                        </span>
                      </div>
                      <div className="bg-slate-950 p-4 rounded-lg border border-slate-800/80">
                        <span className="text-slate-500 block text-[10px] mb-1">SPACE COMPLEXITY</span>
                        <span className="text-cyan-400 text-base font-bold">
                          {actualComplexity?.spaceComplexity || actualComplexity?.space || 'N/A'}
                        </span>
                      </div>
                    </div>
                  </div>

                  {/* Measured Benchmark */}
                  <div className="space-y-3">
                    <h3 className="text-xs font-mono uppercase tracking-wider text-slate-400 font-bold flex items-center gap-2">
                      <Clock className="h-4 w-4 text-emerald-400" />
                      <span>Measured Benchmark Metrics</span>
                    </h3>
                    <div className="grid grid-cols-2 gap-4 text-xs font-mono">
                      <div className="bg-slate-950 p-4 rounded-lg border border-slate-800/80">
                        <span className="text-slate-500 block text-[10px] mb-1">CPU RUNTIME</span>
                        <span className="text-emerald-400 text-base font-bold">
                          {actualTimeNanos !== undefined && actualTimeNanos !== null
                            ? `${(actualTimeNanos / 1000000).toFixed(3)} ms`
                            : 'Not executed yet'}
                        </span>
                      </div>
                      <div className="bg-slate-950 p-4 rounded-lg border border-slate-800/80">
                        <span className="text-slate-500 block text-[10px] mb-1">OPERATION COUNT</span>
                        <span className="text-cyan-400 text-base font-bold">
                          {actualOpCount !== undefined && actualOpCount !== null ? `${actualOpCount} ops` : 'Not executed yet'}
                        </span>
                      </div>
                    </div>
                  </div>
                </div>
              )}

              {/* TAB 3: TRACE EXECUTION */}
              {activeTab === 'trace' && (
                <div className="space-y-4">
                  {actualTrace.length > 0 ? (
                    <div className="border border-slate-800/80 rounded-lg overflow-hidden bg-slate-950 max-h-[420px] overflow-y-auto">
                      <table className="w-full text-left text-xs font-mono border-collapse">
                        <thead className="bg-slate-900 text-slate-400 sticky top-0 border-b border-slate-800">
                          <tr>
                            <th className="py-2.5 px-3 w-16">Step</th>
                            <th className="py-2.5 px-3 w-36">Action</th>
                            <th className="py-2.5 px-3">Description</th>
                          </tr>
                        </thead>
                        <tbody className="divide-y divide-slate-800/50 text-slate-300">
                          {actualTrace.map((step) => (
                            <tr key={step.step} className="hover:bg-slate-900/50">
                              <td className="py-2 px-3 text-slate-500">{step.step}</td>
                              <td className="py-2 px-3">
                                <span className="px-2 py-0.5 rounded bg-cyan-500/10 text-cyan-400 border border-cyan-500/20 text-[11px]">
                                  {step.action}
                                </span>
                              </td>
                              <td className="py-2 px-3 text-slate-300 leading-snug">{step.description}</td>
                            </tr>
                          ))}
                        </tbody>
                      </table>
                    </div>
                  ) : (
                    <div className="h-48 flex items-center justify-center text-slate-500 text-xs border border-dashed border-slate-800 rounded-lg bg-slate-950/40">
                      No trace data available. Make sure "Enable Step-by-Step Trace" is checked before execution.
                    </div>
                  )}
                </div>
              )}

              {/* TAB 4: RAILWAY CONTEXT */}
              {activeTab === 'domain' && railwayNote && (
                <div className="bg-slate-950 border border-slate-800/80 rounded-lg p-5 space-y-3">
                  <h3 className="text-xs font-bold text-cyan-400 font-mono uppercase tracking-wider flex items-center gap-2">
                    <HelpCircle className="h-4 w-4" />
                    <span>Railway Operational Mapping</span>
                  </h3>
                  <p className="text-xs text-slate-300 leading-relaxed font-sans">{railwayNote}</p>
                </div>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
