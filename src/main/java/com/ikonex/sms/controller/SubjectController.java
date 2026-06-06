package com.ikonex.sms.controller;

import com.ikonex.sms.model.Subject;
import com.ikonex.sms.service.SubjectService;
import com.ikonex.sms.service.StreamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/subjects")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private StreamService streamService;

    // View all subjects & add form
    @GetMapping
    public String viewSubjectsPage(Model model) {
        model.addAttribute("subjects", subjectService.getAllSubjects());
        model.addAttribute("streams", streamService.getAllStreams());
        model.addAttribute("newSubject", new Subject());
        return "subjects"; // Looks for subjects.html
    }

    // Process Add Subject
    @PostMapping("/add")
    public String addSubject(@ModelAttribute("newSubject") Subject subject, Model model) {
        String result = subjectService.saveSubject(subject);
        if (result.startsWith("Error")) {
            model.addAttribute("errorMessage", result);
            model.addAttribute("subjects", subjectService.getAllSubjects());
            model.addAttribute("streams", streamService.getAllStreams());
            return "subjects";
        }
        return "redirect:/subjects";
    }

    // Process Assign Subject to Stream Form Connection
    @PostMapping("/assign")
    public String assignToStream(@RequestParam("subjectId") Long subjectId, @RequestParam("streamId") Long streamId) {
        subjectService.assignSubjectToStream(subjectId, streamId);
        return "redirect:/subjects";
    }

    // Show Edit Form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Subject subject = subjectService.getSubjectById(id);
        if (subject == null) {
            return "redirect:/subjects";
        }
        model.addAttribute("subject", subject);
        return "edit-subject"; // Looks for edit-subject.html
    }

    // Process Update Subject
    @PostMapping("/update/{id}")
    public String updateSubject(@PathVariable("id") Long id, @ModelAttribute("subject") Subject subject) {
        subject.setId(id);
        subjectService.saveSubject(subject);
        return "redirect:/subjects";
    }

    // Delete Subject
    @GetMapping("/delete/{id}")
    public String deleteSubject(@PathVariable("id") Long id) {
        subjectService.deleteSubject(id);
        return "redirect:/subjects";
    }
}
