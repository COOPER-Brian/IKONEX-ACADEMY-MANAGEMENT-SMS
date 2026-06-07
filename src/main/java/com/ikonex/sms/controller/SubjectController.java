package com.ikonex.sms.controller;

import com.ikonex.sms.model.Subject;
import com.ikonex.sms.service.SubjectService;
import com.ikonex.sms.service.StreamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

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

        List<Subject> subjects = subjectService.getAllSubjects();
        if (subjects == null) subjects = Collections.emptyList();

        model.addAttribute("subjects", subjects);

        model.addAttribute("streams",
                streamService.getAllStreams() != null
                        ? streamService.getAllStreams()
                        : Collections.emptyList()
        );

        model.addAttribute("newSubject", new Subject());

        return "subjects";
    }

    // Add subject
    @PostMapping("/add")
    public String addSubject(@ModelAttribute("newSubject") Subject subject, Model model) {

        String result = subjectService.saveSubject(subject);

        if (result != null && result.startsWith("Error")) {
            model.addAttribute("errorMessage", result);

            model.addAttribute("subjects",
                    subjectService.getAllSubjects() != null
                            ? subjectService.getAllSubjects()
                            : Collections.emptyList()
            );

            model.addAttribute("streams",
                    streamService.getAllStreams() != null
                            ? streamService.getAllStreams()
                            : Collections.emptyList()
            );

            return "subjects";
        }

        return "redirect:/subjects";
    }

    // Assign subject to stream
    @PostMapping("/assign")
    public String assignToStream(@RequestParam("subjectId") Long subjectId,
                                  @RequestParam("streamId") Long streamId) {
        subjectService.assignSubjectToStream(subjectId, streamId);
        return "redirect:/subjects";
    }

    // Edit form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {

        Subject subject = subjectService.getSubjectById(id);

        if (subject == null) {
            return "redirect:/subjects";
        }

        model.addAttribute("subject", subject);
        return "edit-subject";
    }

    // Update subject
    @PostMapping("/update/{id}")
    public String updateSubject(@PathVariable("id") Long id,
                                @ModelAttribute("subject") Subject subject) {
        subject.setId(id);
        subjectService.saveSubject(subject);
        return "redirect:/subjects";
    }

    // Delete subject
    @GetMapping("/delete/{id}")
    public String deleteSubject(@PathVariable("id") Long id) {
        subjectService.deleteSubject(id);
        return "redirect:/subjects";
    }
}