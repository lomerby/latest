package com.tutorlog.controller;

import com.tutorlog.model.Lesson;
import com.tutorlog.model.User;
import com.tutorlog.service.LessonService;
import com.tutorlog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private LessonService lessonService;

    @Autowired
    private UserService userService;

    @PostMapping("/lessons/create")
    public ResponseEntity<?> createLesson(@RequestBody Lesson lessonRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            Lesson lesson = lessonService.createLesson(
                lessonRequest.getTitle(),
                lessonRequest.getDescription(),
                lessonRequest.getSubject(),
                lessonRequest.getYoutubeLink(),
                lessonRequest.getPdfLinks(),
                lessonRequest.getRequiredSubscription(),
                lessonRequest.getDuration()
            );
            lesson.setDifficulty(lessonRequest.getDifficulty());
            lesson = lessonService.updateLesson(lesson.getId(), lesson);

            response.put("success", true);
            response.put("message", "Lesson created successfully");
            response.put("lesson", lesson);
            return ResponseEntity.ok(response);
        } catch(Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/lessons/{id}")
    public ResponseEntity<?> updateLesson(@PathVariable String id, @RequestBody Lesson lessonDetails) {
        Map<String, Object> response = new HashMap<>();
        try {
            Lesson lesson = lessonService.updateLesson(id, lessonDetails);
            response.put("success", true);
            response.put("message", "Lesson updated successfully");
            response.put("lesson", lesson);
            return ResponseEntity.ok(response);
        } catch(Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/lessons/{id}/delete")
    public ResponseEntity<?> deleteLesson(@PathVariable String id) {
        Map<String, Object> response = new HashMap<>();
        try {
            lessonService.deleteLesson(id);
            response.put("success", true);
            response.put("message", "Lesson deleted successfully");
            return ResponseEntity.ok(response);
        } catch(Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("totalUsers", userService.getTotalUsers());
        stats.put("totalLessons", lessonService.getTotalLessons());
        stats.put("premiumUsers", userService.getPremiumUsers());
        return ResponseEntity.ok(stats);
    }

    @PutMapping("/users/{userId}/subscription/{subscriptionLevel}")
    public ResponseEntity<?> upgradeSubscription(@PathVariable String userId, @PathVariable String subscriptionLevel) {
        Map<String, Object> response = new HashMap<>();
        try {
            User user = userService.upgradeSubscription(userId, subscriptionLevel);
            response.put("success", true);
            response.put("message", "Subscription updated successfully");
            response.put("user", user);
            return ResponseEntity.ok(response);
        } catch(Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
