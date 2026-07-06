package com.tutorlog.controller;

import com.tutorlog.model.Lesson;
import com.tutorlog.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/lessons")
@CrossOrigin(origins = "*")
public class LessonController {
    
    @Autowired
    private LessonService lessonService;
    
    @GetMapping("/all")
    public ResponseEntity<?> getAllLessons() {
        try {
            List<Lesson> lessons = lessonService.getAllLessons();
            return ResponseEntity.ok(lessons);
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping("/published")
    public ResponseEntity<?> getPublishedLessons() {
        try {
            List<Lesson> lessons = lessonService.getAllPublishedLessons();
            return ResponseEntity.ok(lessons);
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping("/by-subscription/{subscriptionLevel}")
    public ResponseEntity<?> getLessonsBySubscription(
            @PathVariable String subscriptionLevel) {
        try {
            List<Lesson> lessons = lessonService.getLessonsBySubscription(subscriptionLevel);
            return ResponseEntity.ok(lessons);
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping("/by-subject/{subject}")
    public ResponseEntity<?> getLessonsBySubject(@PathVariable String subject) {
        try {
            List<Lesson> lessons = lessonService.getLessonsBySubject(subject);
            return ResponseEntity.ok(lessons);
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getLessonById(@PathVariable String id) {
        try {
            Lesson lesson = lessonService.getLessonById(id);
            return ResponseEntity.ok(lesson);
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}