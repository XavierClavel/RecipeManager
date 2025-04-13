import apiClient from '@/plugins/axios.js';

export{
  getReport,
}

async function getReport() {
  return await apiClient.get(`/dashboard`)
}
