package com.bezkoder.spring.jpa.postgresql.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bezkoder.spring.jpa.postgresql.model.Tutorial;

/**
 * Interacts with Tutorials from the database.<p/>
 * We can use JpaRepository’s methods: {@code save()}, {@code findOne()}, {@code findById()}, {@code findAll()}, {@code count()}, {@code delete()}, {@code deleteById()}… without implementing these methods.<p/>
 * [N]:jpa - Extends {@code JpaRepository} where the 1st generic argument is the entity class and the 2nd generic argument is the type of its primary key (Tutorial.id)
 */
public interface TutorialRepository extends JpaRepository<Tutorial, Long> {
  /**
   * 
   * @param published
   * @return all Tutorials with published having value as input published.
   */
  List<Tutorial> findByPublished(boolean published);

  /**
   * 
   * @param title
   * @return all Tutorials which title contains input title.
   */
  List<Tutorial> findByTitleContaining(String title);
}
