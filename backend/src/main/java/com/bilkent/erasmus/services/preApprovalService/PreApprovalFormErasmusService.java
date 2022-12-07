package com.bilkent.erasmus.services.preApprovalService;

import com.bilkent.erasmus.dtos.CourseReviewFormFillRequest;
import com.bilkent.erasmus.dtos.InitialApplicationDTO.PreApprovalFormDTO;
import com.bilkent.erasmus.dtos.PreApprovalFormListDTO;
import com.bilkent.erasmus.models.enums.SemesterOfferings;
import com.bilkent.erasmus.models.enums.Status;
import com.bilkent.erasmus.models.applicationModels.InitialApplicationModels.PreApprovalFormErasmus;
import com.bilkent.erasmus.models.applicationModels.courseReviewForms.CourseReviewForm;
import com.bilkent.erasmus.models.compositeModels.PreApprovalFormErasmusDetail;
import com.bilkent.erasmus.models.courseModels.CourseHost;
import com.bilkent.erasmus.repositories.CoordinatorStudentErasmusRepository;
import com.bilkent.erasmus.repositories.PartnerUniversityErasmusRepository;
import com.bilkent.erasmus.repositories.applicationRepositories.PreApprovalFormErasmusDetailRepository;
import com.bilkent.erasmus.repositories.applicationRepositories.PreApprovalFormErasmusRepository;
import com.bilkent.erasmus.repositories.studentRepository.OutGoingStudentErasmusRepository;
import com.bilkent.erasmus.services.CourseHostService;
import com.bilkent.erasmus.services.CourseReviewFormService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.Predicate;

@Service
@Slf4j
public class PreApprovalFormErasmusService {

    private final PreApprovalFormErasmusRepository erasmusRepository;

    private final PreApprovalFormErasmusDetailRepository erasmusDetailRepository;

    private final PartnerUniversityErasmusRepository universityErasmusRepository;

    private final CourseReviewFormService courseReviewFormService;

    private final CourseHostService courseHostService;

    private final CoordinatorStudentErasmusRepository coordinatorStudentErasmusRepository;

    private final OutGoingStudentErasmusRepository outGoingStudentErasmusRepository;


    public PreApprovalFormErasmusService(PreApprovalFormErasmusRepository erasmusRepository, PreApprovalFormErasmusDetailRepository erasmusDetailRepository,
                                         PartnerUniversityErasmusRepository universityErasmusRepository, CourseReviewFormService courseReviewFormService, CourseHostService courseHostService, CoordinatorStudentErasmusRepository coordinatorStudentErasmusRepository, OutGoingStudentErasmusRepository outGoingStudentErasmusRepository) {
        this.erasmusRepository = erasmusRepository;
        this.erasmusDetailRepository = erasmusDetailRepository;
        this.universityErasmusRepository = universityErasmusRepository;
        this.courseReviewFormService = courseReviewFormService;
        this.courseHostService = courseHostService;
        this.coordinatorStudentErasmusRepository = coordinatorStudentErasmusRepository;
        this.outGoingStudentErasmusRepository = outGoingStudentErasmusRepository;
    }


    public PreApprovalFormErasmus saveForm(PreApprovalFormDTO form) throws Exception {

        PreApprovalFormErasmus erasmusForm = createEmptyPreApprovalForm(form.getAcademicYear(), form.getSemester());
        createMappingObject(erasmusForm, createCourseReviewForms(form.getCourseBilkentIds(), saveAllHostCourses(
                form.getCourseHostNames()
                ,form.getCourseHostDepartments()
                ,form.getCourseHostCredits()
        )), form.getStudentId());
        return erasmusForm;
    }

    private void createMappingObject(PreApprovalFormErasmus erasmusForm,
                                                             List<CourseReviewForm> reviewForms, int studentId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        int studentSecurityId = outGoingStudentErasmusRepository.findByStarsId(auth.getName()).getId();


        for (CourseReviewForm form : reviewForms) {
            PreApprovalFormErasmusDetail formErasmusDetail = new PreApprovalFormErasmusDetail();
            formErasmusDetail.setPreApprovalForm(erasmusForm);
            formErasmusDetail.setReviewForm(form);
            formErasmusDetail.setCoordinatorStudent(coordinatorStudentErasmusRepository.findByStudent_Id(studentSecurityId));
            erasmusDetailRepository.save(formErasmusDetail);
        }
    }

    private PreApprovalFormErasmus createEmptyPreApprovalForm(String academicYear, SemesterOfferings semester) {
        PreApprovalFormErasmus form = new PreApprovalFormErasmus();
        form.setStatus(Status.IN_PROCESS);
        form.setAcademicYear(academicYear);
        form.setSemester(semester);
        return erasmusRepository.save(form);
    }

    private List<CourseReviewForm> createCourseReviewForms(List<Integer> courseBilkentIds, List<CourseHost> coursesHost) throws Exception {
        List<CourseReviewForm> forms = new ArrayList<>();
        for (int i = 0; i < coursesHost.size(); i++) {
            CourseReviewFormFillRequest formFillRequest = new CourseReviewFormFillRequest(
                    courseBilkentIds.get(i)
                    ,coursesHost.get(i).getId()
                    ,null
            );
            forms.add(courseReviewFormService.fillReviewForm(formFillRequest));
        }
        return forms;
    }


    private CourseHost saveCourseHost(String name, double credit) {
        CourseHost course = new CourseHost();
        course.setName(name);
        // course.setUnderDepartment(department);
        course.setCreditECTS(credit);
        return courseHostService.save(course);
    }

    private List<CourseHost> saveAllHostCourses(List<String> courseNames, List<String> departments, List<Double> credits) {
        List<CourseHost> courseHosts = new ArrayList<>();
        for (int i = 0; i < courseNames.size(); i++) {
            courseHosts.add(
                    saveCourseHost(
                    courseNames.get(i)
                    ,credits.get(i)
                )
            );
        }
        return courseHosts;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<PreApprovalFormErasmusDetail> filter(PreApprovalFormListDTO filter) {
        /*if (filter.getType().equalsIgnoreCase("all")) {
            // todo --> call preapproval form repo
        } else if ((filter.getType().equalsIgnoreCase("erasmus"))) {
            // todo --> call preapproval erasmus repo
        } else if ((filter.getType().equalsIgnoreCase("exchange"))){
            // todo --> call preapproval exchange repo
        }*/

        log.info("Process filtering for erasmus is running.");
        Page<PreApprovalFormErasmusDetail> page = erasmusDetailRepository.findAll((root, query, criteriaBuilder) -> {
            query.distinct(true);
            query.orderBy(criteriaBuilder.asc(root.get("id")));
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getAcademicYear() != null) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("preApprovalForm").get("academicYear"), filter.getAcademicYear())));
            }

            if (filter.getSemester() != null) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("preApprovalForm").get("semester"), filter.getSemester())));
            }

            if (filter.getStatus() != null) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("preApprovalForm").get("status"), filter.getStatus())));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }, PageRequest.of(0, 10));
        log.info("Process filtering is successfully completed.");
        return page.getContent();
    }

}
