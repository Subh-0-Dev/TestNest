package com.subh.TestNest.Controller;

import com.subh.TestNest.Dto.UniversityRequestDto;
import com.subh.TestNest.Entity.University;
import com.subh.TestNest.Service.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UniversityService universityService;


    @GetMapping("/hello")
    public ResponseEntity<?> getTest(){
        return new ResponseEntity<>("Hello Admin", HttpStatus.ACCEPTED);
    }

    @PostMapping("/university/add")
    public ResponseEntity<?> addUniversity(@RequestBody List<UniversityRequestDto> request){
        try{
            List<University> university = universityService.addUniversity(request);
            return ResponseEntity.ok(university);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/university/all")
    public ResponseEntity<?> universityList(){
        return ResponseEntity.ok(universityService.allUniversity());
    }
}
