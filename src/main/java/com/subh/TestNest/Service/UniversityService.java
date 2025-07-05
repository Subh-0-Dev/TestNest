package com.subh.TestNest.Service;

import com.subh.TestNest.Dto.UniversityRequestDto;
import com.subh.TestNest.Entity.University;
import com.subh.TestNest.Repository.UniversityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UniversityService {

    @Autowired
    private UniversityRepository universityRepository;

    public List<University> addUniversity(List<UniversityRequestDto> requestDto) {
        List<University> universityList=new ArrayList<>();
        for(UniversityRequestDto request:requestDto){
            if (request.getUniversityName() == null || request.getUniversityName().isBlank()) {
                throw new IllegalArgumentException("University name cannot be empty");
            }

            if (universityRepository.existsByUniversityNameIgnoreCase(request.getUniversityName())) {
                throw new RuntimeException("University already exists");
            }
            University university=new University();
            university.setUniversityName(request.getUniversityName());

            University save = universityRepository.save(university);
            universityList.add(save);
        }
        return universityList;
    }

    public List<University> allUniversity() {
        return universityRepository.findAll();
    }
}
