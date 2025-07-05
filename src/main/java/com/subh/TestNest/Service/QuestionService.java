package com.subh.TestNest.Service;

import com.subh.TestNest.Dto.QuestionDto.QuestionRequestDto;
import com.subh.TestNest.Entity.Exam;
import com.subh.TestNest.Entity.Question;
import com.subh.TestNest.Repository.ExamRepository;
import com.subh.TestNest.Repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class QuestionService {

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private QuestionRepository questionRepository;

    /**
     * Add a single question to a specific exam.
     */
    public Question addQuestionToExam(Long examId, QuestionRequestDto dto) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new IllegalArgumentException("Exam not found"));

        validateCorrectAnswer(dto.getCorrectAnswer());

        Question question = new Question();
        question.setQuestionText(dto.getQuestionText());
        question.setOptionA(dto.getOptionA());
        question.setOptionB(dto.getOptionB());
        question.setOptionC(dto.getOptionC());
        question.setOptionD(dto.getOptionD());
        question.setCorrectAnswer(dto.getCorrectAnswer());
        question.setExam(exam);

        return questionRepository.save(question);
    }

    /**
     * Add multiple questions to an exam.
     */
    public List<Question> addMultipleQuestions(Long examId, List<QuestionRequestDto> questionDtos) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new IllegalArgumentException("Exam not found"));

        List<Question> questionList = new ArrayList<>();

        for (QuestionRequestDto dto : questionDtos) {
            validateCorrectAnswer(dto.getCorrectAnswer());

            Question q = new Question();
            q.setQuestionText(dto.getQuestionText());
            q.setOptionA(dto.getOptionA());
            q.setOptionB(dto.getOptionB());
            q.setOptionC(dto.getOptionC());
            q.setOptionD(dto.getOptionD());
            q.setCorrectAnswer(dto.getCorrectAnswer());
            q.setExam(exam);

            questionList.add(q);
        }

        return questionRepository.saveAll(questionList);
    }

    /**
     * Get all questions for an exam.
     */
    public List<Question> getQuestionsByExam(Long examId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new IllegalArgumentException("Exam not found"));

        return questionRepository.findByExam(exam);
    }

    /**
     * Optional: Validate that correctAnswer is one of A/B/C/D
     */
    private void validateCorrectAnswer(String answer) {
        if (!Set.of("A", "B", "C", "D").contains(answer.toUpperCase())) {
            throw new IllegalArgumentException("Correct answer must be one of A, B, C, or D");
        }
    }
}

