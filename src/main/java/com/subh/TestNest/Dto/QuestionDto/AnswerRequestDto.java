package com.subh.TestNest.Dto.QuestionDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AnswerRequestDto {
    private Long questionId;
    private String selectedOption;
}
