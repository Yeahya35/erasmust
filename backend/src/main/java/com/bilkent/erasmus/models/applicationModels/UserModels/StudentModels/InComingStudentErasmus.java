package com.bilkent.erasmus.models.applicationModels.UserModels.StudentModels;

import com.bilkent.erasmus.models.universityModels.PartnerUniversityErasmus;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "inComingStudentErasmus")
public class InComingStudentErasmus extends Student {

    @ManyToOne
    private PartnerUniversityErasmus partnerUniversity;

}
