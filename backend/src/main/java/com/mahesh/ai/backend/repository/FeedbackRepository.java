package com.mahesh.ai.backend.repository;

import com.mahesh.ai.backend.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByInterviewId(Long interviewId);
    Optional<Feedback> findByQuestionId(Long questionId);
}
