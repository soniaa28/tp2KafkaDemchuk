package com.example.demo.entity;

import jakarta.persistence.*;
@Entity
public class Ligne {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String libelle;
    private int quantite;
    private double prixUnitaire;

    @ManyToOne(optional = false)
    @JoinColumn(name = "commande_id")
    private Commande commande;

    public Ligne() {}
    public Ligne(Commande commande, String libelle, int quantite , double prixUnitaire)
    {
        this.commande = commande;
        this.libelle = libelle;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
    }
    public Long getId() {return id;}
    public String getLibelle(){return libelle;}
    public int getQuantite(){return quantite;}
    public double getPrixUnitaire(){return prixUnitaire;}
    public Commande getCommande() {return commande; }

    public void setLibelle(String libelle) { this.libelle = libelle; }
    public void setQuantite(int quantite) { this.quantite = quantite; }
    public void setPrixUnitaire(double prixUnitaire) { this.prixUnitaire = prixUnitaire; }
    public void setCommande(Commande commande) { this.commande = commande; }
}



