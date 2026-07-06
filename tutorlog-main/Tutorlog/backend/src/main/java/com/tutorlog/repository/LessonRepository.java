package com.tutorlog.repository;

import com.tutorlog.model.Lesson;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LessonRepository extends MongoRepository<Lesson, String> {
    List<Lesson> findByRequiredSubscription(String requiredSubscription);
    List<Lesson> findBySubject(String subject);
    List<Lesson> findByIsPublishedTrue();
}
