package com.subh.TestNest.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class University {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long universityId;
    private String universityName;

    @OneToMany(mappedBy = "university", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Exam> exams = new ArrayList<>();
}
