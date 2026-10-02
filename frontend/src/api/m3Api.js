import { apiClient } from './client';

export const runLevenshtein = async ({ source, target, traceEnabled = true, maxTraceSteps = 500 }) => {
  return apiClient.post('/m3/levenshtein', { source, target, traceEnabled, maxTraceSteps });
};

export const runDamerau = async ({ source, target, traceEnabled = true, maxTraceSteps = 500 }) => {
  return apiClient.post('/m3/damerau', { source, target, traceEnabled, maxTraceSteps });
};

export const runBitmask = async ({ nodeCount, costMatrix, startNode = 0, traceEnabled = true, maxTraceSteps = 500 }) => {
  return apiClient.post('/m3/bitmask', { nodeCount, costMatrix, startNode, traceEnabled, maxTraceSteps });
};

export const runMatrixChain = async ({ dimensions, traceEnabled = true, maxTraceSteps = 500 }) => {
  return apiClient.post('/m3/matrix-chain', { dimensions, traceEnabled, maxTraceSteps });
};

export const runOptimalBST = async ({ keys, frequencies, traceEnabled = true, maxTraceSteps = 500 }) => {
  return apiClient.post('/m3/optimal-bst', { keys, frequencies, traceEnabled, maxTraceSteps });
};
