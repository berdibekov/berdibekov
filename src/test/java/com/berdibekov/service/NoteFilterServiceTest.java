package com.berdibekov.service;

import com.berdibekov.repository.NoteRepository;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.mockito.Mockito.*;

class NoteFilterServiceTest {

    @Test
    void getFilteredNotes_shouldInvokeFindAll_whenSubStringAndHashTagAreNulls() throws SQLException {
        NoteRepository noteRepository = mock(NoteRepository.class);
        NoteFilterService noteFilterService = new NoteFilterService(noteRepository);
        noteFilterService.getFilteredNotes(null, null);
        verify(noteRepository, times(1)).findAll();
        verify(noteRepository, times(0)).findAllByHashTagAndSubString(null, null);
        verify(noteRepository, times(0)).findAllByHashTag(null);
        verify(noteRepository, times(0)).findAllBySubString(null);
    }

    @Test
    void getFilteredNotes_shouldInvokeFindAllByHashTag_whenSubStringNullAndHashTagNotNull() throws SQLException {
        NoteRepository noteRepository = mock(NoteRepository.class);
        NoteFilterService noteFilterService = new NoteFilterService(noteRepository);
        noteFilterService.getFilteredNotes(null, "some tag");
        verify(noteRepository, times(0)).findAll();
        verify(noteRepository, times(0)).findAllByHashTagAndSubString(null, "some tag");
        verify(noteRepository, times(1)).findAllByHashTag("some tag");
        verify(noteRepository, times(0)).findAllBySubString(null);
    }

    @Test
    void getFilteredNotes_shouldInvokeFindAllBySubString_whenSubStringNotNullAndHashTagNull() throws SQLException {
        NoteRepository noteRepository = mock(NoteRepository.class);
        NoteFilterService noteFilterService = new NoteFilterService(noteRepository);
        noteFilterService.getFilteredNotes("some subString", null);
        verify(noteRepository, times(0)).findAllByHashTagAndSubString("some subString", null);
        verify(noteRepository, times(0)).findAll();
        verify(noteRepository, times(0)).findAllByHashTag(null);
        verify(noteRepository, times(1)).findAllBySubString("some subString");
    }

    @Test
    void getFilteredNotes_shouldInvokeFindAllByHashTagAndSubString_whenSubStringAndHashTagAreNotNull() throws SQLException {
        NoteRepository noteRepository = mock(NoteRepository.class);
        NoteFilterService noteFilterService = new NoteFilterService(noteRepository);
        noteFilterService.getFilteredNotes("some subString", "some tag");
        verify(noteRepository, times(0)).findAll();
        verify(noteRepository, times(0)).findAllByHashTag("some tag");
        verify(noteRepository, times(0)).findAllBySubString("some subString");
        verify(noteRepository, times(1)).findAllByHashTagAndSubString("some subString", "some tag");
    }
}