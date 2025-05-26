import apiClient from '@/plugins/axios.js';

export{
  getNotifications,
}

async function getNotifications() {
  return await apiClient.get(`/notification`)
}
