package com.subh.TestNest.Dto.QuestionDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmitExamResponse {
    private int totalQuestions;
    private int correctAnswers;
    private int score;
    private int attempted;
    private int skipped;
    private List<Long> skippedQuestionIds;
}
