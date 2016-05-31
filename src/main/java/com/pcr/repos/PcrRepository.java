package com.pcr.repos;

import com.pcr.domain.Pcr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PcrRepository extends JpaRepository<Pcr, Long> {
    List<Pcr> findByDateDeletedIsNullOrderByDateApprovedAsc(); // For Yellow Tiles
    List<Pcr> findByDateDeletedIsNullOrderByDateBilledAsc(); // For Grey Tiles
    List<Pcr> findByDateDeletedIsNullOrderByDateCompletedAsc(); // For Green Tiles

    @Query("""
        SELECT p 
        FROM Pcr p
        WHERE p.deleted = false
        ORDER BY 
            CASE 
                WHEN p.dateCompleted IS NULL AND p.dateBilled IS NULL THEN 1
                WHEN p.dateCompleted IS NOT NULL AND p.dateBilled IS NULL THEN 2
                ELSE 3
            END,
            CASE 
                WHEN p.dateCompleted IS NULL AND p.dateBilled IS NULL THEN p.dateApproved
                WHEN p.dateCompleted IS NOT NULL AND p.dateBilled IS NULL THEN p.dateCompleted
                ELSE p.dateBilled
            END ASC
    """)
    Page<Pcr> findSortedPcr(Pageable pageable);

}