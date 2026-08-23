import axios from '../utils/axios';

export const authApi = {
  register: async (userData) => {
    const response = await axios.post('/auth/register', userData);
    return response.data;
  },

  login: async (credentials) => {
    const response = await axios.post('/auth/login', credentials);
    return response.data;
  },
};
