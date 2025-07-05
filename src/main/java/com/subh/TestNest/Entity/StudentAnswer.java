package com.subh.TestNest.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Who submitted the answer
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Which exam it belongs to
    @ManyToOne
    @JoinColumn(name = "exam_id")
    private Exam exam;

    // The question being answered
    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    private String selectedOption; // "A", "B", "C", "D"

    private boolean correct;
}

