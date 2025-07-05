package com.subh.TestNest.Service;

import com.subh.TestNest.Dto.QuestionDto.QuestionViewDto;
import com.subh.TestNest.Entity.Exam;
import com.subh.TestNest.Entity.Question;
import com.subh.TestNest.Entity.Role;
import com.subh.TestNest.Entity.User;
import com.subh.TestNest.Repository.ExamRepository;
import com.subh.TestNest.Repository.QuestionRepository;
import com.subh.TestNest.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExamParticipationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private QuestionRepository questionRepository;

    /**
     * Start a live exam: Only available to STUDENT role & only if exam is live
     */
    public List<QuestionViewDto> startExam(Long examId, String studentEmail) {
        User student = userRepository.findByEmail(studentEmail)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        if (student.getRole() != Role.STUDENT) {
            throw new AccessDeniedException("Only students can start exams");
        }

        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new IllegalArgumentException("Exam not found"));

        if (!student.getUniversity().equals(exam.getUniversity())) {
            throw new AccessDeniedException("You are not allowed to access this exam");
        }

        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(exam.getStartTime())) {
            throw new IllegalStateException("Exam has not started yet");
        }
        if (now.isAfter(exam.getEndTime())) {
            throw new IllegalStateException("Exam has already ended");
        }

        // Fetch questions
        List<Question> questions = questionRepository.findByExamId(examId);

        return questions.stream()
                .map(q -> new QuestionViewDto(
                        q.getId(),
                        q.getQuestionText(),
                        q.getOptionA(),
                        q.getOptionB(),
                        q.getOptionC(),
                        q.getOptionD()
                ))
                .collect(Collectors.toList());
    }
}
