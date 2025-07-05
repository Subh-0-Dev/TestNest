package com.subh.TestNest.Controller;

import com.subh.TestNest.Dto.ExamDto.ExamResponseDto;
import com.subh.TestNest.Dto.QuestionDto.AnswerRequestDto;
import com.subh.TestNest.Dto.QuestionDto.QuestionViewDto;
import com.subh.TestNest.Dto.QuestionDto.SubmitExamResponse;
import com.subh.TestNest.Service.ExamParticipationService;
import com.subh.TestNest.Service.ExamService;
import com.subh.TestNest.Service.JWTUtils;
import com.subh.TestNest.Service.StudentExamService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private ExamService examService;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private ExamParticipationService participationService;

    @Autowired
    private StudentExamService studentExamService;



    @GetMapping("/hey")
    public ResponseEntity<?> getTest(){
        return new ResponseEntity<>("Hello Students", HttpStatus.ACCEPTED);
    }


    @GetMapping("/exams/live")
    public ResponseEntity<List<ExamResponseDto>> getLiveExamsForStudent(HttpServletRequest request) {
        String email = jwtUtils.extractUsername(request.getHeader("Authorization").substring(7));
        List<ExamResponseDto> liveExams = examService.getLiveExamsForStudent(email);
        return ResponseEntity.ok(liveExams);
    }




    @GetMapping("/exams/{examId}/start")
    public ResponseEntity<List<QuestionViewDto>> startExam(
            @PathVariable Long examId,
            Principal principal
    ) {
        String email = principal.getName();
        List<QuestionViewDto> questions = participationService.startExam(examId, email);
        return ResponseEntity.ok(questions);
    }



    /**
     * Submit answers for a live exam.
     * Only accessible by STUDENT role.
     */
    @PostMapping("/exams/{examId}/submit")
    public ResponseEntity<SubmitExamResponse> submitExam(
            @PathVariable Long examId,
            @RequestBody List<AnswerRequestDto> answers,
            @AuthenticationPrincipal(expression = "username") String email
    ) {
        SubmitExamResponse response = studentExamService.submitAnswers(examId, email, answers);
        return ResponseEntity.ok(response);
    }

}
