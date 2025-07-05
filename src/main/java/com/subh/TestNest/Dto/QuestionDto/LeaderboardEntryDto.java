package com.subh.TestNest.Dto.QuestionDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LeaderboardEntryDto {
    private String studentName;
    private String universityName;
    private int score;
}

