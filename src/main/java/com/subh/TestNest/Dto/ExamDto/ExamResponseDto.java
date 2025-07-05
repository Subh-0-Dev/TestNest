package com.subh.TestNest.Dto.ExamDto;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamResponseDto {
    private Long id;
    private String title;
    private String subject;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer durationInMinutes;
    private String createdBy;
    private String universityName;
    private String status;
}
