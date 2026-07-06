package com.tutorlog.service;

import com.tutorlog.model.Lesson;
import com.tutorlog.repository.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LessonService {

    @Autowired
    private LessonRepository lessonRepository;

    public Lesson createLesson(String title, String description, String subject,
                              String youtubeLink, List<String> pdfLinks,
                              String requiredSubscription, int duration) {
        Lesson lesson = new Lesson();
        lesson.setTitle(title);
        lesson.setDescription(description);
        lesson.setSubject(subject);
        lesson.setYoutubeLink(youtubeLink);
        lesson.setPdfLinks(pdfLinks);
        lesson.setRequiredSubscription(requiredSubscription);
        lesson.setDuration(duration);
        lesson.setCreatedAt(LocalDateTime.now());
        lesson.setUpdatedAt(LocalDateTime.now());
        lesson.setIsPublished(true);

        return lessonRepository.save(lesson);
    }

    public Lesson getLessonById(String id) {
        return lessonRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Lesson not found"));
    }

    public List<Lesson> getLessonsBySubscription(String subscriptionLevel) {
        return lessonRepository.findByRequiredSubscription(subscriptionLevel);
    }

    public List<Lesson> getLessonsBySubject(String subject) {
        return lessonRepository.findBySubject(subject);
    }

    public List<Lesson> getAllPublishedLessons() {
        return lessonRepository.findByIsPublishedTrue();
    }

    public List<Lesson> getAllLessons() {
        return lessonRepository.findAll();
    }

    public Lesson updateLesson(String id, Lesson lessonDetails) {
        Lesson lesson = getLessonById(id);

        if(lessonDetails.getTitle() != null) lesson.setTitle(lessonDetails.getTitle());
        if(lessonDetails.getDescription() != null) lesson.setDescription(lessonDetails.getDescription());
        if(lessonDetails.getSubject() != null) lesson.setSubject(lessonDetails.getSubject());
        if(lessonDetails.getYoutubeLink() != null) lesson.setYoutubeLink(lessonDetails.getYoutubeLink());
        if(lessonDetails.getPdfLinks() != null) lesson.setPdfLinks(lessonDetails.getPdfLinks());
        if(lessonDetails.getRequiredSubscription() != null) lesson.setRequiredSubscription(lessonDetails.getRequiredSubscription());
        if(lessonDetails.getDuration() > 0) lesson.setDuration(lessonDetails.getDuration());
        if(lessonDetails.getDifficulty() != null) lesson.setDifficulty(lessonDetails.getDifficulty());
        if(lessonDetails.getIsPublished() != null) lesson.setIsPublished(lessonDetails.getIsPublished());

        lesson.setUpdatedAt(LocalDateTime.now());
        return lessonRepository.save(lesson);
    }

    public void deleteLesson(String id) {
        lessonRepository.deleteById(id);
    }

    public long getTotalLessons() {
        return lessonRepository.count();
    }
}
