package com.pcr.service;

import com.pcr.domain.Pcr;
import com.pcr.model.PcrDto;
import com.pcr.repos.PcrRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PcrService {

    @Autowired
    private PcrRepository repository;

    public List<Pcr> getAllPcrs() {
        return repository.findByDateDeletedIsNullOrderByDateApprovedAsc();
    }

    public Pcr savePcr(Pcr pcr) {
        return repository.save(pcr);
    }

    public void deletePcr(Long id) {
        Pcr pcr = repository.findById(id).orElseThrow();
        pcr.setDateDeleted(LocalDateTime.now());
        pcr.setDeleted(true);
        repository.save(pcr);
    }

    public Pcr getPcrById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("PCR not found with id: " + id));
    }

    public Pcr saveOrUpdate(PcrDto pcrDto) {
        Pcr entity;
        if (pcrDto.getPcrId() != null && pcrDto.getPcrId() > 0) {
            entity = repository.findById(pcrDto.getPcrId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid PCR ID"));
        } else {
            entity = new Pcr();
        }
        entity.setClientName(pcrDto.getClientName());
        entity.setNotes(pcrDto.getNotes());
        entity.setDateCompleted(pcrDto.getDateCompleted());
        entity.setFileName(pcrDto.getFileName());
        entity.setPcrFile(pcrDto.getPcrFile());
        entity.setDateApproved(pcrDto.getDateApproved());
        entity.setDateBilled(pcrDto.getDateBilled());
        entity.setDateCompleted(pcrDto.getDateCompleted());
        return repository.save(entity);
    }

    public Page<Pcr> getPcrs(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findSortedPcr(pageable);
    }


}