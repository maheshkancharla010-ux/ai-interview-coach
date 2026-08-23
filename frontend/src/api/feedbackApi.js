import axios from '../utils/axios';

export const feedbackApi = {
  generateFeedbackForQuestion: async (questionId) => {
    const response = await axios.post(`/questions/${questionId}/feedback/generate`);
    return response.data;
  },

  generateFeedbackForInterview: async (interviewId) => {
    const response = await axios.post(`/interviews/${interviewId}/feedback/generate`);
    return response.data;
  },

  getFeedbackByInterview: async (interviewId) => {
    const response = await axios.get(`/interviews/${interviewId}/feedback`);
    return response.data;
  },

  getFeedbackById: async (id) => {
    const response = await axios.get(`/feedback/${id}`);
    return response.data;
  },
};
