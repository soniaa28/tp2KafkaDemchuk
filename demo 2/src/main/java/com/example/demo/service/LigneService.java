package com.example.demo.service;

import com.example.demo.entity.Commande;
import com.example.demo.entity.Ligne;
import com.example.demo.repository.LigneRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LigneService {
    private final LigneRepository ligneRepository;

    public LigneService(LigneRepository ligneRepository) {
        this.ligneRepository = ligneRepository;
    }

    public List<Ligne> listByCommande(Long commandeId) {
        return ligneRepository.findByCommandeIdOrderByIdAsc(commandeId);
    }

    public void add(Commande commande, String libelle, int quantite, double prixUnitaire) {
        ligneRepository.save(new Ligne(commande, libelle, quantite, prixUnitaire));
    }
    @Transactional
    public void delete(Long commandeId, Long ligneId) {
        ligneRepository.deleteByIdAndCommandeId(ligneId, commandeId);
    }
}