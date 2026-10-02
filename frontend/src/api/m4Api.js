import { apiClient } from './client';

export const runFordFulkerson = async (payload) => {
  return apiClient.post('/m4/ford-fulkerson', payload);
};

export const runEdmondsKarp = async (payload) => {
  return apiClient.post('/m4/edmonds-karp', payload);
};

export const runDinic = async (payload) => {
  return apiClient.post('/m4/dinic', payload);
};

export const runBipartiteMatching = async (payload) => {
  return apiClient.post('/m4/bipartite-matching', payload);
};

export const runKonig = async (payload) => {
  return apiClient.post('/m4/konig', payload);
};

export const runMaxFlowMinCut = async (payload) => {
  return apiClient.post('/m4/max-flow-min-cut', payload);
};
