package com.subh.TestNest.Repository;

import com.subh.TestNest.Entity.Exam;
import com.subh.TestNest.Entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByExam(Exam exam);

    List<Question> findByExamId(Long examId);
}
