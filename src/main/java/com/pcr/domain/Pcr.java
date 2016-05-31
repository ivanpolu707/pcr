package com.pcr.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "T_PCR_MASTER")
public class Pcr {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pcrId;
    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreated;
    private LocalDateTime dateDeleted;
    @Size(max = 100)
    private String clientName;
    @Size(max = 255)
    private String notes;
    @Lob
    private byte[] pcrFile;
    private String fileName;
    private LocalDate dateApproved;
    private LocalDate dateCompleted;
    private LocalDate dateBilled;
    private boolean deleted;
    @Transient
    private String className;

    @PrePersist
    public void onCreate() {
        this.dateCreated = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Pcr{" +
                "pcrId=" + pcrId +
                ", dateCreated=" + dateCreated +
                ", dateDeleted=" + dateDeleted +
                ", clientName='" + clientName + '\'' +
                ", notes='" + notes + '\'' +
                ", fileName='" + fileName + '\'' +
                ", dateApproved=" + dateApproved +
                ", dateCompleted=" + dateCompleted +
                ", dateBilled=" + dateBilled +
                ", deleted=" + deleted +
                '}';
    }
}
