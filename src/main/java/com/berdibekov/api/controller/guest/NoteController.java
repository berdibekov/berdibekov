package com.berdibekov.api.controller.guest;

import com.berdibekov.domain.Note;
import com.berdibekov.dto.error.ErrorDetail;
import com.berdibekov.exception.ResourceNotFoundException;
import com.berdibekov.repository.NoteRepository;
import com.berdibekov.service.NoteFilterService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@RequestMapping({"/guest/api/"})
@RestController("NoteControllerGuest")
public class NoteController {

    private NoteRepository noteRepository;
    private NoteFilterService noteFilterService;

    @Autowired
    public NoteController(NoteRepository noteRepository, NoteFilterService noteFilterService) {
        this.noteRepository = noteRepository;
        this.noteFilterService = noteFilterService;
    }

    @RequestMapping(value = "/notes", method = RequestMethod.GET)
    @ApiOperation(value = "Get filtered notes")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Successfully."),
            @ApiResponse(code = 500, message = "Error creating Note", response = ErrorDetail.class)})

    public ResponseEntity<List<Note>> getFilteredNotes(@RequestParam(required = false) String subString,
                                                       @RequestParam(required = false) String hashTag) throws SQLException {
        List<Note> notes = noteFilterService.getFilteredNotes(subString, hashTag);
        return new ResponseEntity<>(notes, HttpStatus.OK);
    }


    @RequestMapping(value = "/notes", method = RequestMethod.POST)
    @ApiOperation(value = "Creates a new Note")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Note Created Successfully."),
            @ApiResponse(code = 500, message = "Error creating Note", response = ErrorDetail.class)})

    public ResponseEntity<Void> createNote(@Valid @RequestBody Note note) throws SQLException {
        note.setDateTime(new Date(System.currentTimeMillis()));
        note = noteRepository.save(note);
        HttpHeaders responseHeaders = getHttpHeadersForNewResource(note.getId());
        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/notes/{noteId}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Deletes given note")
    @ApiResponses(value = {@ApiResponse(code = 200, message = ""),
            @ApiResponse(code = 404, message = "Unable to find note.", response = ErrorDetail.class)})

    public ResponseEntity<Void> deleteNote(@PathVariable Long noteId) throws SQLException {
        verifyNote(noteId);
        noteRepository.deleteById(noteId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/notes/{noteId}", method = RequestMethod.PUT)
    @ApiOperation(value = "Creates a new Note")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Note created successfully."),
            @ApiResponse(code = 500, message = "Error creating Poll", response = ErrorDetail.class)})

    public ResponseEntity<Void> updateNote(@PathVariable long noteId, @Valid @RequestBody Note note) throws SQLException {
        verifyNote(noteId);
        note.setDateTime(new Date(System.currentTimeMillis()));
        noteRepository.update(noteId, note);
        HttpHeaders responseHeaders = getHttpHeadersForNewResource(note.getId());
        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }

    private HttpHeaders getHttpHeadersForNewResource(Long id) {
        HttpHeaders responseHeaders = new HttpHeaders();
        URI newPollUri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
        responseHeaders.setLocation(newPollUri);
        return responseHeaders;
    }

    protected void verifyNote(Long noteId) throws ResourceNotFoundException, SQLException {
        if (!noteRepository.isExist(noteId)) {
            throw new ResourceNotFoundException("Note with id " + noteId + " not found.");
        }
    }
}
