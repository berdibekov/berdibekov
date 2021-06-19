package com.berdibekov.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
public class Note {

    private long id;

    private String noteName;

    @NotNull(message = "text must be not null")
    private String text;

    private List<String> hashTags;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Date dateTime;
}
