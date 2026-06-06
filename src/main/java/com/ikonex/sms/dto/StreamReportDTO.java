package com.ikonex.sms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Map;

@Data
@AllArgsConstructor
public class StreamReportDTO {
    private String streamName;
    private int totalStudents;
    private Map<String, Double> subjectAverages; // Subject Name -> Average Percentage
    private Map<String, Double> studentRankings; // Student Full Name -> Mean Percentage
}