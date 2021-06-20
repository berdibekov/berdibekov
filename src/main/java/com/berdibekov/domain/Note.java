package com.berdibekov.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
public class Note {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;

    private String noteName;

    @NotNull(message = "text must be not null")
    private String text;

    private List<String> hashTags;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Date dateTime;
}
