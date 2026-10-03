import client from './client';

export const runSat = async (payload) => {
  const response = await client.post('/api/m5/sat', payload);
  return response.data;
};

export const runThreeSat = async (payload) => {
  const response = await client.post('/api/m5/3sat', payload);
  return response.data;
};

export const runThreeSatToClique = async (payload) => {
  const response = await client.post('/api/m5/3sat-to-clique', payload);
  return response.data;
};

export const runCliqueToIndependentSet = async (payload) => {
  const response = await client.post('/api/m5/clique-to-independent-set', payload);
  return response.data;
};

export const runIndependentSetToVertexCover = async (payload) => {
  const response = await client.post('/api/m5/independent-set-to-vertex-cover', payload);
  return response.data;
};

export const runVertexCover2Approx = async (payload) => {
  const response = await client.post('/api/m5/vertex-cover-2approx', payload);
  return response.data;
};
