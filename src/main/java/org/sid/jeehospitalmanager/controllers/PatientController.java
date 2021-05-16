package org.sid.jeehospitalmanager.controllers;

import org.jetbrains.annotations.NotNull;
import org.sid.jeehospitalmanager.entities.Patient;
import org.sid.jeehospitalmanager.repositories.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class PatientController {

    @Autowired
    PatientRepository patientRepository;

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("patients")
    public String list(Model model,
                       @RequestParam(name = "page", defaultValue = "0") int page,
                       @RequestParam(name = "size", defaultValue = "5") int size,
                       @RequestParam(name = "keyword", defaultValue = "") String mc) {
        Page<Patient> patientPage = patientRepository
                .findByNameContains(mc, (org.springframework.data.domain.Pageable) PageRequest.of(page, size));

        patientListAddedAttributes(model, page, size, mc, patientPage);
        return "patients";
    }

    private void patientListAddedAttributes(Model model, int page, int size, String mc, Page<Patient> patientPage) {
        model.addAttribute("patients", patientPage.getContent());
        model.addAttribute("pages", new int[patientPage.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("keyword", mc);
    }



    @GetMapping(path = "/formPatient")
    public String formPatient(Model model){
        model.addAttribute("mode", "new");
        model.addAttribute("patient", new Patient());
        return "formPatient";
    }

    @GetMapping(path = "/editPatient")
    public String editPatient(Model model, Long id){
        Patient patient = patientRepository.findById(id).get();
        model.addAttribute("patient", patient);
        model.addAttribute("mode", "edit");
        return "/formPatient";
    }

    @GetMapping(path = "/deletePatient")
    public String delete(Long id, String keyword, int page, int size){
        patientRepository.deleteById(id);
        return "redirect:patients?page=" + page + "&size=" + size + "&keyword=" + keyword;
    }

     @PostMapping(path = "/savePatient")
    public String savePatient(Model model, @Valid Patient patient, BindingResult bindingResult,String mode){
        model.addAttribute("mode", mode);
        model.addAttribute("patient", patient);
        if (bindingResult.hasErrors()){
            return saveError(patient);
        }
        patientRepository.save(patient);
        return correctlyAddedOrSaved(mode, patient.getId());
    }

    @NotNull
    private String saveError(Patient patient) {
        if (checkIfPatientDataAreNull(patient))
          return "redirect:editPatient?id="+ patient.getId();
        return "formPatient";
    }

    @NotNull
    private String correctlyAddedOrSaved(String mode, Long id) {
        return "redirect:confirmation?id="+id+"&mode="+ mode ;
    }

    private boolean checkIfPatientDataAreNull(Patient patient) {
        return patient.getId() != null &&
               patient.getName() != null &&
               patient.getBirthDate() != null ;

    }

    @GetMapping("/confirmation")
    public String confirmation(Model model, String mode, Long id){
        Patient patient = patientRepository.findById(id).get();
        model.addAttribute("patient", patient);
        model.addAttribute("mode", mode);
        return "confirmation";
    }
}
