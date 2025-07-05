package com.subh.TestNest.Repository;

import com.subh.TestNest.Entity.Exam;
import com.subh.TestNest.Entity.StudentExamResult;
import com.subh.TestNest.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentExamResultRepository extends JpaRepository<StudentExamResult, Long> {
    List<StudentExamResult> findByUser(User user);
    Optional<StudentExamResult> findByUserAndExam(User user, Exam exam);

    List<StudentExamResult> findByExamId(Long examId);
}

