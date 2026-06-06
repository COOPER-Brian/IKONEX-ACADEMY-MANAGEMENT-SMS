package com.ikonex.sms.service;

import com.ikonex.sms.model.StreamEntity;
import com.ikonex.sms.repository.StreamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StreamService {

    @Autowired
    private StreamRepository streamRepository;

    // View all streams
    public List<StreamEntity> getAllStreams() {
        return streamRepository.findAll();
    }

    // View details of a single stream
    public StreamEntity getStreamById(Long id) {
        return streamRepository.findById(id).orElse(null);
    }

    // Create a new stream
    public String saveStream(StreamEntity stream) {
        // Prevent duplicate streams
        if (streamRepository.existsByName(stream.getName())) {
            return "Error: A stream with that name already exists!";
        }
        streamRepository.save(stream);
        return "Success";
    }
}