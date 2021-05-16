package org.sid.jeehospitalmanager.controllers;

import org.sid.jeehospitalmanager.entities.Patient;
import org.sid.jeehospitalmanager.repositories.PatientRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PatientRestController {

    PatientRepository patientRepository;

    @GetMapping("/listPatients")
    public List<Patient> list(){
        return patientRepository.findAll();
    }

    @GetMapping("/Patients/{id}")
    public Patient list(@PathVariable Long id){
        return patientRepository.findById(id).get();
    }

}
