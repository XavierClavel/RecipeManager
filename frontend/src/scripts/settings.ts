import apiClient from '@/plugins/axios.js';

export {
  getSettings,
  updateSettings,
}

async function getSettings() {
  return await apiClient.get(`/user/settings`)
}

async function updateSettings(settings) {
  return await apiClient.put(`/user/settings`, settings)
}

