package com.subh.TestNest.Repository;

import com.subh.TestNest.Entity.Exam;
import com.subh.TestNest.Entity.University;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
    List<Exam> findByCreatedByEmail(String email);
    List<Exam> findByUniversity(University university);

    List<Exam> findByUniversityAndStartTimeBeforeAndEndTimeAfter(University university, LocalDateTime now, LocalDateTime now1);

    List<Exam> findByUniversityAndStartTimeAfter(University university, LocalDateTime now);
}
