package com.kapture.nlpdashboardservice.repository.mysql;

import com.kapture.nlpdashboardservice.model.NLPDPrompt;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NLPDPromptRepository extends JpaRepository<NLPDPrompt, Integer> {

    List<NLPDPrompt> findAllByCmIdAndEmpIdOrderByCreateTimeDesc(int cmId, int empId, Pageable pageable);
}
