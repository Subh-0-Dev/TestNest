package com.subh.TestNest.Service;

import com.subh.TestNest.Dto.ExamDto.CreateExamRequest;
import com.subh.TestNest.Dto.ExamDto.ExamResponseDto;
import com.subh.TestNest.Dto.QuestionDto.LeaderboardEntryDto;
import com.subh.TestNest.Entity.*;
import com.subh.TestNest.Repository.ExamRepository;
import com.subh.TestNest.Repository.StudentExamResultRepository;
import com.subh.TestNest.Repository.UniversityRepository;
import com.subh.TestNest.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExamService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UniversityRepository universityRepository;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private StudentExamResultRepository studentExamResultRepository;

    /**
     * Create an exam (FACULTY only)
     */
    public ExamResponseDto createExam(CreateExamRequest request, String email) {
        User faculty = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (faculty.getRole() != Role.FACULTY) {
            throw new AccessDeniedException("Only faculty can create exams");
        }

        Exam exam = new Exam();
        exam.setTitle(request.getTitle());
        exam.setSubject(request.getSubject());
        exam.setStartTime(request.getStartTime());
        exam.setEndTime(request.getEndTime());
        exam.setDurationInMinutes((int) Duration.between(request.getStartTime(), request.getEndTime()).toMinutes());
        exam.setUniversity(faculty.getUniversity());
        exam.setCreatedBy(faculty);

        Exam savedExam = examRepository.save(exam);

        LocalDateTime now = LocalDateTime.now();
        String status;

        if (now.isBefore(exam.getStartTime())) {
            status = "UPCOMING";
        } else if (now.isAfter(exam.getEndTime())) {
            status = "ENDED";
        } else {
            status = "LIVE";
        }

        return new ExamResponseDto(
                savedExam.getId(),
                savedExam.getTitle(),
                savedExam.getSubject(),
                savedExam.getStartTime(),
                savedExam.getEndTime(),
                savedExam.getDurationInMinutes(),
                faculty.getFullName(),
                faculty.getUniversity().getUniversityName(),
                status
        );
    }


    /**
     * Get all exams created by the logged-in faculty
     */
    public List<ExamResponseDto> getExamsByFaculty(String email) {
        List<Exam> exams = examRepository.findByCreatedByEmail(email);
        return exams.stream().map(this::mapToDtoWithStatus).collect(Collectors.toList());
    }

    /**
     * Get all exams in the faculty's university
     */
    public List<ExamResponseDto> getExamsByUniversityOfFaculty(String facultyEmail) {
        User faculty = userRepository.findByEmail(facultyEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        University university = faculty.getUniversity();
        List<Exam> exams = examRepository.findByUniversity(university);
        return exams.stream().map(this::mapToDtoWithStatus).collect(Collectors.toList());
    }

    /**
     * Get all exams by university name (admin use)
     */
    public List<ExamResponseDto> getExamsByUniversity(String universityName) {
        University university = universityRepository.findByUniversityNameIgnoreCase(universityName)
                .orElseThrow(() -> new IllegalArgumentException("University not found"));

        List<Exam> exams = examRepository.findByUniversity(university);
        return exams.stream().map(this::mapToDtoWithStatus).collect(Collectors.toList());
    }

    /**
     * Convert Exam to DTO with status ("UPCOMING" or "ENDED")
     */
    private ExamResponseDto mapToDtoWithStatus(Exam exam) {
        LocalDateTime now = LocalDateTime.now();
        String status;

        if (now.isBefore(exam.getStartTime())) {
            status = "UPCOMING";
        } else if (now.isAfter(exam.getEndTime())) {
            status = "ENDED";
        } else {
            status = "LIVE";
        }

        return new ExamResponseDto(
                exam.getId(),
                exam.getTitle(),
                exam.getSubject(),
                exam.getStartTime(),
                exam.getEndTime(),
                exam.getDurationInMinutes(),
                exam.getCreatedBy().getFullName(),
                exam.getUniversity().getUniversityName(),
                status
        );
    }

    public List<ExamResponseDto> getLiveExamsForStudent(String studentEmail) {
        User student = userRepository.findByEmail(studentEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));


        University university = student.getUniversity();
        LocalDateTime now = LocalDateTime.now();

        List<Exam> exams = examRepository.findByUniversityAndStartTimeBeforeAndEndTimeAfter(university, now, now);


        return exams.stream().map(exam -> new ExamResponseDto(
                exam.getId(),
                exam.getTitle(),
                exam.getSubject(),
                exam.getStartTime(),
                exam.getEndTime(),
                exam.getDurationInMinutes(),
                exam.getCreatedBy().getFullName(),
                university.getUniversityName(),
                "LIVE"
        )).collect(Collectors.toList());
    }

    public List<LeaderboardEntryDto> getLeaderboardForExam(Long examId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new IllegalArgumentException("Exam not found"));

        List<StudentExamResult> submissions = studentExamResultRepository.findByExamId(examId);

        // Sort by score descending
        submissions.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));

        return submissions.stream()
                .map(sub -> new LeaderboardEntryDto(
                        sub.getUser().getFullName(),
                        sub.getUser().getUniversity().getUniversityName(),
                        sub.getScore()
                ))
                .collect(Collectors.toList());
    }

}

