package com.berdibekov.api.controller.guest;

import com.berdibekov.NoteApplication;
import com.berdibekov.service.NoteFilterService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = NoteApplication.class)
@ContextConfiguration(classes = MockServletContext.class)
@WebAppConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NoteControllerTest {

    @Mock
    private NoteFilterService noteFilterService;

    @InjectMocks
    NoteController noteController;

    private MockMvc mockMvc;

    @BeforeAll
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(noteController).build();
    }

    @Test
    public void getFilteredNotes_shouldReturnUnfilteredNotes_whenParamsAreNull() throws Exception {
        when(noteFilterService.getFilteredNotes(null, null)).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/guest/api/notes"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    public void getFilteredNotes_shouldReturnFilteredBySubstringNotes_whenSubStringParamNotNullAndHashTagNull()
            throws Exception {
        when(noteFilterService.getFilteredNotes("some value", null)).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/guest/api/notes").param("subString", "some value"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }
}