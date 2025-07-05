package com.subh.TestNest.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String subject;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private Integer durationInMinutes; // For timer feature

    @ManyToOne
    @JoinColumn(name = "university_id")
    @JsonIgnore
    private University university;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User createdBy; // Only FACULTY can be assigned

    @JsonIgnore
    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();


}
