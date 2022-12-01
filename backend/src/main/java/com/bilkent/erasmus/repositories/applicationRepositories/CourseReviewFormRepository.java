package com.bilkent.erasmus.repositories.applicationRepositories;

import com.bilkent.erasmus.models.applicationModels.courseReviewForms.CourseReviewForm;
import com.bilkent.erasmus.models.applicationModels.courseReviewForms.CourseReviewFormErasmus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseReviewFormRepository extends JpaRepository<CourseReviewForm, Integer> {

}
