package com.berdibekov.domain;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
public class Note {
    private long id;

    private String noteName;

    @NotNull
    private String text;

    @NotNull
    private LocalDate date;

    private List<String> hashTags;
}
