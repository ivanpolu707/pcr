package com.pcr.model;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@ToString
@Getter
@Setter
public class PcrDto {

    private Long pcrId;
    private LocalDateTime dateCreated;
    private LocalDateTime dateUpdated;
    @Size(max = 100)
    private String clientName;
    @Size(max = 255)
    private String notes;
    @Lob
    private byte[] pcrFile;
    private String fileName;
    private LocalDate dateApproved;
    private LocalDate dateBilled;
    private LocalDate dateCompleted;
    private Boolean deleted;

}
