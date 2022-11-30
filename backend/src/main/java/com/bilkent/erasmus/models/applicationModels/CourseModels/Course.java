package com.bilkent.erasmus.models.applicationModels.CourseModels;

import com.bilkent.erasmus.models.AdministrativeModels.Department;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "course")
@Inheritance(strategy = InheritanceType.JOINED)
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private Double creditECTS;

    private Double creditBilkent;

    private Boolean isProjectCourse;

    @ManyToOne
    private Department underDepartment;

}