import { apiClient } from './client';

export const runKmpSearch = async ({ text, pattern, traceEnabled = true, maxTraceSteps = 500 }) => {
  return apiClient.post('/m1/kmp', {
    text,
    pattern,
    traceEnabled,
    maxTraceSteps,
  });
};
