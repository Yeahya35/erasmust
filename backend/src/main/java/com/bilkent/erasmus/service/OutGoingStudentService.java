package com.bilkent.erasmus.service;

import com.bilkent.erasmus.models.applicationModels.UserModels.StudentModels.OutGoingStudentErasmus;
import com.bilkent.erasmus.repository.studentRepository.OutGoingStudentErasmusRepository;
import org.springframework.stereotype.Service;

@Service
public class OutGoingStudentService {

    private final OutGoingStudentErasmusRepository outGoingStudentErasmusRepository;

    public OutGoingStudentService(OutGoingStudentErasmusRepository outGoingStudentErasmusRepository) {
        this.outGoingStudentErasmusRepository = outGoingStudentErasmusRepository;
    }

    public OutGoingStudentErasmus saveErasmusStudent(OutGoingStudentErasmus studentErasmus) {
        return (outGoingStudentErasmusRepository.save(studentErasmus));
    }
}