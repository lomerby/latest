package com.tutorlog.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "lessons")
public class Lesson {
    @Id
    private String id;
    private String title;
    private String description;
    private String subject;
    private String youtubeLink;
    private List<String> pdfLinks;
    private String requiredSubscription;
    private int duration;
    private String difficulty;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
    private Boolean isPublished = true;

    public Lesson() {
    }

    public Lesson(String id, String title, String description, String subject, String youtubeLink,
                  List<String> pdfLinks, String requiredSubscription, int duration, String difficulty,
                  LocalDateTime createdAt, LocalDateTime updatedAt, Boolean isPublished) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.subject = subject;
        this.youtubeLink = youtubeLink;
        this.pdfLinks = pdfLinks;
        this.requiredSubscription = requiredSubscription;
        this.duration = duration;
        this.difficulty = difficulty;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isPublished = isPublished;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getYoutubeLink() { return youtubeLink; }
    public void setYoutubeLink(String youtubeLink) { this.youtubeLink = youtubeLink; }
    public List<String> getPdfLinks() { return pdfLinks; }
    public void setPdfLinks(List<String> pdfLinks) { this.pdfLinks = pdfLinks; }
    public String getRequiredSubscription() { return requiredSubscription; }
    public void setRequiredSubscription(String requiredSubscription) { this.requiredSubscription = requiredSubscription; }
    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public Boolean getIsPublished() { return isPublished; }
    public void setIsPublished(Boolean published) { isPublished = published; }
}