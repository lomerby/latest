package com.tutorlog.repository;

import com.tutorlog.model.UserProgress;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserProgressRepository extends MongoRepository<UserProgress, String> {
    Optional<UserProgress> findByUserIdAndLessonId(String userId, String lessonId);
    List<UserProgress> findByUserId(String userId);
}