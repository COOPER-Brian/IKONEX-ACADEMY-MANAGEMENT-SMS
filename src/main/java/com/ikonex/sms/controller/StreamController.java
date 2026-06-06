package com.ikonex.sms.controller;

import com.ikonex.sms.model.StreamEntity;
import com.ikonex.sms.service.AssessmentService;
import com.ikonex.sms.service.PdfReportCardService;
import com.ikonex.sms.service.StreamService;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/streams")
public class StreamController {

    @Autowired
    private StreamService streamService;

    // View all streams & display the form to add a new stream
    @GetMapping
    public String viewStreamsPage(Model model) {
        model.addAttribute("streams", streamService.getAllStreams());
        model.addAttribute("newStream", new StreamEntity());
        return "streams"; // This tells Spring Boot to look for 'streams.html'
    }

    // Process the form submission for creating a new stream
    @PostMapping("/add")
    public String addNewStream(@ModelAttribute("newStream") StreamEntity stream, Model model) {
        String result = streamService.saveStream(stream);
        if (result.startsWith("Error")) {
            model.addAttribute("errorMessage", result);
            model.addAttribute("streams", streamService.getAllStreams());
            return "streams";
        }
        return "redirect:/streams"; // Refresh page on success
    }

    // View details of a single stream
    @GetMapping("/{id}")
    public String viewStreamDetails(@PathVariable("id") Long id, Model model) {
        StreamEntity stream = streamService.getStreamById(id);
        if (stream == null) {
            return "redirect:/streams";
        }
        model.addAttribute("stream", stream);
        return "stream-details"; // This looks for 'stream-details.html'
    }
    @Autowired
    private AssessmentService assessmentService;

    @Autowired
    private PdfReportCardService pdfReportCardService;

    // View aggregated class performance report panel
    @GetMapping("/report/{id}")
    public String viewClassReport(@PathVariable("id") Long id, Model model) {
        com.ikonex.sms.dto.StreamReportDTO report = assessmentService.getStreamPerformanceReport(id);
        if (report == null) {
            return "redirect:/streams";
        }
        model.addAttribute("report", report);
        model.addAttribute("streamId", id);
        return "stream-report"; // Looks for stream-report.html
    }

    // Export class performance report as a downloadable summary PDF document
    @GetMapping("/report/{id}/pdf")
    public void downloadClassReportPdf(@PathVariable("id") Long id, HttpServletResponse response) throws IOException {
        com.ikonex.sms.dto.StreamReportDTO report = assessmentService.getStreamPerformanceReport(id);
        if (report == null) {
            response.sendRedirect("/streams");
            return;
        }

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=ClassReport_" + report.getStreamName().replaceAll(" ", "_") + ".pdf");

        pdfReportCardService.generateClassPerformanceReport(report, response);
    }
}