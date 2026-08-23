import axios from '../utils/axios';

export const questionApi = {
  generateQuestions: async (interviewId) => {
    const response = await axios.post(`/interviews/${interviewId}/questions/generate`);
    return response.data;
  },

  getQuestionsByInterview: async (interviewId) => {
    const response = await axios.get(`/interviews/${interviewId}/questions`);
    return response.data;
  },

  getQuestionById: async (id) => {
    const response = await axios.get(`/questions/${id}`);
    return response.data;
  },

  submitAnswer: async (questionId, answerData) => {
    const response = await axios.put(`/questions/${questionId}/answer`, answerData);
    return response.data;
  },
};
