package com.berdibekov.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

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

    private Date dateTime;
}
