package com.example.project_with_database.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@CrossOrigin(origins = "*")
//@CrossOrigin(origins = "http://localhost:4200")
public class AppController {
	@GetMapping("/hello")
    public String hello() {
        return "hello world";
    }

    @GetMapping("/bye")
    public String bye() {
        return "good bye";
    }

    @GetMapping("/square")
    public int square(@RequestParam int num) {
        return num * num;
    }

    @GetMapping("/personalInfo")
    public String personalInfo(@RequestParam String me, @RequestParam String father) {
        String output = "hello, i'm " + me + ". father's name: " + father;
        return output;
    }

    @GetMapping("/info")
    public String info(@RequestParam(name = "aa") String myName,
                       @RequestParam(required = false, name = "father_name") String fatherName,
                       @RequestParam(value = "bb") int sonAge
                       //@RequestParam(name = "bb") int sonAge
                       ) {
        if (fatherName != null) {
            return "I am: " + myName + " and my father's name is: " + fatherName + ", my age: " + sonAge;
        } else {
            return "I am: " + myName + " and my age: " + sonAge;
        }
    }
}
