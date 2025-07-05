package com.subh.TestNest.Service;

import com.subh.TestNest.Dto.QuestionDto.AnswerRequestDto;
import com.subh.TestNest.Dto.QuestionDto.SubmitExamResponse;
import com.subh.TestNest.Entity.*;
import com.subh.TestNest.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StudentExamService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private StudentAnswerRepository studentAnswerRepository;

    @Autowired
    private StudentExamResultRepository studentExamResultRepository;

    /**
     * Submits student answers and calculates score.
     */
    public SubmitExamResponse submitAnswers(Long examId, String studentEmail, List<AnswerRequestDto> answers) {
        User student = userRepository.findByEmail(studentEmail)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new IllegalArgumentException("Exam not found"));

        if (studentExamResultRepository.findByUserAndExam(student, exam).isPresent()) {
            throw new IllegalStateException("You have already submitted this exam.");
        }

        // Fetch all question IDs for this exam
        List<Long> allQuestionIds = questionRepository.findByExamId(examId)
                .stream().map(Question::getId).toList();

        // Track submitted question IDs
        Set<Long> submittedQuestionIds = answers.stream()
                .map(AnswerRequestDto::getQuestionId)
                .collect(Collectors.toSet());

        // Determine skipped question IDs
        List<Long> skippedQuestionIds = allQuestionIds.stream()
                .filter(id -> !submittedQuestionIds.contains(id))
                .toList();

        int correctAnswers = 0;

        for (AnswerRequestDto dto : answers) {
            Question question = questionRepository.findById(dto.getQuestionId())
                    .orElseThrow(() -> new IllegalArgumentException("Question not found: ID " + dto.getQuestionId()));

            if (!question.getExam().getId().equals(examId)) {
                throw new IllegalArgumentException("Question does not belong to the specified exam");
            }

            boolean isCorrect = question.getCorrectAnswer().equalsIgnoreCase(dto.getSelectedOption());
            if (isCorrect) correctAnswers++;

            StudentAnswer answer = new StudentAnswer();
            answer.setUser(student);
            answer.setExam(exam);
            answer.setQuestion(question);
            answer.setSelectedOption(dto.getSelectedOption());
            answer.setCorrect(isCorrect);

            studentAnswerRepository.save(answer);
        }

        // Save final result
        StudentExamResult result = new StudentExamResult();
        result.setUser(student);
        result.setExam(exam);
        result.setTotalQuestions(allQuestionIds.size());
        result.setCorrectAnswers(correctAnswers);
        result.setScore(correctAnswers);
        result.setSubmittedAt(LocalDateTime.now());
        studentExamResultRepository.save(result);

        // ✅ Build response
        SubmitExamResponse response = new SubmitExamResponse();
        response.setTotalQuestions(allQuestionIds.size());
        response.setCorrectAnswers(correctAnswers);
        response.setScore(correctAnswers);
        response.setAttempted(submittedQuestionIds.size());
        response.setSkipped(skippedQuestionIds.size());
        response.setSkippedQuestionIds(skippedQuestionIds);

        return response;
    }

}

