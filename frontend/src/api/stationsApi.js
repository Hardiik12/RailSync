import { apiClient } from './client';

export const fetchStations = async ({ page = 0, size = 10, search = '', sortBy = 'stationCode', sortDir = 'ASC' } = {}) => {
  const params = new URLSearchParams();
  params.append('page', page);
  params.append('size', size);
  if (search) params.append('search', search);
  params.append('sortBy', sortBy);
  params.append('sortDir', sortDir);

  return apiClient.get(`/stations?${params.toString()}`);
};

export const fetchStationByCode = async (code) => {
  return apiClient.get(`/stations/${code}`);
};
