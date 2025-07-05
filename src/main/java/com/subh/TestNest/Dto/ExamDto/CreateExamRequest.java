package com.subh.TestNest.Dto.ExamDto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateExamRequest {
    private String title;
    private String subject;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
