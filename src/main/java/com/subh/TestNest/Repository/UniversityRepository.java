package com.subh.TestNest.Repository;

import com.subh.TestNest.Entity.University;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UniversityRepository extends JpaRepository<University, Long> {

    boolean existsByUniversityNameIgnoreCase(String universityName);

    Optional<University> findByUniversityName(String universityName);

    Optional<University> findByUniversityNameIgnoreCase(String universityName);

}
