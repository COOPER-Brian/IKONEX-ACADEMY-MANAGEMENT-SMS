package com.ikonex.sms.service;

import com.ikonex.sms.dto.StreamReportDTO;
import com.ikonex.sms.model.Assessment;
import com.ikonex.sms.model.Student;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

@Service
public class PdfReportCardService {

    // --- METHOD 1: GENERATE INDIVIDUAL STUDENT REPORT CARD ---
    public void generateReportCard(Student student, List<Assessment> assessments, HttpServletResponse response) throws IOException {
        // Initialize a standard A4 document layout
        Document document = new Document(PageSize.A4, 36, 36, 36, 36);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        // 1. Title/Header Styling
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, Color.DARK_GRAY);
        Paragraph title = new Paragraph("IKONEX ACADEMY", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 12, Color.GRAY);
        Paragraph subtitle = new Paragraph("Official Terminal Academic Assessment Report", subtitleFont);
        subtitle.setAlignment(Element.ALIGN_CENTER);
        subtitle.setSpacingAfter(20);
        document.add(subtitle);

        // 2. Student Information Section
        Font infoFont = FontFactory.getFont(FontFactory.HELVETICA, 11, Color.BLACK);
        Font boldInfoFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, Color.BLACK);

        Paragraph p1 = new Paragraph();
        p1.add(new Chunk("Student Name: ", boldInfoFont));
        p1.add(new Chunk(student.getFirstName() + " " + student.getLastName() + "\n", infoFont));
        p1.add(new Chunk("Admission Number: ", boldInfoFont));
        p1.add(new Chunk(student.getAdmissionNumber() + "\n", infoFont));
        p1.add(new Chunk("Class Stream: ", boldInfoFont));
        p1.add(new Chunk((student.getStream() != null ? student.getStream().getName() : "Unassigned") + "\n", infoFont));
        p1.setSpacingAfter(25);
        document.add(p1);

        // 3. Academic Scores Table Setup
        PdfPTable table = new PdfPTable(4); // 4 columns
        table.setWidthPercentage(100);
        table.setWidths(new float[] {2f, 4f, 2f, 2f}); // column proportions

        // Design Table Headers
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, Color.WHITE);
        Color headerBg = new Color(44, 62, 80); // Deep Dark Blue Accent

        String[] headers = {"Subject Code", "Subject Name", "Marks Obtained", "Percentage"};
        for (String headerTitle : headers) {
            PdfPCell headerCell = new PdfPCell(new Paragraph(headerTitle, headerFont));
            headerCell.setBackgroundColor(headerBg);
            headerCell.setPadding(8);
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(headerCell);
        }

        // 4. Fill Table with Student Grades & track totals
        double totalMarksObtained = 0;
        double totalMaxMarks = 0;

        for (Assessment assessment : assessments) {
            table.addCell(createCenterCell(assessment.getSubject().getCode(), infoFont));
            table.addCell(new PdfPCell(new Paragraph(assessment.getSubject().getName(), infoFont)));
            
            String marksString = assessment.getMarksObtained() + " / " + assessment.getMaxMarks();
            table.addCell(createCenterCell(marksString, infoFont));
            
            String percentageString = String.format("%.2f%%", assessment.getPercentage());
            table.addCell(createCenterCell(percentageString, infoFont));

            totalMarksObtained += assessment.getMarksObtained();
            totalMaxMarks += assessment.getMaxMarks();
        }

        document.add(table);

        // 5. Summary Analysis Section (Overall Grade calculations)
        if (!assessments.isEmpty()) {
            double overallPercentage = (totalMarksObtained / totalMaxMarks) * 100;
            
            Paragraph summary = new Paragraph();
            summary.setSpacingBefore(20);
            summary.add(new Chunk("\nOVERALL PERFORMANCE SUMMARY\n", boldInfoFont));
            summary.add(new Chunk("Total Marks: ", boldInfoFont));
            summary.add(new Chunk(totalMarksObtained + " out of " + totalMaxMarks + "\n", infoFont));
            summary.add(new Chunk("Mean Percentage Score: ", boldInfoFont));
            summary.add(new Chunk(String.format("%.2f%%", overallPercentage) + "\n", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, new Color(39, 174, 96))));
            
            document.add(summary);
        } else {
            Paragraph noMarks = new Paragraph("\nNo formal exam assessment scores recorded for this grading cycle.", infoFont);
            document.add(noMarks);
        }

        document.close();
    }

    // --- METHOD 2: GENERATE AGGREGATED CLASS PERFORMANCE REPORT ---
    public void generateClassPerformanceReport(StreamReportDTO report, HttpServletResponse response) throws IOException {
        Document document = new Document(PageSize.A4, 36, 36, 36, 36);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, Color.DARK_GRAY);
        Paragraph title = new Paragraph("IKONEX ACADEMY - CLASS PERFORMANCE REPORT", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(5);
        document.add(title);

        Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Color.GRAY);
        Paragraph subtitle = new Paragraph("Stream: " + report.getStreamName() + " | Total Enrollment: " + report.getTotalStudents() + " Students", subtitleFont);
        subtitle.setAlignment(Element.ALIGN_CENTER);
        subtitle.setSpacingAfter(25);
        document.add(subtitle);

        Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, new Color(44, 62, 80));
        Font tableHeaderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, Color.WHITE);
        Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 11, Color.BLACK);
        Color headerBg = new Color(52, 73, 94);

        // --- SECTION A: SUBJECT PERFORMANCE AVERAGES ---
        document.add(new Paragraph("Subject Performance Breakdown", sectionFont));
        document.add(new Paragraph(" ", bodyFont)); // Spacer

        PdfPTable subjectTable = new PdfPTable(2);
        subjectTable.setWidthPercentage(100);
        
        PdfPCell subHeader1 = new PdfPCell(new Paragraph("Subject Title", tableHeaderFont));
        subHeader1.setBackgroundColor(headerBg);
        subHeader1.setPadding(6);
        subjectTable.addCell(subHeader1);

        PdfPCell subHeader2 = new PdfPCell(new Paragraph("Mean Percentage Score", tableHeaderFont));
        subHeader2.setBackgroundColor(headerBg);
        subHeader2.setPadding(6);
        subjectTable.addCell(subHeader2);

        report.getSubjectAverages().forEach((subject, avg) -> {
            subjectTable.addCell(new PdfPCell(new Paragraph(subject, bodyFont)) {{ setPadding(6); }});
            subjectTable.addCell(new PdfPCell(new Paragraph(String.format("%.2f%%", avg), bodyFont)) {{ setPadding(6); }});
        });
        document.add(subjectTable);

        // --- SECTION B: STUDENT MERIT RANKINGS ---
        document.add(new Paragraph("\nStudent Merit Order Rankings", sectionFont));
        document.add(new Paragraph(" ", bodyFont)); // Spacer

        PdfPTable rankTable = new PdfPTable(3);
        rankTable.setWidthPercentage(100);
        rankTable.setWidths(new float[]{1.5f, 5f, 2.5f});

        PdfPCell rankHeader1 = new PdfPCell(new Paragraph("Rank", tableHeaderFont));
        rankHeader1.setBackgroundColor(headerBg);
        rankHeader1.setPadding(6);
        rankTable.addCell(rankHeader1);

        PdfPCell rankHeader2 = new PdfPCell(new Paragraph("Student Name", tableHeaderFont));
        rankHeader2.setBackgroundColor(headerBg);
        rankHeader2.setPadding(6);
        rankTable.addCell(rankHeader2);

        PdfPCell rankHeader3 = new PdfPCell(new Paragraph("Mean Average Score", tableHeaderFont));
        rankHeader3.setBackgroundColor(headerBg);
        rankHeader3.setPadding(6);
        rankTable.addCell(rankHeader3);

        final int[] rank = {1};
        report.getStudentRankings().forEach((studentName, meanScore) -> {
            rankTable.addCell(new PdfPCell(new Paragraph(String.valueOf(rank[0]++), bodyFont)) {{ setPadding(6); }});
            rankTable.addCell(new PdfPCell(new Paragraph(studentName, bodyFont)) {{ setPadding(6); }});
            rankTable.addCell(new PdfPCell(new Paragraph(String.format("%.2f%%", meanScore), bodyFont)) {{ setPadding(6); }});
        });
        document.add(rankTable);

        document.close();
    }

    // Helper method to keep text center-aligned inside table columns
    private PdfPCell createCenterCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Paragraph(text, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(6);
        return cell;
    }
}