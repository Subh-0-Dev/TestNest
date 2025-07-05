package com.subh.TestNest.Controller;

import com.subh.TestNest.Dto.QuestionDto.LeaderboardEntryDto;
import com.subh.TestNest.Service.ExamService;
import com.subh.TestNest.Service.StudentExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
public class GeneralController {
    @Autowired
    private ExamService examService;

    @GetMapping("/register")
    public String register() {
        return "login"; // templates/my-exams.html
    }
    @GetMapping("/login")
    public String login() {
        return "login"; // templates/my-exams.html
    }

    @GetMapping("/exams/{examId}/leaderboard")
    public ResponseEntity<List<LeaderboardEntryDto>> getLeaderboard(@PathVariable Long examId) {
        return ResponseEntity.ok(examService.getLeaderboardForExam(examId));
    }
}
