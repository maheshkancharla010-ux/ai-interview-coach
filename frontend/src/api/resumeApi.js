import axios from '../utils/axios';

export const resumeApi = {
  uploadResume: async (file) => {
    const formData = new FormData();
    formData.append('file', file);
    const response = await axios.post('/resumes', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
    return response.data;
  },

  getAllResumes: async () => {
    const response = await axios.get('/resumes');
    return response.data;
  },

  getResumeById: async (id) => {
    const response = await axios.get(`/resumes/${id}`);
    return response.data;
  },

  deleteResume: async (id) => {
    const response = await axios.delete(`/resumes/${id}`);
    return response.data;
  },

  downloadResume: async (id) => {
    const response = await axios.get(`/resumes/${id}/download`, {
      responseType: 'blob',
    });
    return response;
  },
};
