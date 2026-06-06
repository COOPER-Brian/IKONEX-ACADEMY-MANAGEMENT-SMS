package com.ikonex.sms.service;

import com.ikonex.sms.dto.StreamReportDTO;
import com.ikonex.sms.model.Assessment;
import com.ikonex.sms.model.StreamEntity;
import com.ikonex.sms.repository.AssessmentRepository;
import com.ikonex.sms.repository.StreamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AssessmentService {

    @Autowired
    private AssessmentRepository assessmentRepository;

    @Autowired
    private StreamRepository streamRepository;

    // 1. Fetch all marks belonging to a specific student
    public List<Assessment> getScoresByStudent(Long studentId) {
        return assessmentRepository.findByStudentId(studentId);
    }

    // 2. Persist or update an assessment score
    public void saveAssessment(Assessment assessment) {
        assessmentRepository.save(assessment);
    }

    // 3. Delete an assessment entry by its ID
    public void deleteAssessment(Long id) {
        assessmentRepository.deleteById(id);
    }

    // 4. Generate aggregated analytics for a class stream performance report
    public StreamReportDTO getStreamPerformanceReport(Long streamId) {
        StreamEntity stream = streamRepository.findById(streamId).orElse(null);
        if (stream == null) {
            return null;
        }

        // Pull raw assessment records for the entire stream
        List<Assessment> assessments = assessmentRepository.findByStreamId(streamId);
        int totalStudents = stream.getStudents() != null ? stream.getStudents().size() : 0;

        // Group structures to hold scores for calculations
        Map<String, List<Double>> subjectScoresMap = new HashMap<>();
        Map<String, List<Double>> studentScoresMap = new HashMap<>();

        // Populate datasets with percentages
        for (Assessment a : assessments) {
            String subjectName = a.getSubject().getName();
            String studentName = a.getStudent().getFirstName() + " " + a.getStudent().getLastName();
            double percentageScore = a.getPercentage();

            subjectScoresMap.computeIfAbsent(subjectName, k -> new ArrayList<>()).add(percentageScore);
            studentScoresMap.computeIfAbsent(studentName, k -> new ArrayList<>()).add(percentageScore);
        }

        // Calculate average performance for every subject
        Map<String, Double> subjectAverages = subjectScoresMap.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().stream().mapToDouble(Double::doubleValue).average().orElse(0.0)
                ));

        // Calculate student means and sort them descending to form the leaderboard ranking
        Map<String, Double> studentRankings = studentScoresMap.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().stream().mapToDouble(Double::doubleValue).average().orElse(0.0)
                )).entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey, 
                        Map.Entry::getValue,
                        (e1, e2) -> e1, 
                        LinkedHashMap::new
                ));

        return new StreamReportDTO(stream.getName(), totalStudents, subjectAverages, studentRankings);
    }
}