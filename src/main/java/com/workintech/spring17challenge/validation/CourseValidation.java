package com.workintech.spring17challenge.validation;

import com.workintech.spring17challenge.exceptions.ApiException ;
import org.springframework.http.HttpStatus;

public class CourseValidation {

    public static void check(String name) {
        if(name == null || name.isEmpty()){
            throw new ApiException ("Name can't be empty!", HttpStatus.BAD_REQUEST);
        }
    }

    public static void checkCredit(Integer credit) {
        if(credit == null || credit < 0 || credit > 4){
            throw new ApiException ("Invalid credit value!", HttpStatus.BAD_REQUEST);
        }
    }

    public static void checkId(Integer id) {
        if (id == null || id < 0) {
            throw new ApiException ("Id can't be null or less than zero! ", HttpStatus.BAD_REQUEST);
        }

    }
}
