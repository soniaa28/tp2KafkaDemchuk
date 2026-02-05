package com.example.demo.repository;

import com.example.demo.entity.Commande;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommandeRepository extends JpaRepository<Commande, Long> {

    List<Commande> findByClientEmailOrderByCreatedAtDesc(String email);
    Optional<Commande> findByIdAndClientEmail(Long id, String email);
}