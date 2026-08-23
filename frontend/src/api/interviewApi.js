import axios from '../utils/axios';

export const interviewApi = {
  createInterview: async (interviewData) => {
    const response = await axios.post('/interviews', interviewData);
    return response.data;
  },

  getAllInterviews: async () => {
    const response = await axios.get('/interviews');
    return response.data;
  },

  getInterviewById: async (id) => {
    const response = await axios.get(`/interviews/${id}`);
    return response.data;
  },

  deleteInterview: async (id) => {
    const response = await axios.delete(`/interviews/${id}`);
    return response.data;
  },

  updateInterviewStatus: async (id, status) => {
    const response = await axios.put(`/interviews/${id}/status`, null, {
      params: { status },
    });
    return response.data;
  },
};
