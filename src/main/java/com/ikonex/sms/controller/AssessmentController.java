package com.ikonex.sms.controller;

import com.ikonex.sms.model.Assessment;
import com.ikonex.sms.model.Student;
import com.ikonex.sms.service.AssessmentService;
import com.ikonex.sms.service.StudentService;
import com.ikonex.sms.service.SubjectService;
import com.ikonex.sms.service.PdfReportCardService;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/assessments")
public class AssessmentController {

    @Autowired
    private AssessmentService assessmentService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private PdfReportCardService pdfReportCardService;

    // 1. View specific student's marks and display the entry form
    @GetMapping("/student/{studentId}")
    public String viewStudentMarks(@PathVariable("studentId") Long studentId, Model model) {
        Student student = studentService.getStudentById(studentId);
        if (student == null) {
            return "redirect:/students";
        }
        
        model.addAttribute("student", student);
        model.addAttribute("assessments", assessmentService.getScoresByStudent(studentId));
        model.addAttribute("subjects", subjectService.getAllSubjects());
        
        Assessment newAssessment = new Assessment();
        newAssessment.setStudent(student);
        model.addAttribute("newAssessment", newAssessment);
        
        return "student-marks"; // Looks for student-marks.html
    }

    // 2. Process the submission of new marks
    @PostMapping("/add")
    public String addMark(@ModelAttribute("newAssessment") Assessment assessment, @RequestParam("studentId") Long studentId) {
        Student student = studentService.getStudentById(studentId);
        if (student != null) {
            assessment.setStudent(student);
            assessmentService.saveAssessment(assessment);
        }
        return "redirect:/assessments/student/" + studentId;
    }

    // 3. Delete a grade entry
    @GetMapping("/delete/{id}")
    public String deleteMark(@PathVariable("id") Long id, @RequestParam("studentId") Long studentId) {
        assessmentService.deleteAssessment(id);
        return "redirect:/assessments/student/" + studentId;
    }

    // 4. Export Performance Report Card as a downloadable PDF document
    @GetMapping("/student/{studentId}/pdf")
    public void downloadPdfReportCard(@PathVariable("studentId") Long studentId, HttpServletResponse response) throws IOException {
        Student student = studentService.getStudentById(studentId);
        if (student == null) {
            response.sendRedirect("/students");
            return;
        }

        // Configure the browser download transmission response headers
        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=ReportCard_" + student.getFirstName() + "_" + student.getLastName() + ".pdf";
        response.setHeader(headerKey, headerValue);

        // Retrieve existing academic scores dataset from the database
        List<Assessment> assessments = assessmentService.getScoresByStudent(studentId);

        // Compile and output the OpenPDF binary document stream
        pdfReportCardService.generateReportCard(student, assessments, response);
    }
}