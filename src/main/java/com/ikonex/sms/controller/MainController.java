package com.ikonex.sms.controller;

import com.ikonex.sms.service.StreamService;
import com.ikonex.sms.service.StudentService;
import com.ikonex.sms.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private StreamService streamService;

    @Autowired
    private SubjectService subjectService;

    @GetMapping("/")
    public String viewDashboard(Model model) {
        // Pass total counts to display on the dashboard home cards
        model.addAttribute("totalStudents", (long) studentService.getAllStudents().size());
        model.addAttribute("totalStreams", (long) streamService.getAllStreams().size());
        model.addAttribute("totalSubjects", (long) subjectService.getAllSubjects().size());
        return "index"; // Looks for index.html
    }
}