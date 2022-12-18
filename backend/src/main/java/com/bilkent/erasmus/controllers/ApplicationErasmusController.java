package com.bilkent.erasmus.controllers;
import com.bilkent.erasmus.dtos.InitialApplicationDTO.ApplicationErasmusDTO;
import com.bilkent.erasmus.services.ApplicationErasmusService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/erasmus-application")
public class ApplicationErasmusController {

    private final ApplicationErasmusService applicationErasmusService;

    public ApplicationErasmusController( ApplicationErasmusService applicationErasmusService) {
        this.applicationErasmusService = applicationErasmusService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createErasmusApplication(@RequestBody ApplicationErasmusDTO applicationErasmusDTO){
        return new ResponseEntity<>(applicationErasmusService.createErasmusApplication(applicationErasmusDTO), HttpStatus.CREATED);
    }

    @PatchMapping("/edit")
    public ResponseEntity<?> editErasmusApplication(@RequestBody ApplicationErasmusDTO applicationErasmusDTO) throws Exception {
        return new ResponseEntity<>(applicationErasmusService.editErasmusApplication(applicationErasmusDTO), HttpStatus.OK);
    }

    @PostMapping("/cancel")
    public ResponseEntity<?> cancelErasmusApplication() throws Exception {
        applicationErasmusService.cancelErasmusApplication();
        return new ResponseEntity<>("Application cancelled", HttpStatus.OK);
    }

    @PostMapping("/place")
    public ResponseEntity<?> placeStudents(){
        applicationErasmusService.placeStudents();
        return new ResponseEntity<>("students placed", HttpStatus.OK);
    }

    @PostMapping("/reevaluate")
    public ResponseEntity<?> reevaluateApplications(){
        applicationErasmusService.reevaluateApplications();
        return new ResponseEntity<>("applications reevaluated", HttpStatus.OK);
    }
    @RolesAllowed("ROLE_STUDENT")
    @GetMapping("/view-application")
    public ResponseEntity<?> viewApplication(){
        return new ResponseEntity<>(applicationErasmusService.viewApplication(), HttpStatus.OK);
    }

    @RolesAllowed("ROLE_STUDENT")
    @GetMapping("/view-application-by-id")
    public ResponseEntity<?> viewApplication(@RequestBody int id) {
        return new ResponseEntity<>(applicationErasmusService.viewApplicationById(id), HttpStatus.OK);
    }

    @RolesAllowed("ROLE_STUDENT")
    @GetMapping("/student/view-application-all")
    public ResponseEntity<?> viewApplicationAllForStudent() {
        return new ResponseEntity<>(applicationErasmusService.viewApplicationStudentAll(), HttpStatus.OK);
    }

    @GetMapping("/all-applications")
    public ResponseEntity<?> viewAllApplication(){
        return new ResponseEntity<>(applicationErasmusService.viewAllApplications(), HttpStatus.OK);
    }
}

