import {base_url} from "@/scripts/common";
import apiClient from '@/plugins/axios.js';

export {
  listCookbooks,
}

async function listCookbooks(search) {
  return await apiClient.get(`/cookbook${search}`)
}

