package com.workintech.spring17challenge.controller;

import com.workintech.spring17challenge.entity.*;
import com.workintech.spring17challenge.exceptions.ApiException;
import com.workintech.spring17challenge.exceptions.ApiResponse;
import com.workintech.spring17challenge.validation.CourseValidation;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/courses")
public class CourseController {
    private List<Course> courses;
    private final CourseGpa lowCourseGpa;
    private final CourseGpa mediumCourseGpa;
    private final CourseGpa highCourseGpa;

    @Autowired
    public CourseController(@Qualifier("lowCourseGpa") CourseGpa lowCourseGpa,
                            @Qualifier("mediumCourseGpa") CourseGpa mediumCourseGpa,
                            @Qualifier("highCourseGpa") CourseGpa highCourseGpa){
        this.lowCourseGpa =lowCourseGpa;
        this.mediumCourseGpa = mediumCourseGpa;
        this.highCourseGpa = highCourseGpa;
    }

    @PostConstruct
    public void init(){
        this.courses = new ArrayList<>();
    }

    @GetMapping
    public List<Course> findAll(){
        return courses;
    }

    @GetMapping("/{name}")
    public Course findCourse(@PathVariable String name){
        CourseValidation.check(name);
        return courses.stream()
                .filter(c -> c.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new ApiException("There is no such a course with name: " + name, HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> addCourse(@RequestBody Course course){
        CourseValidation.checkCredit(course.getCredit());
        CourseValidation.check(course.getName());
        courses.add(course);
        Integer totalGpa = getTotalGpa(course);
        ApiResponse apiResponse = new ApiResponse(course, totalGpa);
        return  new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    private Integer getTotalGpa(Course course) {
        Integer totalGpa = 0;
        if(course.getCredit() <= 2){
            totalGpa = course.getGrade().getCoefficient() * course.getCredit() * lowCourseGpa.getGpa();
        } else if(course.getCredit() == 3){
            totalGpa = course.getGrade().getCoefficient() * course.getCredit() * mediumCourseGpa.getGpa();
        } else {
            totalGpa = course.getGrade().getCoefficient() * course.getCredit() * highCourseGpa.getGpa();
        }
        return totalGpa;
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable Integer id, @RequestBody Course course){
        CourseValidation.checkCredit(course.getCredit());
        CourseValidation.check(course.getName());
        CourseValidation.checkId(id);
        Course existingC = courses.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ApiException("There is no such a course with id: " + id, HttpStatus.BAD_REQUEST));
        int indexOfExistingC = courses.indexOf(existingC);
        courses.set(indexOfExistingC, course);
        Integer totalGpa = getTotalGpa(course);
        ApiResponse apiResponse = new ApiResponse(course, totalGpa);
        return  new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    //[DELETE]/workintech/courses/{id} => İlgili id değerindeki course objesini listeden siler.
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id) {
        Course existingC = getExistingCById(id);
        courses.remove(existingC);
    }

    private Course getExistingCById(Integer id) {
        return courses.stream()
                .filter(cStream -> cStream.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ApiException("There is no such a course with id: " + id, HttpStatus.NOT_FOUND));
    }
}
