package com.ikonex.sms.service;

import com.ikonex.sms.model.Subject;
import com.ikonex.sms.model.StreamEntity;
import com.ikonex.sms.repository.SubjectRepository;
import com.ikonex.sms.repository.StreamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private StreamRepository streamRepository;

    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    public Subject getSubjectById(Long id) {
        return subjectRepository.findById(id).orElse(null);
    }

    public String saveSubject(Subject subject) {
        if (subject.getId() == null && subjectRepository.existsByCode(subject.getCode())) {
            return "Error: Subject code already exists!";
        }
        subjectRepository.save(subject);
        return "Success";
    }

    public void deleteSubject(Long id) {
        subjectRepository.deleteById(id);
    }

    // Assign a subject to a specific stream
    public void assignSubjectToStream(Long subjectId, Long streamId) {
        Subject subject = subjectRepository.findById(subjectId).orElse(null);
        StreamEntity stream = streamRepository.findById(streamId).orElse(null);
        
        if (subject != null && stream != null) {
            if (!subject.getStreams().contains(stream)) {
                subject.getStreams().add(stream);
                subjectRepository.save(subject);
            }
        }
    }
}