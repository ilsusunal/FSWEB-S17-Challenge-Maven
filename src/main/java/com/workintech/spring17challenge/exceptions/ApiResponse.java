package com.workintech.spring17challenge.exceptions;

import com.workintech.spring17challenge.entity.Course;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ApiResponse {
    private Course course;
    private Integer totalGpa;
}
