import { apiClient } from './client';

export const runSuffixArray = async ({ text, traceEnabled = true, maxTraceSteps = 500 }) => {
  return apiClient.post('/m2/suffix-array', { text, traceEnabled, maxTraceSteps });
};

export const runSAIS = async ({ text, traceEnabled = true, maxTraceSteps = 500 }) => {
  return apiClient.post('/m2/sa-is', { text, traceEnabled, maxTraceSteps });
};

export const runKasai = async ({ text, suffixArray, traceEnabled = true, maxTraceSteps = 500 }) => {
  return apiClient.post('/m2/kasai', { text, suffixArray, traceEnabled, maxTraceSteps });
};

export const runLCP = async ({ text, suffixArray, traceEnabled = true, maxTraceSteps = 500 }) => {
  return apiClient.post('/m2/lcp', { text, suffixArray, traceEnabled, maxTraceSteps });
};

export const runSuffixAutomaton = async ({ text, query, traceEnabled = true, benchmarkEnabled = true, maxTraceSteps = 500 }) => {
  return apiClient.post('/m2/suffix-automaton', { text, query, traceEnabled, benchmarkEnabled, maxTraceSteps });
};

export const fetchDocuments = async () => {
  return apiClient.get('/documents');
};

export const searchDocuments = async (query) => {
  return apiClient.post('/documents/search', { query });
};

export const analyzeDocumentRepeatedSubstrings = async () => {
  return apiClient.post('/documents/repeated-substrings', {});
};
