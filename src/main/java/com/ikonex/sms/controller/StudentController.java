package com.ikonex.sms.controller;

import com.ikonex.sms.model.Student;
import com.ikonex.sms.service.StudentService;
import com.ikonex.sms.service.StreamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private StreamService streamService;

    // View all students & display registration form
    @GetMapping
    public String viewStudentsPage(Model model) {
        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("streams", streamService.getAllStreams());
        model.addAttribute("newStudent", new Student());
        return "students"; // Looks for students.html
    }

    // Process Student Registration
    @PostMapping("/add")
    public String registerStudent(@ModelAttribute("newStudent") Student student, Model model) {
        String result = studentService.saveStudent(student);
        if (result.startsWith("Error")) {
            model.addAttribute("errorMessage", result);
            model.addAttribute("students", studentService.getAllStudents());
            model.addAttribute("streams", streamService.getAllStreams());
            return "students";
        }
        return "redirect:/students";
    }

    // Show Edit Form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Student student = studentService.getStudentById(id);
        if (student == null) {
            return "redirect:/students";
        }
        model.addAttribute("student", student);
        model.addAttribute("streams", streamService.getAllStreams());
        return "edit-student"; // Looks for edit-student.html
    }

    // Process Student Update
    @PostMapping("/update/{id}")
    public String updateStudent(@PathVariable("id") Long id, @ModelAttribute("student") Student student) {
        student.setId(id);
        studentService.saveStudent(student);
        return "redirect:/students";
    }

    // Delete Student
    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable("id") Long id) {
        studentService.deleteStudent(id);
        return "redirect:/students";
    }

    // View single student details
    @GetMapping("/{id}")
    public String viewStudentDetails(@PathVariable("id") Long id, Model model) {
        Student student = studentService.getStudentById(id);
        if (student == null) {
            return "redirect:/students";
        }
        model.addAttribute("student", student);
        return "student-details"; // Looks for student-details.html
    }
}