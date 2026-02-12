package fr.univ.lille.stock.kafka;

import java.util.List;

public class OrderSubmittedEvent {
    public Long commandeId;
    public String clientEmail;
    public List<LigneDto> lignes;

    public static class LigneDto {
        public String libelle;
        public Integer quantite;
    }
}