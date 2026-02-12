package com.example.demo.kafka;

import java.util.List;

public class OrderSubmittedEvent {
    public Long commandeId;
    public String clientEmail;
    public List<LigneDto> lignes;

    public static class LigneDto {
        public String libelle;
        public int quantite;

        public LigneDto() {
        }

        public LigneDto(String libelle, int quantite) {
            this.libelle = libelle;
            this.quantite = quantite;
        }
    }
}