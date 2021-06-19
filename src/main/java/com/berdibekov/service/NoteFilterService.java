package com.berdibekov.service;

import com.berdibekov.domain.Note;
import com.berdibekov.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;
import java.util.List;

@Component
public class NoteFilterService {

    private NoteRepository noteRepository;

    @Autowired
    public NoteFilterService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public List<Note> getFilteredNotes(@RequestParam(required = false) String subString, @RequestParam(required = false) String hashTag) throws SQLException {
        List<Note> notes;
        if (subString == null && hashTag != null) {
            notes = noteRepository.findAllByHashTag(hashTag);
        } else if (subString != null && hashTag == null) {
            notes = noteRepository.findAllBySubString(subString);
        } else if (subString != null && hashTag != null) {
            notes = noteRepository.findAllByHashTagAndSubString(subString, hashTag);
        } else {
            notes = noteRepository.findAll();
        }
        return notes;
    }
}
