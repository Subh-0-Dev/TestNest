package com.subh.TestNest.Controller;

import com.subh.TestNest.Dto.ExamDto.CreateExamRequest;
import com.subh.TestNest.Dto.ExamDto.ExamResponseDto;
import com.subh.TestNest.Dto.QuestionDto.QuestionRequestDto;
import com.subh.TestNest.Entity.Exam;
import com.subh.TestNest.Entity.Question;
import com.subh.TestNest.Service.ExamService;
import com.subh.TestNest.Service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/faculty")
public class FacultyController {

    @Autowired
    private ExamService examService;

    @Autowired
    private QuestionService questionService;

    // ✅ Create an exam
    @PostMapping("/exam/create")
    public ResponseEntity<ExamResponseDto> createExam(@RequestBody CreateExamRequest request, Principal principal) {
        ExamResponseDto exam = examService.createExam(request, principal.getName());
        return ResponseEntity.ok(exam);
    }

    // ✅ Get all exams created by the logged-in faculty
    @GetMapping("exam/my")
    public ResponseEntity<List<ExamResponseDto>> getMyExams(Principal principal) {
        return ResponseEntity.ok(examService.getExamsByFaculty(principal.getName()));
    }

    // ✅ Optional: Get all exams in your university (if needed)
    @GetMapping("/university")
    public ResponseEntity<List<ExamResponseDto>> getExamsByUniversity(Principal principal) {
        List<ExamResponseDto> exams = examService.getExamsByUniversityOfFaculty(principal.getName());
        return ResponseEntity.ok(exams);
    }


    /**
     * Add a single question to an exam.
     */
    @PostMapping("exam/question/add/{examId}")
    public ResponseEntity<Question> addQuestion(
            @PathVariable Long examId,
            @RequestBody QuestionRequestDto questionDto) {

        Question question = questionService.addQuestionToExam(examId, questionDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(question);
    }

    /**
     * Add multiple questions to an exam.
     */
    @PostMapping("exam/question/add-multiple/{examId}")
    public ResponseEntity<List<Question>> addMultipleQuestions(
            @PathVariable Long examId,
            @RequestBody List<QuestionRequestDto> questionDtos) {

        List<Question> savedQuestions = questionService.addMultipleQuestions(examId, questionDtos);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedQuestions);
    }

    /**
     * Get all questions for an exam.
     */
    @GetMapping("/exam/question/{examId}")
    public ResponseEntity<List<Question>> getQuestionsByExam(@PathVariable Long examId) {
        List<Question> questions = questionService.getQuestionsByExam(examId);
        return ResponseEntity.ok(questions);
    }
}
