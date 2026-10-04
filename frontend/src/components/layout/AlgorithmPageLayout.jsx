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
  Sliders,
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
      <div className="bg-[#111827] border border-[#263449] rounded-xl p-5 sm:p-6 shadow-sm">
        <div className="flex flex-col lg:flex-row lg:items-center lg:justify-between gap-4">
          <div className="space-y-1.5">
            <div className="inline-flex items-center gap-2 px-2.5 py-0.5 rounded bg-[#172033] border border-[#263449] text-teal-400 text-xs font-mono font-medium">
              <Sliders className="h-3 w-3" />
              <span>{moduleTag}</span>
            </div>
            <h1 className="text-2xl font-bold text-[#F1F5F9] tracking-tight">{title}</h1>
            {description && <p className="text-[#94A3B8] text-xs leading-relaxed max-w-3xl">{description}</p>}
          </div>

          {/* Quick Presets */}
          {presets.length > 0 && (
            <div className="flex flex-wrap items-center gap-2 bg-[#0B1220] p-2 rounded-lg border border-[#263449] self-start lg:self-center">
              <span className="text-[11px] font-mono text-[#94A3B8] uppercase px-1">Presets:</span>
              {presets.map((preset, idx) => (
                <button
                  key={idx}
                  onClick={preset.onClick}
                  className="px-2.5 py-1 text-xs font-medium rounded bg-[#172033] text-[#F1F5F9] hover:bg-slate-800 border border-[#263449] transition-all"
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
        <div className="lg:col-span-5 bg-[#111827] border border-[#263449] rounded-xl p-5 space-y-5 shadow-sm">
          <div className="flex items-center justify-between border-b border-[#263449] pb-3">
            <h2 className="text-xs font-mono font-bold uppercase tracking-wider text-[#F1F5F9] flex items-center gap-2">
              <Sliders className="h-4 w-4 text-teal-400" />
              <span>Input Parameters</span>
            </h2>
            {onReset && (
              <button
                onClick={onReset}
                className="text-xs font-mono text-[#94A3B8] hover:text-[#F1F5F9] flex items-center gap-1 transition-colors"
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
            <div className="bg-red-500/10 border border-red-500/30 rounded-lg p-3 flex items-start gap-2.5 text-red-400 text-xs font-mono">
              <AlertCircle className="h-4 w-4 shrink-0 mt-0.5" />
              <div className="space-y-0.5">
                <span className="font-bold block">Execution Error</span>
                <p className="text-red-300/90 leading-normal">{error}</p>
              </div>
            </div>
          )}

          {/* Primary Action Controls */}
          <div className="pt-2">
            <button
              onClick={onRun}
              disabled={loading}
              className="w-full py-2.5 px-4 rounded-lg bg-teal-600 hover:bg-teal-500 disabled:opacity-50 text-white font-semibold text-xs tracking-wide flex items-center justify-center gap-2 transition-all shadow-md active:scale-[0.99]"
            >
              {loading ? (
                <>
                  <div className="h-3.5 w-3.5 border-2 border-white/30 border-t-white rounded-full animate-spin" />
                  <span>Executing Algorithm...</span>
                </>
              ) : (
                <>
                  <Play className="h-4 w-4 fill-current" />
                  <span>Run Algorithm</span>
                </>
              )}
            </button>
          </div>
        </div>

        {/* Right Column: Execution Workbench & Results (7 Cols on LG) */}
        <div className="lg:col-span-7 space-y-4">
          {/* Output Workbench Container */}
          <div className="bg-[#111827] border border-[#263449] rounded-xl overflow-hidden shadow-sm min-h-[480px] flex flex-col">
            {/* Tab Navigation Bar */}
            <div className="bg-[#0B1220] border-b border-[#263449] px-4 pt-2 flex items-center gap-1 overflow-x-auto scrollbar-thin">
              <button
                onClick={() => setActiveTab('result')}
                className={`px-4 py-2.5 text-xs font-medium border-b-2 flex items-center gap-2 transition-all whitespace-nowrap ${
                  activeTab === 'result'
                    ? 'border-teal-400 text-teal-300 bg-[#172033]'
                    : 'border-transparent text-[#94A3B8] hover:text-[#F1F5F9] hover:bg-[#172033]/40'
                }`}
              >
                <CheckCircle2 className="h-3.5 w-3.5" />
                <span>Result & Visuals</span>
                {result && <span className="h-1.5 w-1.5 rounded-full bg-teal-400" />}
              </button>

              <button
                onClick={() => setActiveTab('metrics')}
                className={`px-4 py-2.5 text-xs font-medium border-b-2 flex items-center gap-2 transition-all whitespace-nowrap ${
                  activeTab === 'metrics'
                    ? 'border-teal-400 text-teal-300 bg-[#172033]'
                    : 'border-transparent text-[#94A3B8] hover:text-[#F1F5F9] hover:bg-[#172033]/40'
                }`}
              >
                <BarChart2 className="h-3.5 w-3.5" />
                <span>Complexity & Metrics</span>
                {actualTimeNanos !== undefined && actualTimeNanos !== null && (
                  <span className="text-[10px] font-mono px-1.5 py-0.2 rounded bg-[#172033] text-[#F1F5F9] border border-[#263449]">
                    {(actualTimeNanos / 1000000).toFixed(2)}ms
                  </span>
                )}
              </button>

              <button
                onClick={() => setActiveTab('trace')}
                className={`px-4 py-2.5 text-xs font-medium border-b-2 flex items-center gap-2 transition-all whitespace-nowrap ${
                  activeTab === 'trace'
                    ? 'border-teal-400 text-teal-300 bg-[#172033]'
                    : 'border-transparent text-[#94A3B8] hover:text-[#F1F5F9] hover:bg-[#172033]/40'
                }`}
              >
                <Layers className="h-3.5 w-3.5" />
                <span>Execution Trace</span>
                {actualTrace.length > 0 && (
                  <span className="text-[10px] font-mono px-1.5 py-0.2 rounded bg-[#172033] text-[#94A3B8] border border-[#263449]">
                    {actualTrace.length} steps
                  </span>
                )}
              </button>

              {railwayNote && (
                <button
                  onClick={() => setActiveTab('domain')}
                  className={`px-4 py-2.5 text-xs font-medium border-b-2 flex items-center gap-2 transition-all whitespace-nowrap ${
                    activeTab === 'domain'
                      ? 'border-teal-400 text-teal-300 bg-[#172033]'
                      : 'border-transparent text-[#94A3B8] hover:text-[#F1F5F9] hover:bg-[#172033]/40'
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
                    <div className="bg-[#0B1220] border border-[#263449] rounded-lg p-4 font-mono text-xs text-[#F1F5F9] overflow-x-auto">
                      <pre>{JSON.stringify(result, null, 2)}</pre>
                    </div>
                  ) : (
                    <div className="h-64 flex flex-col items-center justify-center text-[#94A3B8] text-xs space-y-2 border border-dashed border-[#263449] rounded-lg bg-[#0B1220]/40">
                      <Sliders className="h-8 w-8 text-[#263449]" />
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
                    <h3 className="text-xs font-mono uppercase tracking-wider text-[#94A3B8] font-bold flex items-center gap-2">
                      <Cpu className="h-4 w-4 text-teal-400" />
                      <span>Theoretical Complexity</span>
                    </h3>
                    <div className="grid grid-cols-2 gap-4 text-xs font-mono">
                      <div className="bg-[#0B1220] p-4 rounded-lg border border-[#263449]">
                        <span className="text-[#94A3B8] block text-[10px] mb-1">TIME COMPLEXITY</span>
                        <span className="text-teal-400 text-base font-bold">
                          {actualComplexity?.timeComplexity || actualComplexity?.time || 'N/A'}
                        </span>
                      </div>
                      <div className="bg-[#0B1220] p-4 rounded-lg border border-[#263449]">
                        <span className="text-[#94A3B8] block text-[10px] mb-1">SPACE COMPLEXITY</span>
                        <span className="text-teal-400 text-base font-bold">
                          {actualComplexity?.spaceComplexity || actualComplexity?.space || 'N/A'}
                        </span>
                      </div>
                    </div>
                  </div>

                  {/* Measured Benchmark */}
                  <div className="space-y-3">
                    <h3 className="text-xs font-mono uppercase tracking-wider text-[#94A3B8] font-bold flex items-center gap-2">
                      <Clock className="h-4 w-4 text-emerald-400" />
                      <span>Measured Benchmark Metrics</span>
                    </h3>
                    <div className="grid grid-cols-2 gap-4 text-xs font-mono">
                      <div className="bg-[#0B1220] p-4 rounded-lg border border-[#263449]">
                        <span className="text-[#94A3B8] block text-[10px] mb-1">CPU RUNTIME</span>
                        <span className="text-emerald-400 text-base font-bold">
                          {actualTimeNanos !== undefined && actualTimeNanos !== null
                            ? `${(actualTimeNanos / 1000000).toFixed(3)} ms`
                            : 'Not executed yet'}
                        </span>
                      </div>
                      <div className="bg-[#0B1220] p-4 rounded-lg border border-[#263449]">
                        <span className="text-[#94A3B8] block text-[10px] mb-1">OPERATION COUNT</span>
                        <span className="text-teal-400 text-base font-bold">
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
                    <div className="border border-[#263449] rounded-lg overflow-hidden bg-[#0B1220] max-h-[420px] overflow-y-auto">
                      <table className="w-full text-left text-xs font-mono border-collapse">
                        <thead className="bg-[#172033] text-[#94A3B8] sticky top-0 border-b border-[#263449]">
                          <tr>
                            <th className="py-2.5 px-3 w-16">Step</th>
                            <th className="py-2.5 px-3 w-36">Action</th>
                            <th className="py-2.5 px-3">Description</th>
                          </tr>
                        </thead>
                        <tbody className="divide-y divide-[#263449] text-[#F1F5F9]">
                          {actualTrace.map((step) => (
                            <tr key={step.step} className="hover:bg-[#172033]/50">
                              <td className="py-2 px-3 text-[#94A3B8]">{step.step}</td>
                              <td className="py-2 px-3">
                                <span className="px-2 py-0.5 rounded bg-teal-500/10 text-teal-400 border border-teal-500/20 text-[11px]">
                                  {step.action}
                                </span>
                              </td>
                              <td className="py-2 px-3 text-[#F1F5F9] leading-snug">{step.description}</td>
                            </tr>
                          ))}
                        </tbody>
                      </table>
                    </div>
                  ) : (
                    <div className="h-48 flex items-center justify-center text-[#94A3B8] text-xs border border-dashed border-[#263449] rounded-lg bg-[#0B1220]/40">
                      No trace data available. Make sure "Enable Step-by-Step Trace" is checked before execution.
                    </div>
                  )}
                </div>
              )}

              {/* TAB 4: RAILWAY CONTEXT */}
              {activeTab === 'domain' && railwayNote && (
                <div className="bg-[#0B1220] border border-[#263449] rounded-lg p-5 space-y-3">
                  <h3 className="text-xs font-bold text-teal-400 font-mono uppercase tracking-wider flex items-center gap-2">
                    <HelpCircle className="h-4 w-4" />
                    <span>Railway Operational Mapping</span>
                  </h3>
                  <p className="text-xs text-[#F1F5F9] leading-relaxed font-sans">{railwayNote}</p>
                </div>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

