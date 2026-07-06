package com.tutorlog.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "user_progress")
public class UserProgress {
    @Id
    private String id;
    private String userId;
    private String lessonId;
    private int progressPercentage = 0;
    private boolean completed = false;
    private LocalDateTime startedAt = LocalDateTime.now();
    private LocalDateTime completedAt;
    private LocalDateTime updatedAt = LocalDateTime.now();

    public UserProgress() {
    }

    public UserProgress(String id, String userId, String lessonId, int progressPercentage, boolean completed,
                        LocalDateTime startedAt, LocalDateTime completedAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.lessonId = lessonId;
        this.progressPercentage = progressPercentage;
        this.completed = completed;
        this.startedAt = startedAt;
        this.completedAt = completedAt;
        this.updatedAt = updatedAt;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getLessonId() { return lessonId; }
    public void setLessonId(String lessonId) { this.lessonId = lessonId; }
    public int getProgressPercentage() { return progressPercentage; }
    public void setProgressPercentage(int progressPercentage) { this.progressPercentage = progressPercentage; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}