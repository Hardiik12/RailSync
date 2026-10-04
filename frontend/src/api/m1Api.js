import { apiClient } from './client';

export const runKmpSearch = async ({ text, pattern, traceEnabled = true, maxTraceSteps = 500 }) => {
  return apiClient.post('/m1/kmp', {
    text,
    pattern,
    traceEnabled,
    maxTraceSteps,
  });
};

export const runZFunctionSearch = async ({ text, pattern, traceEnabled = true, maxTraceSteps = 500 }) => {
  return apiClient.post('/m1/z', {
    text,
    pattern,
    traceEnabled,
    maxTraceSteps,
  });
};

export const runRabinKarpSearch = async ({ text, pattern, primeModulus, traceEnabled = true, maxTraceSteps = 500 }) => {
  return apiClient.post('/m1/rabin-karp', {
    text,
    pattern,
    primeModulus,
    traceEnabled,
    maxTraceSteps,
  });
};

export const runAhoCorasickSearch = async ({ text, keywords, traceEnabled = true, benchmarkEnabled = true, maxTraceSteps = 500 }) => {
  return apiClient.post('/m1/aho-corasick', {
    text,
    keywords,
    traceEnabled,
    benchmarkEnabled,
    maxTraceSteps,
  });
};

export const searchRailwayStations = async ({ query, algorithm = 'KMP', dataOriginFilter = 'ALL', limit = 10, traceEnabled = false }) => {
  return apiClient.post('/m1/stations/search', {
    query,
    algorithm,
    dataOriginFilter,
    limit,
    traceEnabled,
  });
};

