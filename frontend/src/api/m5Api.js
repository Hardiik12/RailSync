import { apiClient } from './client';

export const runSat = async (payload) => {
  return apiClient.post('/m5/sat', payload);
};

export const runThreeSat = async (payload) => {
  return apiClient.post('/m5/3sat', payload);
};

export const runThreeSatToClique = async (payload) => {
  return apiClient.post('/m5/3sat-to-clique', payload);
};

export const runCliqueToIndependentSet = async (payload) => {
  return apiClient.post('/m5/clique-to-independent-set', payload);
};

export const runIndependentSetToVertexCover = async (payload) => {
  return apiClient.post('/m5/independent-set-to-vertex-cover', payload);
};

export const runVertexCover2Approx = async (payload) => {
  return apiClient.post('/m5/vertex-cover-2approx', payload);
};
