package com.example.demo.repository;

import com.example.demo.entity.Ligne;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LigneRepository extends JpaRepository<Ligne, Long> {
    List<Ligne> findByCommandeIdOrderByIdAsc(Long commandeId);
    void deleteByIdAndCommandeId(Long id, Long commandeId);
}