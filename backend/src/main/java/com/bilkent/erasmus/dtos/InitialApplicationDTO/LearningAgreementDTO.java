package com.bilkent.erasmus.dtos.InitialApplicationDTO;

import com.bilkent.erasmus.embeddables.BilkentInformation;
import com.bilkent.erasmus.embeddables.ReceivingInstitutionInformation;
import com.bilkent.erasmus.models.enums.MobilityType;
import com.bilkent.erasmus.models.enums.SemesterOfferings;
import com.bilkent.erasmus.models.enums.Status;
import lombok.Data;

@Data
public class LearningAgreementDTO {


    private int id;

    private String studentId;

    private String academicYear;

    private String studyCycle;

    private String subjectArea;

    private SemesterOfferings semester;

    private BilkentInformation bilkentInformation;

    private ReceivingInstitutionInformation receivingInstitutionInformation;

    private Status status;

    private MobilityType currentMobility;

}